package com.divae.ageto.hybris.install.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * @author Klaus Hauschild
 */
public class CleanupTask extends AbstractWorkDirectoryTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(CleanupTask.class);

    @Override
    protected void execute(TaskContext taskContext, File workDirectory) {
        LOGGER.info(String.format("Cleanup work directory: %s", workDirectory));
        delete(workDirectory);
    }

    private void delete(final File file) {
        if (file.isDirectory()) {
            for (final File child : file.listFiles()) {
                delete(child);
            }
        }
        file.delete();
    }

}
