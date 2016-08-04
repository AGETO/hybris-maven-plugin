package com.divae.ageto.hybris.codegenerator;

import java.io.File;

import org.testng.annotations.Test;

import com.divae.ageto.hybris.utils.EnvironmentUtils;

/**
 * @author Klaus Hauschild
 */
public class ExtensionInfoIT {

    @Test
    public void newExtensionInfoTest() {
        ExtensionInfo.newExtensionInfo(
                new File(EnvironmentUtils.getHybrisInstallationDirectory(), "bin/platform/ext/core/extensioninfo.xml"));
    }

}
