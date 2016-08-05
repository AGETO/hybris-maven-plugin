package com.divae.ageto.hybris.install.task;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by mhaagen on 05.08.2016.
 */
public class MoveFilesTask extends AbstractWorkDirectoryTask {

    private final String source;
    private final String target;

    public MoveFilesTask(final String source, final String target) {
        this.source = source;
        this.target = target;
    }

    @Override
    protected void execute(TaskContext taskContext, File workDirectory) {
        final File sourceDirectory = new File(taskContext.getHybrisDirectory(), source);
        final File targetDirectory = new File(workDirectory, target);

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
