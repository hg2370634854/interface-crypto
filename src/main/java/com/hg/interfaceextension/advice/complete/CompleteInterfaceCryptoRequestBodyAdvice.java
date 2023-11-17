package com.hg.interfaceextension.advice.complete;

import com.hg.interfaceextension.annotation.Decrypt;
import com.hg.interfaceextension.common.InterfaceCryptoConstants;
import com.hg.interfaceextension.support.BaseCryptoManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

/**
 * 请求参数解密
 *
 * @author huangguang
 */
@ControllerAdvice
public class CompleteInterfaceCryptoRequestBodyAdvice extends RequestBodyAdviceAdapter {

    private final Logger logger = LoggerFactory.getLogger(CompleteInterfaceCryptoRequestBodyAdvice.class);
    @Autowired
    private BaseCryptoManager cryptoManager;
    @Autowired
    private HttpServletRequest httpServletRequest;

    /**
     * 只支持加入了 {@link Decrypt } 的方法
     *
     * @param methodParameter the method parameter
     * @param targetType      the target type, not necessarily the same as the method
     *                        parameter type, e.g. for {@code HttpEntity<String>}.
     * @param converterType   the selected converter type
     */
    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return methodParameter.hasMethodAnnotation(Decrypt.class);
    }

    /**
     * 对当前请求参数解密后并解封装
     *
     * @param inputMessage  the request
     * @param parameter     the target method parameter
     * @param targetType    the target type, not necessarily the same as the method
     *                      parameter type, e.g. for {@code HttpEntity<String>}.
     * @param converterType the converter used to deserialize the body
     */
    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        InputStream body = inputMessage.getBody();
        String encryptStr = StreamUtils.copyToString(body, StandardCharsets.UTF_8);
        if (logger.isDebugEnabled()) {
            logger.debug("请求参数加密值：{}", encryptStr);
        }
        if (StringUtils.hasText(encryptStr)) {
            try {
                String accessKey = httpServletRequest.getHeader(InterfaceCryptoConstants.ACCESS_KEY_NAME);
                String decryptStr = cryptoManager.decryptStr(encryptStr, accessKey);
                if (logger.isDebugEnabled()) {
                    logger.debug("请求参数解密值：{}", decryptStr);
                }
                return new HttpInputMessage() {
                    @Override
                    public InputStream getBody() {
                        return new ByteArrayInputStream(decryptStr.getBytes(StandardCharsets.UTF_8));
                    }

                    @Override
                    public HttpHeaders getHeaders() {
                        return inputMessage.getHeaders();
                    }
                };
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                throw e;
            }
        }
        return inputMessage;
    }
}
