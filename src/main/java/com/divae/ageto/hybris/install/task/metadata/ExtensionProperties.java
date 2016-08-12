package com.divae.ageto.hybris.install.task.metadata;

import java.io.File;

import com.divae.ageto.hybris.install.extensions.binary.ExtensionBinary;

/**
 * Created by mhaagen on 12.08.2016.
 */
public class ExtensionProperties {

    private final String          extensionName;
    private final File            extensionBaseDirectory;
    private final ExtensionBinary binary;

    public ExtensionProperties(final String extensionName, final File extensionBaseDirectory,
            final ExtensionBinary extensionBinary) {
        this.extensionName = extensionName;
        this.extensionBaseDirectory = extensionBaseDirectory;
        this.binary = extensionBinary;
    }

    public String getExtensionName() {
        return extensionName;
    }

    public File getExtensionBaseDirectory() {
        return extensionBaseDirectory;
    }

    public ExtensionBinary getBinary() {
        return binary;
    }
}
