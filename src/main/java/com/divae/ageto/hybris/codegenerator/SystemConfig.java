package com.divae.ageto.hybris.codegenerator;

import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.Properties;

/**
 * @author Klaus Hauschild
 */
enum SystemConfig {

    ;

    static final Class<?>       SYSTEM_CONFIG__CLASS          = Utils.loadClass("de.hybris.bootstrap.config.SystemConfig");

    private static final Method GET_INSTANCE_BY_PROPS__METHOD = Utils.getMethod(SYSTEM_CONFIG__CLASS, "getInstanceByProps",
                                                                      Hashtable.class);

    public static Object getInstanceByProps(final Properties environmentProperties) {
        return Utils.invoke(GET_INSTANCE_BY_PROPS__METHOD, environmentProperties);
    }

}
