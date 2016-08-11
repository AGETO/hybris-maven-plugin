package com.divae.ageto.hybris.install.task;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;

import com.divae.ageto.hybris.install.task.copy.CopyDirectoryContentToDirectoryTask;
import com.divae.ageto.hybris.install.task.copy.CopyFileToDirectoryTask;
import com.divae.ageto.hybris.utils.EnvironmentUtils;

/**
 * @author Marvin Haagen
 */
public class RestructurePlatformTask extends AbstractWorkDirectoryTask {

    @Override
    protected void execute(final TaskContext taskContext, final File workDirectory) {
        final File resourcesDirectory = new File("src/main/resources");
        File hybrisInstallationDirectory = EnvironmentUtils.getHybrisInstallationDirectory();
        new TaskChainTask(Arrays.<InstallTask>asList( //
                new CopyFileToDirectoryTask(new File(hybrisInstallationDirectory + "/bin/platform/project.properties"),
                        resourcesDirectory), //
                new CopyDirectoryContentToDirectoryTask(new File(hybrisInstallationDirectory + "/bin/platform/resources"),
                        resourcesDirectory), //
                new CopyDirectoryContentToDirectoryTask(
                        new File(hybrisInstallationDirectory + "/bin/platform/bootstrap/resources"),
                        new File(resourcesDirectory + "/bootstrap")), //
                new CreatePomTask("com/divae/ageto/hybris/install/models.pom.xml", "models",
                        Collections.<String, String>emptyMap()) //
        )).execute(taskContext);
    }

}
