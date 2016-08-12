package com.divae.ageto.hybris.install.extensions.binary;

import java.io.File;

/**
 * Created by mhaagen on 12.08.2016.
 */
public class None implements ExtensionBinary {

    @Override
    public File getExtensionBinaryPath() {
        throw new RuntimeException("This extension have no binary, so it also does not have a binary path");
    }

    @Override
    public String getType() {
        return "none";
    }
}
