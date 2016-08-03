package com.divae.ageto.hybris.utils;

import static org.testng.Assert.fail;

import java.io.File;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.divae.ageto.hybris.EnvironmentUtils;

/**
 * @author Klaus Hauschild
 */
public class UtilsIT {

    @DataProvider
    public Object[][] getHybrisPlatformDirectoryTestDataProvider() {
        return new Object[][] { { new File("."), false, }, { EnvironmentUtils.getHybrisInstallationDirectory(), true, }, };
    }

    @Test(dataProvider = "getHybrisPlatformDirectoryTestDataProvider")
    public void unrecognizableFolderStructureTest(final File directoryToTest, final boolean hybrisInstallationExpected) {
        try {
            Utils.getHybrisPlatformDirectory(directoryToTest);
            if (!hybrisInstallationExpected) {
                fail("IllegalStateException expected");
            }
        } catch (final IllegalStateException exception) {
            if (hybrisInstallationExpected) {
                fail("IllegalStateException not expected");
            }
        }
    }

}
