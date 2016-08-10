package com.divae.ageto.hybris.install.extensions;

import com.divae.ageto.hybris.install.extensions.binary.ExtensionBinary;

import java.io.File;

/**
 * Created by mhaagen on 10.08.2016.
 */
public interface Extension {

    File getExtensionBaseDirectory();

    String getExtensionName();

    ExtensionBinary getExtensionBinary();

}
