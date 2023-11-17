package com.hg.interfaceextension.configuration;

import com.hg.interfaceextension.interceptor.InterfaceCryptoInterceptor;
import com.hg.interfaceextension.support.RequestEncryptParamMethodArgumentResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * 对框架中的拦截器、参数解析器配置到spring容器中
 *
 * @author huangguang
 */
@Configuration
public class InterfaceCryptoWebMvcConfigurer implements WebMvcConfigurer {

    /**
     * 参数解密拦截器
     */
    @Autowired
    private InterfaceCryptoInterceptor interfaceCryptoInterceptor;

    /**
     * 添加拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interfaceCryptoInterceptor)
                .addPathPatterns("/**");
    }

    /**
     * 添加参数解析器
     *
     * @param resolvers initially an empty list
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new RequestEncryptParamMethodArgumentResolver());
    }
}