package com.divae.ageto.hybris.install.task;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

/**
 * Created by mhaagen on 05.08.2016.
 */
public class MoveFilesTask extends AbstractWorkDirectoryTask {

    private final File source;
    private final File targetDirectory;

    public MoveFilesTask(final File source, final File targetDirectory) {
        this.source = source;
        this.targetDirectory = targetDirectory;
    }

    @Override
    protected void execute(TaskContext taskContext, File workDirectory) {
        final File sourceDirectory = new File(taskContext.getHybrisDirectory(), this.source.toString());
        final File targetDirectory = new File(workDirectory, this.targetDirectory.toString());

        try {
            if (sourceDirectory.isDirectory()) {
                FileUtils.moveDirectoryToDirectory(sourceDirectory, targetDirectory, true);
            }
            if (sourceDirectory.isFile()) {
                FileUtils.moveFileToDirectory(sourceDirectory, targetDirectory, true);
            }
        } catch (IOException e) {
            throw new RuntimeException("Can not move directory '" + sourceDirectory + "' to '" + targetDirectory + "''");
        }
    }
}
