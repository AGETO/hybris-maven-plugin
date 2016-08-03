package com.divae.ageto.hybris.codegenerator;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import com.google.common.collect.Lists;

/**
 * @author khauschild
 * @date 23.03.2016
 */
class CodeGenerator {

    private static final Class<?> CODE_GENERATOR__CLASS = Utils.loadClass("de.hybris.bootstrap.codegenerator.CodeGenerator");

    static void generate(final File hybrisReactorDir) {
        final Collection<Object> extensionInfos = getExtensionInfos(hybrisReactorDir);
    }

    private static Collection<Object> getExtensionInfos(final File hybrisReactorDir) {
        final List<Object> extensionInfos = Lists.newArrayList();
        for (final File extensionDir : hybrisReactorDir.listFiles()) {
            if (!extensionDir.isDirectory()) {
                continue;
            }
            extensionInfos.add(ExtensionInfo
                    .newExtensionInfo(new File(extensionDir, "src/main/resources/" + ExtensionInfo.EXTENSION_INFO__FILE)));
        }
        return extensionInfos;
    }

    public static void generate_() {
        final Properties environmentProperties = new Properties();
        environmentProperties.setProperty("HYBRIS_BIN_DIR", "TODO");
        environmentProperties.setProperty("HYBRIS_TEMP_DIR", "TODO");
        environmentProperties.setProperty("HYBRIS_ROLES_DIR", "TODO");
        environmentProperties.setProperty("HYBRIS_LOG_DIR", "TODO");
        environmentProperties.setProperty("HYBRIS_DATA_DIR", "TODO");
        environmentProperties.setProperty("HYBRIS_BOOTSTRAP_BIN_DIR", "TODO");
        environmentProperties.setProperty("HYBRIS_CONFIG_DIR", "TODO");
        // TODO fill properties

        // system configuration
        final Object systemConfig = SystemConfig.getInstanceByProps(environmentProperties);

        // platform configuration
        final Object platformConfig = PlatformConfig.getInstance(systemConfig);

        // code generator
        final Method generateMethod = Utils.getMethod(CODE_GENERATOR__CLASS, "generate", PlatformConfig.PLATFORM_CONFIG__CLASS);
        Utils.invoke(generateMethod, null, platformConfig);
    }

}
