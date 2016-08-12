package com.divae.ageto.hybris.install.extensions.binary;

import java.io.File;

/**
 * Created by mhaagen on 12.08.2016.
 */
public class ClassFolder implements ExtensionBinary {

    private final File classFolder;

    public ClassFolder(final File classFolder) {
        this.classFolder = classFolder;
    }

    @Override
    public File getExtensionBinaryPath() {
        return classFolder;
    }

    @Override
    public String getType() {
        return "class folder";
    }
}
