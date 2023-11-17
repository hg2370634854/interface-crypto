package com.hg.interfaceextension.configuration.properties;

/**
 * 全局框架配置基类
 *
 * @author huangguang
 */
public class BaseProperties {

    /**
     * 响应参数中加密字段的名称
     */
    private String responseParameterName = "data";

    /**
     * 请求参数中解密字段的名称
     */
    private String requestParameterName = "data";

    /**
     * 参数加密所需的key
     */
    private String paramEncryptKey;

    public String getParamEncryptKey() {
        return paramEncryptKey;
    }

    public void setParamEncryptKey(String paramEncryptKey) {
        this.paramEncryptKey = paramEncryptKey;
    }

    public String getResponseParameterName() {
        return responseParameterName;
    }

    public void setResponseParameterName(String responseParameterName) {
        this.responseParameterName = responseParameterName;
    }

    public String getRequestParameterName() {
        return requestParameterName;
    }

    public void setRequestParameterName(String requestParameterName) {
        this.requestParameterName = requestParameterName;
    }
}
