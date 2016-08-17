package com.divae.ageto.hybris;

import java.io.File;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

import com.divae.ageto.hybris.utils.FileUtils;
import com.google.common.io.Files;

/**
 * @author Klaus Hauschild
 */
public abstract class AbstractTempDirectoryTests {

    private File tempDirectory;

    protected File getTempDirectory() {
        return tempDirectory;
    }

    @BeforeTest
    private void before() {
        tempDirectory = Files.createTempDir();
    }

    @AfterTest
    private void after() {
        if (tempDirectory != null) {
            FileUtils.delete(tempDirectory);
        }
    }

}
