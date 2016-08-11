package com.divae.ageto.hybris.install.extensions;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.io.File;
import java.util.List;

import org.testng.annotations.Test;

import com.divae.ageto.hybris.utils.EnvironmentUtils;

/**
 * @author Marvin Haagen
 */
public class ExtensionInfoIT {

    @Test
    public void extensionNameTest() throws Exception {
        final String extensionName = ExtensionInfo.getExtensionName(
                new File(EnvironmentUtils.getHybrisInstallationDirectory(), "bin/platform/ext/core/extensioninfo.xml"));
        assertEquals(extensionName, "core");
    }

    @Test
    public void dependencyNamesOfCoreTest() throws Exception {
        List<String> dependencies = ExtensionInfo
                .getDependencyNames(new File(com.divae.ageto.hybris.utils.EnvironmentUtils.getHybrisInstallationDirectory(),
                        "bin/platform/ext/core/extensioninfo.xml"));
        assertEquals(dependencies.size(), 0);
    }

    @Test
    public void dependencyNamesOfAdvancedSavedQueryTest() throws Exception {
        List<String> dependencies = ExtensionInfo
                .getDependencyNames(new File(com.divae.ageto.hybris.utils.EnvironmentUtils.getHybrisInstallationDirectory(),
                        "bin/platform/ext/advancedsavedquery/extensioninfo.xml"));
        assertEquals(dependencies.size(), 3);
        assertTrue(dependencies.contains("impex"));
        assertTrue(dependencies.contains("processing"));
        assertTrue(dependencies.contains("catalog"));
    }

}
