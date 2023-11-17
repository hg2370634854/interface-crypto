package com.hg.interfaceextension.common.entity;

import java.io.Serializable;

/**
 * 授权信息
 *
 * @author huangguang
 */
public interface AccessDetail extends Serializable {

    static final long serialVersionUID = 1L;

    /**
     * 获取授权码，用于接口的授权操作，此值必须唯一
     */
    public String getAccessKey();

    /**
     * 获取授权密钥，用于接口的授权操作
     */
    public String getSecretKey();

    /**
     * 获取加密的密钥，用于接口参数的加密解密
     */
    public String getAesKey();

    /**
     * IPv4白名单,多项以’,‘分割
     */
    public String getWhiteIPList();
}
