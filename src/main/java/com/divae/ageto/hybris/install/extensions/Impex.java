package com.divae.ageto.hybris.install.extensions;

import com.divae.ageto.hybris.install.extensions.binary.ExtensionBinary;
import com.divae.ageto.hybris.install.extensions.binary.JARArchive;

import java.io.File;

/**
 * Created by mhaagen on 10.08.2016.
 */
public class Impex implements Extension {
    @Override
    public File getExtensionBaseDirectory() {
        return new File("ext/impex");
    }

    @Override
    public String getExtensionName() {
        return "impex";
    }

    @Override
    public ExtensionBinary getExtensionBinary() {
        return new JARArchive(new File("impexserver.jar"));
    }
}
