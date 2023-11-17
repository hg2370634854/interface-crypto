package com.hg.interfaceextension.common.exception;

/**
 * 鉴权失败
 *
 * @author huangguang
 */
public class AuthException extends RuntimeException {

    public AuthException(String message) {
        super(message);
    }
}
