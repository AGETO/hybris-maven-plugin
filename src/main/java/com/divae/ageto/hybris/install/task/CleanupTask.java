package com.divae.ageto.hybris.install.task;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Klaus Hauschild
 */
class CleanupTask extends AbstractWorkDirectoryTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(CleanupTask.class);

    @Override
    protected void execute(TaskContext taskContext, File workDirectory) {
        LOGGER.info(String.format("Cleanup work directory: %s", workDirectory));
        delete(workDirectory);
    }

    private void delete(final File file) {
        if (file.isDirectory() && file.listFiles() != null) {
            for (final File child : file.listFiles()) {
                delete(child);
            }
        }
        file.delete();
    }

}
