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

    public static void setWorkDirectory(final TaskContext taskContext, final File workDirectory) {
        taskContext.setParameter(WORK_DIRECTORY, workDirectory);
    }

    @Override
    public void execute(final TaskContext taskContext) {
        File workDirectory = AbstractWorkDirectoryTask.getWorkDirectory(taskContext);
        if (workDirectory != null) {
            workDirectory.mkdirs();
            return;
        }
        workDirectory = Files.createTempDir();
        LOGGER.info(String.format("Work directory: %s", workDirectory));
        setWorkDirectory(taskContext, workDirectory);
    }
}
