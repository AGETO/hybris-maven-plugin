package com.divae.ageto.hybris.install.task;

import java.io.File;

/**
 * @author Klaus Hauschild
 */
public class CreateDirectoryTask extends AbstractWorkDirectoryTask {

    private final String directory;

    public CreateDirectoryTask(final String directory) {
        this.directory = directory;
    }

    @Override
    protected void execute(final TaskContext taskContext, final File workDirectory) {
        final File directoryToCreate = new File(workDirectory, directory);
        if (!directoryToCreate.mkdirs()) {
            throw new RuntimeException(String.format("Unable to create %s", directoryToCreate));
        }
    }

}
