package com.divae.ageto.hybris.install.task;

import java.io.File;

import org.testng.annotations.Test;

/**
 * @author Marvin Haagen
 */
public class CopyFilesTaskTest {

    @Test
    public void testExecute() {
        final TaskContext taskContext = new TaskContext(null, new File("src/test/resources"));
        new CopyFilesTask("com/divae/ageto/hybris/version/5.5.1.1-build.number", null).execute(taskContext, new File("target"));
    }

}
