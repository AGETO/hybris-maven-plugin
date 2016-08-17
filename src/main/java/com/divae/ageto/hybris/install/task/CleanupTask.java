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
        // FileUtils.delete(workDirectory);
    }

}
