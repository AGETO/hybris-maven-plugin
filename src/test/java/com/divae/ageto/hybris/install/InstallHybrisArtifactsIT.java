package com.divae.ageto.hybris.install;

import java.io.File;

import org.testng.annotations.Test;

import com.divae.ageto.hybris.utils.EnvironmentUtils;

/**
 * @author Klaus Hauschild
 */
public class InstallHybrisArtifactsIT {

    @Test
    public void installHybrisArtifactsTest() {
        final File hybrisInstallationDirectory = EnvironmentUtils.getHybrisInstallationDirectory();
        final InstallHybrisArtifacts installHybrisArtifacts = new InstallHybrisArtifacts(hybrisInstallationDirectory, null,
                false);
        installHybrisArtifacts.execute();
    }

}
