package com.divae.ageto.hybris.install.extensions;

import java.io.File;
import java.util.List;

import com.divae.ageto.hybris.install.extensions.binary.ExtensionBinary;

/**
 * Created by mhaagen on 16.08.2016.
 */
public class WebExtension extends Extension {
    public WebExtension(final File baseDirectory, String name, ExtensionBinary binary) {
        super(baseDirectory, name, binary);
    }

    public WebExtension(final File baseDirectory, final String name, final ExtensionBinary binary,
            final List<Extension> dependencies) {
        super(baseDirectory, name, binary, dependencies);
    }

    @Override
    public File getResourcesDirectory() {
        return new File(getExtensionDirectory(), "src/main/webapp");
    }

    public File getWebResourcesFolder() {
        return super.getResourcesDirectory();
    }
}
