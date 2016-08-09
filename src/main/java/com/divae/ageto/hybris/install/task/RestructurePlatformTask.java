package com.divae.ageto.hybris.install.task;

import java.io.File;
import java.util.Arrays;

/**
 * @author Marvin Haagen
 */
public class RestructurePlatformTask extends AbstractWorkDirectoryTask {

    @Override
    protected void execute(final TaskContext taskContext, final File workDirectory) {
        final File resourcesDirectory = new File("src/main/resources");
        new TaskChainTask(Arrays.<InstallTask>asList( //
                new CopyFileToDirectoryTask(new File("bin/platform/project.properties"), resourcesDirectory), //
                new CopyDirectoryContentToDirectoryTask(new File("bin/platform/resources"), resourcesDirectory), //
                new CopyDirectoryContentToDirectoryTask(new File("bin/platform/bootstrap/resources"),
                        new File(resourcesDirectory + "/bootstrap")) //
        )).execute(taskContext);
    }

}
