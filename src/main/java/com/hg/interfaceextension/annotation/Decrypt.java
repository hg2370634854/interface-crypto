package com.hg.interfaceextension.annotation;

import java.lang.annotation.*;

/**
 * 对添加controller的方法接收的数据进行解密，只适用于H5端密钥
 *
 * @author huangguang
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface Decrypt {
}