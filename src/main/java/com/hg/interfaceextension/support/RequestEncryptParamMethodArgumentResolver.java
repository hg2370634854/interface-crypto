package com.hg.interfaceextension.support;

import com.alibaba.fastjson2.JSONObject;
import com.hg.interfaceextension.annotation.RequestEncryptParam;
import org.springframework.beans.BeanUtils;
import org.springframework.core.MethodParameter;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.annotation.RequestParamMethodArgumentResolver;
import org.springframework.web.multipart.support.MultipartResolutionDelegate;

import java.util.Map;

/**
 * 处理使用了 {@link RequestEncryptParam} 注解的方法参数
 *
 * @author huangguang
 */
public class RequestEncryptParamMethodArgumentResolver extends RequestParamMethodArgumentResolver {

    public RequestEncryptParamMethodArgumentResolver() {
        super(true);
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        if (parameter.hasParameterAnnotation(RequestEncryptParam.class)) {
            if (Map.class.isAssignableFrom(parameter.nestedIfOptional().getNestedParameterType())) {
                RequestEncryptParam requestEncryptParam = parameter.getParameterAnnotation(RequestEncryptParam.class);
                return (requestEncryptParam != null && StringUtils.hasText(requestEncryptParam.name()));
            } else {
                return true;
            }
        } else {
            if (parameter.hasParameterAnnotation(RequestPart.class)) {
                return false;
            }
            parameter = parameter.nestedIfOptional();
            // 不支持上传文件参数
            if (MultipartResolutionDelegate.isMultipartArgument(parameter)) {
                return false;
            } else {
                return BeanUtils.isSimpleProperty(parameter.getNestedParameterType());
            }
        }
    }

    @Override
    protected Object resolveName(String name, MethodParameter parameter, NativeWebRequest request) throws Exception {

        JSONObject decryptJson = RequestEncryptParamHolder.getDecryptJson();
        if (decryptJson != null) {
            Object arg = decryptJson.get(name);
            return arg;
        }
        return null;
    }
}
