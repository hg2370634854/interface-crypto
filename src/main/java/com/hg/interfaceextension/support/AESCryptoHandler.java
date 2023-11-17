package com.hg.interfaceextension.support;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;

/**
 * AES类型的处理器
 *
 * @author huangguang
 */
public class AESCryptoHandler implements BaseCryptoHandler {

    private final AES aes;

    public AESCryptoHandler(String AESKey) {
        if (!StringUtils.hasText(AESKey)) {
            throw new IllegalArgumentException("AES密钥不能为空值");
        }
        aes = SecureUtil.aes(AESKey.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String encryptStr(String plaintext) {
        String cipherText = aes.encryptHex(plaintext);
        if (StringUtils.hasText(cipherText)) {
            return cipherText.toUpperCase();
        }
        return null;
    }

    @Override
    public String decryptStr(String ciphertext) {
        return aes.decryptStr(ciphertext);
    }
}
