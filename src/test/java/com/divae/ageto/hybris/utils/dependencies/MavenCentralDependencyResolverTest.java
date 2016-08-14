package com.divae.ageto.hybris.utils.dependencies;

import static org.testng.Assert.assertEquals;

import java.io.File;

import org.apache.maven.model.Dependency;
import org.testng.annotations.Test;

/**
 * @author Klaus Hauschid
 */
public class MavenCentralDependencyResolverTest {

    @Test
    public void resolveDependencyTest() {
        final DependencyResolver dependencyResolver = new MavenCentralDependencyResolver();
        final Dependency dependency = dependencyResolver.resolve(new File("commons-configuration-1.7.jar"));
        assertEquals(dependency.getGroupId(), "commons-configuration");
        assertEquals(dependency.getArtifactId(), "commons-configuration");
        assertEquals(dependency.getVersion(), "1.7");
    }

}
