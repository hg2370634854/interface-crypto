package com.hg.interfaceextension.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 简单模式下的全局配置类
 *
 * @author huangguang
 */
@ConfigurationProperties(prefix = "com.hg.interaceextension")
public class SimpleInterfaceCryptoProperties extends BaseProperties {

    /**
     * 接口授权超时时间（S）
     */
    private Integer requestDifferSeconds = 60;


    public Integer getRequestDifferSeconds() {
        return requestDifferSeconds;
    }

    public void setRequestDifferSeconds(Integer requestDifferSeconds) {
        this.requestDifferSeconds = requestDifferSeconds;
    }
}
