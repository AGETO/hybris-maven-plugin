package com.divae.ageto.hybris.install.task;

import com.divae.ageto.hybris.install.InstallHybrisArtifacts;
import com.divae.ageto.hybris.install.InstallStrategy;
import com.divae.ageto.hybris.version.HybrisVersion;
import org.testng.annotations.Test;

import java.io.File;

import static org.testng.Assert.*;

/**
 * Created by mhaagen on 05.08.2016.
 */
public class CopyFilesTaskTest {

    @Test
    public void testExecute() throws Exception {

        TaskContext taskContext = new InstallHybrisArtifacts(new File("C:\\Users\\mhaagen\\hybris\\")).getTaskContext();
        new CopyFilesTask(String.format("bin/platform/extensions.xml"), "").execute(taskContext,
                new File("C:\\Users\\mhaagen\\AppData\\Local\\Temp\\1470386718572-0"));
    }

}
