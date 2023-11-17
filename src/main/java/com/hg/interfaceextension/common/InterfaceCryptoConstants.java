package com.hg.interfaceextension.common;

/**
 * 框架相关常量
 *
 * @author huangguang
 */
public interface InterfaceCryptoConstants {

    /**
     * 接口鉴权过滤器中获取请求头中的字段名
     */
    public final String SIGNATURE_NAME = "X-Request-Signature";
    /**
     * 接口鉴权过滤器等获取授权密钥中的字段名
     */
    public final String ACCESS_KEY_NAME = "Access-Key";
    /**
     * 接口鉴权过滤器中获取授权时间戳的字段名
     */
    public final String TIMESTAMP_NAME = "Timestamp";
}
