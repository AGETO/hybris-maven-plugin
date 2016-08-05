package com.divae.ageto.hybris.install.task;

import java.io.File;

/**
 * @author Klaus Hauschild
 */
public abstract class AbstractWorkDirectoryTask implements InstallTask {

    @Override
    public void execute(final TaskContext taskContext) {
        final File workDirectory = getWorkDirectory(taskContext);
        execute(taskContext, workDirectory);
    }

    public static File getWorkDirectory(final TaskContext taskContext) {
        return (File) taskContext.getParameter(CreateWorkDirectoryTask.WORK_DIRECTORY);
    }

    protected abstract void execute(final TaskContext taskContext, final File workDirectory);

}
