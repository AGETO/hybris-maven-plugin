package com.divae.ageto.hybris.install;

import com.divae.ageto.hybris.EnvironmentUtils;
import org.testng.annotations.Test;

import java.io.File;

/**
 * @author Klaus Hauschild
 */
public class InstallHybrisArtifactsIT {

    private static final String hybrisDirectoryEnvKey = "HybrisDirectory";

    @Test
    public void installHybrisArtifactsTest() {
        final File hybrisInstallationDirectory = EnvironmentUtils.getHybrisInstallationDirectory();
        final InstallHybrisArtifacts installHybrisArtifacts = new InstallHybrisArtifacts(hybrisInstallationDirectory);
        installHybrisArtifacts.execute();
    }

}
