package com.divae.ageto.hybris.install.task;

import java.io.File;
import java.util.Arrays;

/**
 * @author Marvin Haagen
 */
public class RestructurePlatformTask extends AbstractWorkDirectoryTask {

    @Override
    protected void execute(final TaskContext taskContext, final File workDirectory) {
        final String resourcesDirectory = "src/main/resources";
        new TaskChainTask(Arrays.<InstallTask>asList( //
                new CopyFilesTask("bin/platform/project.properties", resourcesDirectory), //
                new CopyFilesTask("bin/platform/resources", resourcesDirectory), //
                new CopyFilesTask("bin/platform/bootstrap/resources", resourcesDirectory + "/bootstrap") //
        )).execute(taskContext);
    }

}
