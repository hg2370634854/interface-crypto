package com.hg.interfaceextension.configuration;

import com.hg.interfaceextension.annotation.EnableCrypto;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;

/**
 * 根据模式导入指定的bean到容器
 *
 * @author huangguang
 */
public class InterfaceCryptoConfigurationSelector implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        Class<?> annotationType = EnableCrypto.class;
        AnnotationAttributes attributes = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(
                annotationType.getName(), false));
        Assert.notNull(attributes, String.format("@%s未按预期出现在导入类'%s'上",
                annotationType.getSimpleName(), importingClassMetadata.getClassName()));

        String[] imports;
        if (attributes.containsKey("mode") && attributes.getEnum("mode") == Mode.SIMPLE) {
            imports = new String[]{
                    SimpleInterfaceCryptoConfiguration.class.getName(),
                    InterfaceCryptoWebMvcConfigurer.class.getName()
            };
        } else {
            imports = new String[]{
                    CompleteInterfaceCryptoConfiguration.class.getName(),
                    InterfaceCryptoWebMvcConfigurer.class.getName()
            };
        }

        return imports;
    }
}
