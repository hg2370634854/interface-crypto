package com.hg.interfaceextension.configuration;

/**
 * 框架开启的功能模式
 *
 * @author huangguang
 */
public enum Mode {
    /**
     * 简单模式，框架只开启参数的加密解密功能
     */
    SIMPLE,
    /**
     * 全功能模式，框架除了对参数加密解密以外，还对指定的接口进行校验授权
     */
    COMPLETE
}
