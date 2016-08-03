package com.divae.ageto.hybris;

import java.io.File;

import com.google.common.base.Strings;

/**
 * @author Klaus Hauschild
 */
public enum EnvironmentUtils {

    ;

    private static final String HYBRIS_INSTALL_DIR_ENVKEY = "HYBRIS_INSTALL_DIR";

    public static File getHybrisInstallationDirectory() {
        final String hybrisInstallDirectory = System.getenv(HYBRIS_INSTALL_DIR_ENVKEY);
        if (Strings.isNullOrEmpty(hybrisInstallDirectory)) {
            throw new IllegalStateException(String.format(
                    "Configure hybris installation directory by setting environment variable '%s'", HYBRIS_INSTALL_DIR_ENVKEY));
        }
        return new File(hybrisInstallDirectory);
    }

}
