package com.divae.ageto.hybris.codegenerator;

import java.io.File;
import java.lang.reflect.Method;
import java.util.*;

import com.divae.ageto.hybris.utils.EnvironmentUtils;
import com.google.common.collect.Lists;


/*import de.hybris.bootstrap.config.ConfigUtil;
import de.hybris.bootstrap.config.SystemConfig;
import de.hybris.bootstrap.config.PlatformConfig;
import de.hybris.bootstrap.codegenerator.CodeGenerator;
import de.hybris.bootstrap.typesystem.YTypeSystemLoader;*/

/**
 * @author khauschild
 * @date 23.03.2016
 */
class CodeGenerator {

    private static final Class<?> CODE_GENERATOR__CLASS = Utils.loadClass("de.hybris.bootstrap.codegenerator.CodeGenerator");

    /*public static void generate(final File hybrisReactorDir) {
        /*Hashtable<String, String> conf = new Hashtable<>();
        conf.put(SystemConfig.PROPERTY_BIN_DIR,
                EnvironmentUtils.getPropertyFromEnvPropFile(SystemConfig.PROPERTY_BIN_DIR).toString());
        conf.put(SystemConfig.PROPERTY_CONFIG_DIR,
                EnvironmentUtils.getPropertyFromEnvPropFile(SystemConfig.PROPERTY_CONFIG_DIR).toString());
        conf.put(SystemConfig.PROPERTY_BOOTSTRAP_BIN_DIR,
                EnvironmentUtils.getPropertyFromEnvPropFile(SystemConfig.PROPERTY_BOOTSTRAP_BIN_DIR).toString());
        conf.put(SystemConfig.PROPERTY_ROLES_DIR,
                EnvironmentUtils.getPropertyFromEnvPropFile(SystemConfig.PROPERTY_ROLES_DIR).toString());
        conf.put(SystemConfig.PROPERTY_DATA_DIR,
                EnvironmentUtils.getPropertyFromEnvPropFile(SystemConfig.PROPERTY_DATA_DIR).toString());
        conf.put(SystemConfig.PROPERTY_LOG_DIR,
                EnvironmentUtils.getPropertyFromEnvPropFile(SystemConfig.PROPERTY_LOG_DIR).toString());
        conf.put(SystemConfig.PROPERTY_TEMP_DIR,
                hybrisReactorDir.toString());
        conf.put(SystemConfig.PLATFORM_HOME,
                EnvironmentUtils.getHybrisPlatformDir().toString());

        SystemConfig sc = SystemConfig.getInstanceByProps(conf);

        de.hybris.bootstrap.config.PlatformConfig pc = de.hybris.bootstrap.config.PlatformConfig.getInstance(sc);

        //CodeGenerator.generate(pc);

        //CodeGenerator cg = new CodeGenerator(hybrisReactorDir.toString());
        CodeGenerator cg = new CodeGenerator(pc);

        //CodeGenerator cg = new CodeGenerator(hybrisReactorDir.toString());*/

        /*System.setProperty(SystemConfig.PROPERTY_BIN_DIR,
                EnvironmentUtils.getPropertyFromEnvPropFile(SystemConfig.PROPERTY_BIN_DIR).toString());
        System.setProperty(SystemConfig.PROPERTY_CONFIG_DIR,
                EnvironmentUtils.getPropertyFromEnvPropFile(SystemConfig.PROPERTY_CONFIG_DIR).toString());
        System.setProperty(SystemConfig.PROPERTY_BOOTSTRAP_BIN_DIR,
                EnvironmentUtils.getPropertyFromEnvPropFile(SystemConfig.PROPERTY_BOOTSTRAP_BIN_DIR).toString());
        System.setProperty(SystemConfig.PROPERTY_ROLES_DIR,
                EnvironmentUtils.getPropertyFromEnvPropFile(SystemConfig.PROPERTY_ROLES_DIR).toString());
        System.setProperty(SystemConfig.PROPERTY_DATA_DIR,
                EnvironmentUtils.getPropertyFromEnvPropFile(SystemConfig.PROPERTY_DATA_DIR).toString());
        System.setProperty(SystemConfig.PROPERTY_LOG_DIR,
                EnvironmentUtils.getPropertyFromEnvPropFile(SystemConfig.PROPERTY_LOG_DIR).toString());
        System.setProperty(SystemConfig.PROPERTY_TEMP_DIR,
                hybrisReactorDir.toString());
        System.setProperty(SystemConfig.PLATFORM_HOME,
                EnvironmentUtils.getHybrisPlatformDir().toString());

        CodeGenerator cg = new CodeGenerator(hybrisReactorDir.toString());

        cg.generate(cg.getPlatformConfig());*/

        /*String cmdline[] = {
                ""
        };

        CodeGenerator.main(cmdline);*/
    //}

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

    /*public static void generate_() {


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
    }*/
}
