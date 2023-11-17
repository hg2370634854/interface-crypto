package com.hg.interfaceextension.annotation;

import java.lang.annotation.*;

/**
 * 对添加controller的方法返回的数据进行加密，适用于H5端和Partner端的密钥
 *
 * @author huangguang
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface Encrypt {
}
