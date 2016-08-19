package com.divae.ageto.hybris;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

import com.divae.ageto.hybris.utils.FileUtils;
import com.google.common.io.Files;

/**
 * @author Klaus Hauschild
 */
public abstract class AbstractTempDirectoryTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractTempDirectoryTests.class);

    private File                tempDirectory;

    protected File getTempDirectory() {
        return tempDirectory;
    }

    @BeforeTest
    public void prepareTempDirectory() {
        tempDirectory = Files.createTempDir();
        LOGGER.debug(String.format("temp directory test working in: %s", tempDirectory));
    }

    @AfterTest
    public void deleteTempDirectory() {
        if (tempDirectory != null) {
            FileUtils.delete(tempDirectory);
        }
    }

}
