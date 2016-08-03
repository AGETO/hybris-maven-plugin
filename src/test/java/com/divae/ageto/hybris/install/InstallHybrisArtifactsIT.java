package com.divae.ageto.hybris.install;

import java.io.File;

import org.testng.annotations.Test;

import com.divae.ageto.hybris.EnvironmentUtils;

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
