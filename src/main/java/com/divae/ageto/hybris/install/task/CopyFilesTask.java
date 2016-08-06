package com.divae.ageto.hybris.install.task;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Klaus Hauschild
 */
public class CopyFilesTask extends AbstractWorkDirectoryTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(CopyFilesTask.class);

    private final String        source;
    private final String        target;

    public CopyFilesTask(final String source, final String target) {
        this.source = source;
        this.target = target;
    }

    @Override
    protected void execute(final TaskContext taskContext, final File workDirectory) {
        final File sourceDirectory = new File(taskContext.getHybrisDirectory(), source);
        final File targetDirectory = getTargetDirectory(workDirectory, target);
        LOGGER.trace(String.format("Copy '%s' to '%s'.", sourceDirectory, targetDirectory));
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

    @Override
    public String toString() {
        return String.format("%s -> %s", source, target);
    }

}
