package com.divae.ageto.hybris.install.task;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

/**
 * @author Klaus Hauschild
 */
public class CopyFilesTask extends AbstractWorkDirectoryTask {

    private final String source;
    private final String target;

    public CopyFilesTask(final String source, final String target) {
        this.source = source;
        this.target = target;
    }

    @Override
    protected void execute(final TaskContext taskContext, final File workDirectory) {
        final File sourceDirectory = new File(taskContext.getHybrisDirectory(), source);
        final File targetDirectory = getTargetDirectory(workDirectory, target);
        try {
            if (sourceDirectory.isFile()) {
                FileUtils.copyFileToDirectory(sourceDirectory, targetDirectory);
            }
            if (sourceDirectory.isDirectory()) {
                FileUtils.copyDirectory(sourceDirectory, targetDirectory);
            }
        } catch (final IOException exception) {
            throw new RuntimeException(String.format("Unable to copy files from [%s] to [%s]", sourceDirectory, targetDirectory),
                    exception);
        }
    }

    private File getTargetDirectory(final File workDirectory, final String target) {
        if (target == null) {
            return workDirectory;
        }
        return new File(workDirectory, target);
    }

}
