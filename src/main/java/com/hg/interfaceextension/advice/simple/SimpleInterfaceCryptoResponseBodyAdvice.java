package com.hg.interfaceextension.advice.simple;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import com.hg.interfaceextension.annotation.Encrypt;
import com.hg.interfaceextension.configuration.properties.SimpleInterfaceCryptoProperties;
import com.hg.interfaceextension.support.BaseCryptoManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 响应参数加密
 *
 * @author huangguang
 */
@ControllerAdvice
public class SimpleInterfaceCryptoResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    private final Logger logger = LoggerFactory.getLogger(SimpleInterfaceCryptoResponseBodyAdvice.class);
    /**
     * 提供加密解密的实体
     */
    @Autowired
    private BaseCryptoManager cryptoManager;

    /**
     * 全局框架配置
     */
    @Autowired
    private SimpleInterfaceCryptoProperties controlProperties;

    /**
     * 参数加密只支持加入了{@link Encrypt }的方法
     *
     * @param methodParameter the return type
     * @param converterType   the selected converter type
     */
    @Override
    public boolean supports(MethodParameter methodParameter, Class converterType) {
        return methodParameter.hasMethodAnnotation(Encrypt.class);
    }

    /**
     * 对当前请求参数加密后并封装
     *
     * @param body                  the body to be written
     * @param returnType            the return type of the controller method
     * @param selectedContentType   the content type selected through content negotiation
     * @param selectedConverterType the converter type selected to write to the response
     * @param request               the current request
     * @param response              the current response
     */
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {

        if (body == null) {
            return null;
        }
        try {
            JSONObject bodyJson = JSONObject.from(body);
            String responseParameterName = controlProperties.getResponseParameterName();
            Object responseData = bodyJson.get(responseParameterName);
            if (responseData == null) {
                return bodyJson;
            }
            String dataStr;
            if (ObjectUtil.isBasicType(responseData) || responseData instanceof String) {
                dataStr = responseData.toString();
            } else {
                JSONObject dataJson = JSONObject.from(responseData);
                dataStr = dataJson.toJSONString(JSONWriter.Feature.WriteMapNullValue);
            }
            String encryptStr = cryptoManager.encryptStr(dataStr, null);
            bodyJson.put(responseParameterName, encryptStr);
            return bodyJson;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
    }
}
