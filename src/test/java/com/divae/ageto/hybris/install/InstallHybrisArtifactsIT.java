package com.divae.ageto.hybris.install;

import org.testng.annotations.Test;

import java.io.File;

import static org.testng.Assert.fail;

/**
 * @author Klaus Hauschild
 */
public class InstallHybrisArtifactsIT {

    private static final String hybrisDirectoryEnvKey = "HybrisDirectory";

    @Test
    public void installHybrisArtifactsTest() {
        String hybrisFolder = System.getenv(hybrisDirectoryEnvKey);
        if (hybrisFolder == null) {
            hybrisDirNotFound(hybrisDirectoryEnvKey);
        }
        else {
            final InstallHybrisArtifacts installHybrisArtifacts = new InstallHybrisArtifacts(new File(
                    hybrisFolder));
            installHybrisArtifacts.execute();
        }
    }

    private void hybrisDirNotFound(String hybrisDir)
    {
        fail("Error: You have to set the environment variable \""+
                hybrisDirectoryEnvKey+"\" " +
                "to the folder of your hybris suite to run this test.");
    }
}
