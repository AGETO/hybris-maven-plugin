package com.divae.ageto.hybris.utils;

import java.io.File;

/**
 * @author Klaus Hauschild
 */
public enum Utils {

    ;

    public static File getHybrisPlatformDirectory(final File hybrisDirectory) {
        final File hybrisPlatformDirectory = new File(new File(hybrisDirectory, "bin"), "platform");
        if (!hybrisPlatformDirectory.exists()) {
            throw new IllegalStateException("Unrecognizable hybris suite folder structure.");
        }
        return hybrisPlatformDirectory;
    }

}
