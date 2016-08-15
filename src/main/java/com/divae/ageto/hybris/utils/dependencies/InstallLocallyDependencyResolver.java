package com.divae.ageto.hybris.utils.dependencies;

import java.io.File;

import org.apache.maven.model.Dependency;

import com.divae.ageto.hybris.utils.maven.MavenExecutorUtils;

/**
 * @author Klaus Hauschild
 */
class InstallLocallyDependencyResolver extends AbstractDependencyResolver {

    @Override
    protected Dependency resolve(final File library, final String artifactId, final String version) {
        // TODO maybe there are maven meta data contained in archive and we can receive the maven coordinates from there
        final Dependency dependency = new Dependency();
        dependency.setGroupId(artifactId);
        dependency.setArtifactId(artifactId);
        dependency.setVersion(version);

        MavenExecutorUtils.installLibrary(library, dependency);

        return dependency;
    }

}
