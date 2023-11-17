package com.hg.interfaceextension.interceptor;

import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONObject;
import com.hg.interfaceextension.annotation.RequestEncryptParam;
import com.hg.interfaceextension.common.InterfaceCryptoConstants;
import com.hg.interfaceextension.configuration.properties.BaseProperties;
import com.hg.interfaceextension.support.BaseCryptoManager;
import com.hg.interfaceextension.support.RequestEncryptParamHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 请求加密参数拦截器，用于统一处理解密内容，暂时性保存
 *
 * @author huangguang
 */
public class InterfaceCryptoInterceptor implements HandlerInterceptor {

    private final Logger logger = LoggerFactory.getLogger(InterfaceCryptoInterceptor.class);

    /**
     * 全局配置类
     */
    @Autowired
    private BaseProperties baseProperties;

    /**
     * 加密解密管理器
     */
    @Autowired
    private BaseCryptoManager cryptoManager;

    /**
     * 只会对控制器中的方法做出拦截，并且控制器中的方法参数只杀需要有一个加 @RequestEncryptParam注解的才进行处理
     *
     * @param request  current HTTP request
     * @param response current HTTP response
     * @param handler  chosen handler to execute, for type and/or instance evaluation
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        String requestParameterName = baseProperties.getRequestParameterName();
        String data = request.getParameter(requestParameterName);
        if (!StringUtils.hasText(data)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        MethodParameter[] methodParameters = handlerMethod.getMethodParameters();

        boolean isHasEncryptParameter = false;
        for (MethodParameter methodParameter : methodParameters) {
            RequestEncryptParam encryptParameter = methodParameter.getParameterAnnotation(RequestEncryptParam.class);
            if (encryptParameter != null) {
                isHasEncryptParameter = true;
                break;
            }
        }

        if (isHasEncryptParameter) {
            String accessKey = request.getHeader(InterfaceCryptoConstants.ACCESS_KEY_NAME);
            String decryptStr = cryptoManager.decryptStr(data, accessKey);
            if (StringUtils.hasText(decryptStr)) {
                try {
                    JSONObject decryptJson = JSONObject.parseObject(decryptStr);
                    RequestEncryptParamHolder.setDecryptJson(decryptJson);
                } catch (JSONException e) {
                    logger.error(e.getMessage(), e);
                    throw e;
                }
            }
        }
        return true;
    }

    /**
     * 对此次请求的参数临时缓存进行清理
     *
     * @param request  current HTTP request
     * @param response current HTTP response
     * @param handler  the handler (or {@link HandlerMethod}) that started asynchronous
     *                 execution, for type and/or instance examination
     * @param ex       any exception thrown on handler execution, if any; this does not
     *                 include exceptions that have been handled through an exception resolver
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        RequestEncryptParamHolder.clear();
    }
}
