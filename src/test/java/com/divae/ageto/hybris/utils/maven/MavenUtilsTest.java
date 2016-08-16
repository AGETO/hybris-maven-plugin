package com.divae.ageto.hybris.utils.maven;

import static org.testng.Assert.assertEquals;

import org.apache.maven.model.Dependency;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * @author Klaus Hauschild
 */
public class MavenUtilsTest {

    @Test(dataProvider = "isDependencyResolvableTestDataProvider")
    public void isDependencyResolvableTest(final Dependency dependency, final boolean resolvableExpected) {
        final boolean resolvable = MavenUtils.isDependencyResolvable(dependency);
        assertEquals(resolvable, resolvableExpected);
    }

    @DataProvider
    public Object[][] isDependencyResolvableTestDataProvider() {
        return new Object[][] { //
                { newDependency("commons-configuration", "commons-configuration", "1.10"), true }, //
                { newDependency("foo", "bar", "unknown"), false }, //
        };
    }

    private Dependency newDependency(final String groupId, final String artifactId, final String version) {
        final Dependency dependency = new Dependency();
        dependency.setGroupId(groupId);
        dependency.setArtifactId(artifactId);
        dependency.setVersion(version);
        return dependency;
    }

}
