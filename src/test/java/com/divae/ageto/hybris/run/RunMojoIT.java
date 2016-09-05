package com.divae.ageto.hybris.run;

import static org.testng.Assert.fail;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

/**
 * Created by mhaagen on 05.09.2016.
 */
public class RunMojoIT extends RunMojo {

    private Logger LOGGER = LoggerFactory.getLogger(RunMojoIT.class);

    @Test
    public void execute() {
        try {
            LOGGER.info("Please input location of hybris reactor dir:");

            final RunMojo runMojo = new RunMojo();
            runMojo.setWorkDirectory(new File("C:\\Users\\mhaagen\\AppData\\Local\\Temp\\1473063679874-0\\"));
            runMojo.execute();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

}
