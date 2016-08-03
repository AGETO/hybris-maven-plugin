package com.divae.ageto.hybris.codegenerator;

import java.io.File;
import java.lang.reflect.Constructor;

/**
 * @author Klaus Hauschild
 */
enum ExtensionInfo {

    ;

    static final String                 EXTENSION_INFO__FILE        = "extensioninfo.xml";

    static final Class<?>               EXTENSION_INFO__CLASS       = Utils.loadClass("de.hybris.bootstrap.config.ExtensionInfo");

    private static final Constructor<?> EXTENSION_INFO__CONSTRUCTOR = Utils.getConstructor(EXTENSION_INFO__CLASS,
            PlatformConfig.PLATFORM_CONFIG__CLASS, File.class, boolean.class);

    public static Object newExtensionInfo(final File extensionFile) {
        return Utils.newInstance(EXTENSION_INFO__CONSTRUCTOR, null, extensionFile, true);
    }

}
