package com.hg.interfaceextension.support;

/**
 * 加密解密管理器
 *
 * @author huangguang
 */
public interface BaseCryptoManager {

    /**
     * 加密明文
     *
     * @param plaintext 明文
     * @param accessKey 授权码
     * @return 加密后的16进制字符串
     */
    public String encryptStr(String plaintext, String accessKey);

    /**
     * 解密密文
     *
     * @param ciphertext 16进制的密文
     * @param accessKey  授权码
     * @return 解密后的字符串
     */
    public String decryptStr(String ciphertext, String accessKey);
}
