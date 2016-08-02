package com.divae.ageto.hybris.codegenerator;

import java.io.File;

import com.divae.ageto.hybris.Constants;
import org.testng.annotations.Test;

/**
 * @author Klaus Hauschild
 */
public class ExtensionInfoIT {

    @Test
    public void newExtensionInfoTest() {
        ExtensionInfo.newExtensionInfo(new File(Constants.HYBRIS_INSTALLATION_DIR, "bin/platform/ext/core/extensioninfo.xml"));
    }

}
