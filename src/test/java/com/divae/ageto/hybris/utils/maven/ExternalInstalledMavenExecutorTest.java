package com.divae.ageto.hybris.utils.maven;

import java.io.File;

import org.testng.annotations.Test;

/**
 * @author Klaus Hauschild
 */
public class ExternalInstalledMavenExecutorTest {

    @Test
    public void mavenInvocationTest() {
        new ExternalInstalledMavenExecutor().execute(new String[] { "--help" }, new File("."));
    }

}
