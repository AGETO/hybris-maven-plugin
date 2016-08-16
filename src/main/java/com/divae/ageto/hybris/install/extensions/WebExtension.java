package com.divae.ageto.hybris.install.extensions;

import java.io.File;

import com.divae.ageto.hybris.install.extensions.binary.ExtensionBinary;

/**
 * Created by mhaagen on 16.08.2016.
 */
public class WebExtension extends Extension {
    public WebExtension(final File baseDirectory, String name, ExtensionBinary binary) {
        super(baseDirectory, name, binary);
    }

    @Override
    public File getResourcesDirectory() {
        return new File(getExtensionDirectory(), "src/main/webapp");
    }
}
