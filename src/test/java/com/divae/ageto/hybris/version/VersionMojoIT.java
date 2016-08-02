package com.divae.ageto.hybris.version;

import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * @author Klaus Hauschild
 */
public class VersionMojoIT extends AbstractMojoTestCase {

    @BeforeTest
    protected void before() throws Exception {
        setUp();
    }

    @AfterTest
    protected void after() throws Exception {
        tearDown();
    }

    @Test
    public void executeTest() throws Exception {
        final VersionMojo versionMojo = (VersionMojo) lookupMojo("version",
                "src/test/resources/com/divae/ageto/hybris/version/version-test.pom.xml");
        versionMojo.execute();
    }

}
