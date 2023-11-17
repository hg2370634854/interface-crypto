package com.hg.interfaceextension.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 加密管理器，用于简单功能模式下的管理器
 *
 * @author huangguang
 */
public class SimpleCryptoManager implements BaseCryptoManager {

    private BaseCryptoHandler cryptoHandler;

    public SimpleCryptoManager(BaseCryptoHandler cryptoHandler) {
        this.cryptoHandler = cryptoHandler;
    }

    private final Logger logger = LoggerFactory.getLogger(CompleteCryptoManager.class);

    /**
     * 加密明文
     *
     * @param plaintext 明文
     * @return 加密后的16进制字符串
     */
    @Override
    public String encryptStr(String plaintext, String accessKey) {
        String ciphertext = cryptoHandler.encryptStr(plaintext);
        if (logger.isDebugEnabled()) {
            logger.debug("明文：{}", plaintext);
            logger.debug("加密后的密文：{}", ciphertext);
        }
        return ciphertext;

    }

    /**
     * 解密密文
     *
     * @param ciphertext 16进制的密文
     * @return 解密后的字符串
     */
    @Override
    public String decryptStr(String ciphertext, String accessKey) {
        String decryptStr = cryptoHandler.decryptStr(ciphertext);
        if (logger.isDebugEnabled()) {
            logger.debug("请求的密文：{}", ciphertext);
            logger.debug("解密后明文：{}", decryptStr);
        }
        return decryptStr;
    }
}
