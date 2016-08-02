package com.divae.ageto.hybris.install;

import java.io.File;

import com.divae.ageto.hybris.Constants;
import org.testng.annotations.Test;

/**
 * @author Klaus Hauschild
 */
public class InstallHybrisArtifactsIT {

    @Test
    public void installHybrisArtifactsTest() {
        final InstallHybrisArtifacts installHybrisArtifacts = new InstallHybrisArtifacts(new File(
                Constants.HYBRIS_INSTALLATION_DIR));
        installHybrisArtifacts.execute();
    }

}
