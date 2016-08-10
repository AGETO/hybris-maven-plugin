package com.divae.ageto.hybris.install.extensions;

import com.divae.ageto.hybris.install.extensions.Extension;
import com.divae.ageto.hybris.install.extensions.binary.ExtensionBinary;
import com.divae.ageto.hybris.install.extensions.binary.JARArchive;

import java.io.File;

/**
 * Created by mhaagen on 10.08.2016.
 */
public class AdvancedSavedQuery implements Extension {
    @Override
    public ExtensionBinary getExtensionBinary() {
        return new JARArchive(new File("advancedsavedqueryserver.jar"));
    }

    @Override
    public String getExtensionName() {
        return "advancedsavedquery";
    }

    @Override
    public File getExtensionBaseDirectory() {
        return new File("ext/advancedsavedquery");
    }
}
