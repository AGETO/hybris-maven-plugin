package com.divae.ageto.hybris.install.task;

import com.divae.ageto.hybris.install.InstallHybrisArtifacts;
import org.testng.annotations.Test;

import java.io.File;

import static org.testng.Assert.*;

/**
 * Created by mhaagen on 05.08.2016.
 */
public class RestructurePlatformTaskTest {

    @Test
    public void testExecute() throws Exception {
        InstallHybrisArtifacts hybrisArtifacts = new InstallHybrisArtifacts(new File("C:\\Users\\mhaagen\\hybris\\"));
        hybrisArtifacts.execute();
        TaskContext taskContext = hybrisArtifacts.getTaskContext();
        new RestructurePlatformTask().execute(taskContext);
    }

}
