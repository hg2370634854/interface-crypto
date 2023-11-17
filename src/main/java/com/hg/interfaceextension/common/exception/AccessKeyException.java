package com.hg.interfaceextension.common.exception;

/**
 * 授权信息异常
 *
 * @author huangguang
 */
public class AccessKeyException extends AuthException {
    public AccessKeyException() {
        super("授权码错误");
    }

    public AccessKeyException(String message) {
        super(message);
    }
}
