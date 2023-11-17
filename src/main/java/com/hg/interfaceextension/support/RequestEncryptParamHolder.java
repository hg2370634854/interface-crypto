package com.hg.interfaceextension.support;

import com.alibaba.fastjson2.JSONObject;
import com.hg.interfaceextension.annotation.RequestEncryptParam;

/**
 * 用于临时缓存每次请求的参数，当控制器中有多个使用了 {@link RequestEncryptParam } 加密参数，
 * 可能需要进行多次的加密处理
 *
 * @author huangguang
 */
public class RequestEncryptParamHolder {

    private static final ThreadLocal<JSONObject> requestDecryptJsonHolder = new ThreadLocal<>();

    public static void setDecryptJson(JSONObject decryptJson) {
        requestDecryptJsonHolder.set(decryptJson);
    }

    public static JSONObject getDecryptJson() {
        return requestDecryptJsonHolder.get();
    }

    public static void clear() {
        requestDecryptJsonHolder.remove();
    }
}
