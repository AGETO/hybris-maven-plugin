package com.divae.ageto.hybris.codegenerator;

import java.lang.reflect.Method;

/**
 * @author Klaus Hauschild
 */
enum PlatformConfig {

    ;

    static final Class<?>       PLATFORM_CONFIG__CLASS = Utils.loadClass("de.hybris.bootstrap.config.PlatformConfig");

    private static final Method GET_INSTANCE__METHOD   = Utils.getMethod(PLATFORM_CONFIG__CLASS, "getInstance",
            SystemConfig.SYSTEM_CONFIG__CLASS);

    public static Object getInstance(final Object systemConfig) {
        return Utils.invoke(GET_INSTANCE__METHOD, systemConfig);
    }

}
