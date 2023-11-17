package com.hg.interfaceextension.support.event;

import org.springframework.context.ApplicationEvent;

/**
 * 加密处理器更新事件
 *
 * @author huangguang
 */
public class CryptoHandlerUpdateEvent extends ApplicationEvent {

    /**
     * 授权码
     */
    private final String accessKey;
    /**
     * 新的AES加密密钥
     */
    private final String aesKey;

    /**
     * 新的AES密钥
     *
     * @param source    来源
     * @param accessKey 授权码
     * @param aesKey    AES加密密钥
     */

    public CryptoHandlerUpdateEvent(Object source, String accessKey, String aesKey) {
        super(source);
        this.accessKey = accessKey;
        this.aesKey = aesKey;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public String getAesKey() {
        return aesKey;
    }
}
