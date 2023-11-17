package com.hg.interfaceextension.common.service;

import com.hg.interfaceextension.common.entity.AccessDetail;

/**
 * 查询授权信息服务，建议此接口的实现类加入缓存
 *
 * @author huangguang
 */
public interface AccessDetailService {


    /**
     * 通过授权key查询授权信息
     *
     * @param accessKey 授权码
     */
    AccessDetail loadSysAccessByAccessKey(String accessKey);
}
