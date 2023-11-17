package com.hg.interfaceextension.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * 完整模式下的全局配置类
 *
 * @author huangguang
 */
@ConfigurationProperties(prefix = "com.hg.interaceextension")
public class CompleteInterfaceCryptoProperties extends BaseProperties {

    /**
     * 接口授权超时时间
     */
    private Integer requestDifferSeconds = 60;
    /**
     * 授权接口Filter表达式
     */
    private List<String> filterPattern;

    public Integer getRequestDifferSeconds() {
        return requestDifferSeconds;
    }

    public void setRequestDifferSeconds(Integer requestDifferSeconds) {
        this.requestDifferSeconds = requestDifferSeconds;
    }

    public List<String> getFilterPattern() {
        return filterPattern;
    }

    public void setFilterPattern(List<String> filterPattern) {
        this.filterPattern = filterPattern;
    }
}
