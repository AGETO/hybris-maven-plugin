package com.divae.ageto.hybris.install.task;

import java.io.File;

import com.divae.ageto.hybris.utils.FileUtils;

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

        FileUtils.makeDirectory(directoryToCreate);
    }

}
