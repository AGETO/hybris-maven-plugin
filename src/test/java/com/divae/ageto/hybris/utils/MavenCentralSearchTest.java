package com.divae.ageto.hybris.utils;

import static org.testng.Assert.assertEquals;

import org.apache.maven.model.Dependency;
import org.testng.annotations.Test;

/**
 * @author Klaus Hauschild
 */
public class MavenCentralSearchTest {

    @Test
    public void determineDependencyTest() {
        final Dependency dependency = MavenCentralSearch.determineDependencyForLibrary("commons-configuration-1.7.jar");
        assertEquals(dependency.getGroupId(), "commons-configuration");
        assertEquals(dependency.getArtifactId(), "commons-configuration");
        assertEquals(dependency.getVersion(), "1.7");
    }

}
