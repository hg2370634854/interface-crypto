package com.hg.interfaceextension.common.exception;

/**
 * IP地址不在白名单时的异常
 *
 * @author huangguang
 */
public class IPAddressException extends AuthException {
    public IPAddressException() {
        super("当前客户端IP不在白名单内");
    }
}
