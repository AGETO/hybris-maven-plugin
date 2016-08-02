package com.divae.ageto.hybris.install.task;

import java.io.File;

/**
 * @author Klaus Hauschild
 */
public abstract class AbstractWorkDirectoryTask implements InstallTask {

    @Override
    public void execute(final TaskContext taskContext) {
        final File workDirectory = (File) taskContext.getParameter(CreateWorkDirectoryTask.WORK_DIRECTORY);
        execute(taskContext, workDirectory);
    }

    protected abstract void execute(final TaskContext taskContext, final File workDirectory);

}
