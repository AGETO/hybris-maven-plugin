package com.divae.ageto.hybris.install.task.copy;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.divae.ageto.hybris.install.task.AbstractWorkDirectoryTask;
import com.divae.ageto.hybris.install.task.TaskContext;

/**
 * @author Klaus Hauschild
 */
abstract class CopyFilesTask extends AbstractWorkDirectoryTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(CopyFilesTask.class);

    private final File          source;
    private final File          target;

    CopyFilesTask(final File source, final File target) {
        this.source = source;
        this.target = target;
    }

    @Override
    protected void execute(final TaskContext taskContext, final File workDirectory) {
        final File sourceDirectory = source;
        final File targetDirectory = getTargetDirectory(workDirectory, target);
        LOGGER.trace(String.format("Copy '%s' to '%s'.", sourceDirectory, targetDirectory));
        try {
            doCopy(sourceDirectory, targetDirectory);
        } catch (final IOException exception) {
            throw new RuntimeException(String.format("Unable to copy files from [%s] to [%s]", sourceDirectory, targetDirectory),
                    exception);
        }
    }

    protected abstract void doCopy(final File source, final File target) throws IOException;

    private File getTargetDirectory(final File workDirectory, final File target) {
        if (target == null) {
            return workDirectory;
        }
        return new File(workDirectory, target.toString());
    }

    @Override
    public String toString() {
        return String.format("%s -> %s", source, target);
    }

}
