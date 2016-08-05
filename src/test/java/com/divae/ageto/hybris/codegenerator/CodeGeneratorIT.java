package com.divae.ageto.hybris.codegenerator;

import java.io.File;

import com.divae.ageto.hybris.install.InstallHybrisArtifacts;
import com.divae.ageto.hybris.install.task.AbstractWorkDirectoryTask;
import com.divae.ageto.hybris.utils.EnvironmentUtils;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * @author Klaus Hauschild
 */
public class CodeGeneratorIT {

    private File workDirectory;

    // @BeforeTest
    public void before() {
        final File hybrisInstallationDirectory = EnvironmentUtils.getHybrisInstallationDirectory();
        final InstallHybrisArtifacts installHybrisArtifacts = new InstallHybrisArtifacts(hybrisInstallationDirectory);
        installHybrisArtifacts.execute();
        workDirectory = AbstractWorkDirectoryTask.getWorkDirectory(installHybrisArtifacts.getTaskContext());
    }

    @Test
    public void generateTest() {
        final File hybrisInstallationDirectory = EnvironmentUtils.getHybrisInstallationDirectory();
        final InstallHybrisArtifacts installHybrisArtifacts = new InstallHybrisArtifacts(hybrisInstallationDirectory);
        installHybrisArtifacts.execute();
        workDirectory = AbstractWorkDirectoryTask.getWorkDirectory(installHybrisArtifacts.getTaskContext());
        CodeGenerator.generate(workDirectory);
        // CodeGenerator.generate(new File("C:\\Users\\mhaagen\\AppData\\Local\\Temp\\1470382850099-0"));
    }

}
