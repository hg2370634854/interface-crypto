package com.hg.interfaceextension.annotation;

import com.hg.interfaceextension.configuration.InterfaceCryptoConfigurationSelector;
import com.hg.interfaceextension.configuration.Mode;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 开启安全控制
 *
 * @author huangguang
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(InterfaceCryptoConfigurationSelector.class)
public @interface EnableCrypto {
    /**
     * 模式，具体请看 {@link Mode } 的描述
     */
    Mode mode() default Mode.COMPLETE;
}
