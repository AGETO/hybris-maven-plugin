package com.divae.ageto.hybris.install;

import java.io.File;

import com.divae.ageto.hybris.AbstractTempDirectoryTests;
import com.divae.ageto.hybris.utils.EnvironmentUtils;

/**
 * @author Klaus Hauschild
 */
public class InstallHybrisArtifactsIT extends AbstractTempDirectoryTests {

    public static void main(final String[] args) {
        new InstallHybrisArtifactsIT().installHybrisArtifactsTest();
    }

    // @Test
    public void installHybrisArtifactsTest() {
        final File hybrisInstallationDirectory = EnvironmentUtils.getHybrisInstallationDirectory();
        final InstallHybrisArtifacts installHybrisArtifacts = new InstallHybrisArtifacts(hybrisInstallationDirectory,
                getTempDirectory(), false);
        installHybrisArtifacts.execute();
    }

}
