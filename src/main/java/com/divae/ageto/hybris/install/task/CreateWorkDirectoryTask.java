package com.divae.ageto.hybris.install.task;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.Files;

/**
 * @author Klaus Hauschild
 */
public class CreateWorkDirectoryTask implements InstallTask {

    public static final String  WORK_DIRECTORY = "workDirectory";

    private static final Logger LOGGER         = LoggerFactory.getLogger(CreateWorkDirectoryTask.class);

    @Override
    public void execute(final TaskContext taskContext) {
        final File workDirectory = Files.createTempDir();
        LOGGER.info(String.format("Work directory: %s", workDirectory));
        taskContext.setParameter(WORK_DIRECTORY, workDirectory);
    }

}
