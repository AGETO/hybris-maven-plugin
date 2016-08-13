package com.divae.ageto.hybris.install.task;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Klaus Hauschild
 */
public class CleanupTask extends AbstractWorkDirectoryTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(CleanupTask.class);

    @Override
    protected void execute(TaskContext taskContext, File workDirectory) {
        LOGGER.info(String.format("Cleanup work directory: %s", workDirectory));

        // TODO activate this for production
        // delete(workDirectory);
    }

    private void delete(final File directory) {
        if (directory.isDirectory() && directory.listFiles() != null) {
            for (final File child : directory.listFiles()) {
                delete(child);
            }
        }
        if (directory.exists() && !directory.delete()) {
            throw new RuntimeException(String.format("Directory %s can not be deleted", directory));
        }
    }

}
