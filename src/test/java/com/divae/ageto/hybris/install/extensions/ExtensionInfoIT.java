package com.divae.ageto.hybris.install.extensions;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.io.File;
import java.util.List;

import org.testng.annotations.Test;

/**
 * Created by mhaagen on 11.08.2016.
 */
public class ExtensionInfoIT {

    @Test
    public void testGetExtensionName() throws Exception {
        assertEquals(ExtensionInfo
                .getExtensionName(new File(com.divae.ageto.hybris.utils.EnvironmentUtils.getHybrisInstallationDirectory(),
                        "bin/platform/ext/core/extensioninfo.xml")),
                "core");
    }

    @Test
    public void testGetDependencyNames() throws Exception {
        List<String> dependencies = ExtensionInfo
                .getDependencyNames(new File(com.divae.ageto.hybris.utils.EnvironmentUtils.getHybrisInstallationDirectory(),
                        "bin/platform/ext/core/extensioninfo.xml"));
        assertEquals(dependencies.size(), 0);
    }

    @Test
    public void testGetDependencyNames2() throws Exception {
        List<String> dependencies = ExtensionInfo
                .getDependencyNames(new File(com.divae.ageto.hybris.utils.EnvironmentUtils.getHybrisInstallationDirectory(),
                        "bin/platform/ext/advancedsavedquery/extensioninfo.xml"));
        assertEquals(dependencies.size(), 3);
        assertTrue(dependencies.contains("impex"));
        assertTrue(dependencies.contains("processing"));
        assertTrue(dependencies.contains("catalog"));
    }

}
