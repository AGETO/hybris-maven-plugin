package com.divae.ageto.hybris.codegenerator;

import com.divae.ageto.hybris.EnvironmentUtils;
import org.testng.annotations.Test;

import java.io.File;

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
