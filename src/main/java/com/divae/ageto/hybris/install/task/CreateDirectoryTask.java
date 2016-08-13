package com.divae.ageto.hybris.install.task;

import java.io.File;

/**
 * @author Klaus Hauschild
 */
class CreateDirectoryTask extends AbstractWorkDirectoryTask {

    private final File directory;

    public CreateDirectoryTask(final File directory) {
        this.directory = directory;
    }

    @Override
    protected void execute(final TaskContext taskContext, final File workDirectory) {
        final File directoryToCreate = new File(workDirectory, directory.toString());
        if (!directoryToCreate.exists() && !directoryToCreate.mkdirs()) {
            throw new RuntimeException(String.format("Unable to create directory %s", directoryToCreate));
        }
    }

}
