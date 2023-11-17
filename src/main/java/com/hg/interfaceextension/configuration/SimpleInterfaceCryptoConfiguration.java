package com.hg.interfaceextension.configuration;

import com.hg.interfaceextension.advice.simple.SimpleInterfaceCryptoRequestBodyAdvice;
import com.hg.interfaceextension.advice.simple.SimpleInterfaceCryptoResponseBodyAdvice;
import com.hg.interfaceextension.configuration.properties.SimpleInterfaceCryptoProperties;
import com.hg.interfaceextension.interceptor.InterfaceCryptoInterceptor;
import com.hg.interfaceextension.support.AESCryptoHandler;
import com.hg.interfaceextension.support.BaseCryptoManager;
import com.hg.interfaceextension.support.SimpleCryptoManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 简单模式下的Bean相关配置
 *
 * @author huangguang
 */
@EnableConfigurationProperties(SimpleInterfaceCryptoProperties.class)
@Configuration
public class SimpleInterfaceCryptoConfiguration {

    /**
     * 框架中相关的自定义配置
     */
    @Autowired
    protected SimpleInterfaceCryptoProperties controlProperties;

    /**
     * 加密管理器
     */
    @Bean
    public BaseCryptoManager cryptoManager() {
        AESCryptoHandler aesCryptoHandler = new AESCryptoHandler(controlProperties.getParamEncryptKey());
        return new SimpleCryptoManager(aesCryptoHandler);
    }

    /**
     * 请求参数解密增强
     */
    @Bean
    public SimpleInterfaceCryptoRequestBodyAdvice controlRequestBodyAdvice() {
        return new SimpleInterfaceCryptoRequestBodyAdvice();
    }

    /**
     * 响应参数加密增强
     */
    @Bean
    public SimpleInterfaceCryptoResponseBodyAdvice controlResponseBodyAdvice() {
        return new SimpleInterfaceCryptoResponseBodyAdvice();
    }

    /**
     * 接口拦截器
     */
    @Bean
    public InterfaceCryptoInterceptor interfaceCryptoInterceptor() {
        return new InterfaceCryptoInterceptor();
    }
}
