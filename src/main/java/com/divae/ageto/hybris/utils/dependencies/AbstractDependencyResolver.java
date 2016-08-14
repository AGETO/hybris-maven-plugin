package com.divae.ageto.hybris.utils.dependencies;

import java.io.File;

import org.apache.commons.io.FilenameUtils;
import org.apache.maven.model.Dependency;

/**
 * @author Klaus Hauschild
 */
abstract class AbstractDependencyResolver implements DependencyResolver {

    @Override
    public Dependency resolve(final File library) {
        final Dependency partialDependency = getPartialDependency(library);
        return resolve(partialDependency.getArtifactId(), partialDependency.getVersion());
    }

    protected abstract Dependency resolve(final String artifactId, final String version);

    private Dependency getPartialDependency(final File library) {
        final String rawLibraryName = getRawLibraryName(library);
        final int pivot = rawLibraryName.lastIndexOf("-");
        final String artifactId = rawLibraryName.substring(0, pivot);
        final String version = rawLibraryName.substring(pivot + 1, rawLibraryName.length());
        final Dependency dependency = new Dependency();
        dependency.setArtifactId(artifactId);
        dependency.setVersion(version);
        return dependency;
    }

    private String getRawLibraryName(final File library) {
        final String libraryName = FilenameUtils.getName(library.getPath());
        if (libraryName.endsWith(".jar")) {
            return libraryName.substring(0, libraryName.length() - 4);
        }
        return libraryName;
    }

}
