package com.hg.interfaceextension.common.exception;

/**
 * 请求失效
 *
 * @author huangguang
 */
public class RequestExpiredException extends AuthException {

    public RequestExpiredException() {
        super("请求已失效");
    }
}
