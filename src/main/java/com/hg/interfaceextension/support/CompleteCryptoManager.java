package com.hg.interfaceextension.support;

import com.hg.interfaceextension.common.entity.AccessDetail;
import com.hg.interfaceextension.common.service.AccessDetailService;
import com.hg.interfaceextension.configuration.properties.BaseProperties;
import com.hg.interfaceextension.support.event.CryptoHandlerUpdateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 加密管理器，用于完全功能模式下的管理器
 *
 * @author huangguang
 */
public class CompleteCryptoManager implements BaseCryptoManager, ApplicationListener<CryptoHandlerUpdateEvent>, InitializingBean {

    /**
     * 缓存，用于储存accessKey对应的加密处理器
     */
    private final Map<String, BaseCryptoHandler> CACHE_CRYPTO_HANDLE_MAP = new HashMap<>();

    private final String EMPTY_CACHE_KEY = null;

    private final Logger logger = LoggerFactory.getLogger(CompleteCryptoManager.class);

    @Autowired
    private AccessDetailService accessDetailService;

    @Autowired
    private BaseProperties baseProperties;

    /**
     * 加密明文
     *
     * @param plaintext 明文
     * @return 加密后的16进制字符串
     */
    @Override
    public String encryptStr(String plaintext, String accessKey) {
        BaseCryptoHandler cryptoHandler = getCryptoHandler(accessKey);
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
        BaseCryptoHandler cryptoHandler = getCryptoHandler(accessKey);
        String decryptStr = cryptoHandler.decryptStr(ciphertext);
        if (logger.isDebugEnabled()) {
            logger.debug("请求的密文：{}", ciphertext);
            logger.debug("解密后明文：{}", decryptStr);
        }
        return decryptStr;
    }

    /**
     * 获取本加密解密管理器中的加密解密处理器
     *
     * @param accessKey 授权码
     */
    private BaseCryptoHandler getCryptoHandler(String accessKey) {

      /*  if (!StringUtils.hasText(accessKey)) {
            throw new IllegalArgumentException("accessKey的值一定不能为空");
        }*/
        BaseCryptoHandler cryptoHandler = CACHE_CRYPTO_HANDLE_MAP.get(accessKey);
        if (cryptoHandler == null) {
            AccessDetail accessDetail = accessDetailService.loadSysAccessByAccessKey(accessKey);
            if (accessDetail == null) {
                throw new IllegalStateException("未找到与“" + accessKey + "”有关的授权信息");
            }
            String aesKey = accessDetail.getAesKey();
            if (!StringUtils.hasText(aesKey)) {
                throw new IllegalStateException("与“" + accessKey + "”有关的授权信息的’aesKey‘不能为空");
            }
            cryptoHandler = new AESCryptoHandler(aesKey);
            CACHE_CRYPTO_HANDLE_MAP.put(accessKey, cryptoHandler);
        }
        return cryptoHandler;
    }

    /**
     * 事件的监听，当 {@link BaseCryptoHandler } 中的值更新后需要更新 CACHE_CRYPTO_HANDLE_MAP 中对应的值，
     * 此事件的处理就是更新它们。
     * 注意：可能更新对应accessKey值的CryptoHandler在缓存中不存在，此时将会忽略此次更新
     */
    @Override
    public void onApplicationEvent(CryptoHandlerUpdateEvent event) {
        String accessKey = event.getAccessKey();
        String aesKey = event.getAesKey();
        if (!StringUtils.hasText(accessKey) || !StringUtils.hasText(aesKey)) {
            return;
        }
        BaseCryptoHandler cryptoHandler = CACHE_CRYPTO_HANDLE_MAP.get(accessKey);
        if (cryptoHandler == null) {
            return;
        }
        AESCryptoHandler aesCryptoHandler = new AESCryptoHandler(aesKey);
        CACHE_CRYPTO_HANDLE_MAP.put(accessKey, aesCryptoHandler);
    }

    @Override
    public void afterPropertiesSet() {
        String paramEncryptKey = baseProperties.getParamEncryptKey();
        AESCryptoHandler aesCryptoHandler = new AESCryptoHandler(paramEncryptKey);
        CACHE_CRYPTO_HANDLE_MAP.put(EMPTY_CACHE_KEY, aesCryptoHandler);
    }
}
