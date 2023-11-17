package com.hg.interfaceextension.configuration;

import cn.hutool.core.collection.CollUtil;
import com.hg.interfaceextension.advice.complete.CompleteInterfaceCryptoRequestBodyAdvice;
import com.hg.interfaceextension.advice.complete.CompletInterfaceCryptoResponseBodyAdvice;
import com.hg.interfaceextension.common.service.AccessDetailService;
import com.hg.interfaceextension.configuration.properties.CompleteInterfaceCryptoProperties;
import com.hg.interfaceextension.interceptor.InterfaceCryptoInterceptor;
import com.hg.interfaceextension.support.BaseCryptoManager;
import com.hg.interfaceextension.support.CompleteCryptoManager;
import com.hg.interfaceextension.web.filter.AuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.Filter;
import java.util.List;

/**
 * 完全模式下的Bean相关配置
 *
 * @author huangguang
 */
@EnableConfigurationProperties(CompleteInterfaceCryptoProperties.class)
@Configuration
public class CompleteInterfaceCryptoConfiguration {

    /**
     * 授权过滤器的创建和注入
     */
    @Bean
    @Autowired
    public FilterRegistrationBean<Filter> authFilter(CompleteInterfaceCryptoProperties controlProperties,
                                                     HandlerExceptionResolver handlerExceptionResolver,
                                                     AccessDetailService accessDetailService) {
        AuthFilter authFilter = new AuthFilter()
                .setControlProperties(controlProperties)
                .setHandlerExceptionResolver(handlerExceptionResolver)
                .setAccessDetailService(accessDetailService);
        FilterRegistrationBean<Filter> bean = new FilterRegistrationBean<>();
        // 设置过滤URL匹配符
        List<String> filterPattern = controlProperties.getFilterPattern();
        if (CollUtil.isEmpty(filterPattern)) {
            bean.addUrlPatterns("/*");
        } else {
            bean.addUrlPatterns(filterPattern.toArray(new String[0]));
        }
        bean.setFilter(authFilter);
        bean.setName("authFilter");
        bean.setOrder(1);
        return bean;
    }

    /**
     * 加密管理器
     */
    @Bean
    public BaseCryptoManager cryptoManager() {
        return new CompleteCryptoManager();
    }

    /**
     * 请求参数解密增强
     */
    @Bean
    public CompleteInterfaceCryptoRequestBodyAdvice controlRequestBodyAdvice() {
        return new CompleteInterfaceCryptoRequestBodyAdvice();
    }

    /**
     * 响应参数加密增强
     */
    @Bean
    public CompletInterfaceCryptoResponseBodyAdvice controlResponseBodyAdvice() {
        return new CompletInterfaceCryptoResponseBodyAdvice();
    }


    @Bean
    public InterfaceCryptoInterceptor interfaceCryptoInterceptor() {
        return new InterfaceCryptoInterceptor();
    }
}
