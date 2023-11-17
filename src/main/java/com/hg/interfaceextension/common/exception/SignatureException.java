package com.hg.interfaceextension.common.exception;

/**
 * 签名异常
 *
 * @author huangguang
 */
public class SignatureException extends AuthException {
    public SignatureException() {
        super("签名错误");
    }
}
