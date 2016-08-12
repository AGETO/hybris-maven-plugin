package com.divae.ageto.hybris.install.extensions.binary;

import java.io.File;

/**
 * Created by mhaagen on 10.08.2016.
 */
public class JARArchive implements ExtensionBinary {

    private final File archivePath;

    public JARArchive(final File archivePath) {
        this.archivePath = archivePath;
    }

    @Override
    public File getExtensionBinaryPath() {
        return archivePath;
    }

    @Override
    public String getType() {
        return "JAR archive";
    }
}
