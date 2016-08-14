package com.divae.ageto.hybris.utils.dependencies;

import java.io.File;

import org.apache.maven.model.Dependency;

/**
 * @author Klaus Hauschild
 */
public class DependencyResolverDelegator implements DependencyResolver {

    private DependencyResolver mavenCentralDependencyResolver = new MavenCentralDependencyResolver();
    private DependencyResolver installLocallyDependencyResolver = new InstallLocallyDependencyResolver();

    @Override
    public Dependency resolve(final File library) {
        try {
            // resolve dependency from maven central
            return mavenCentralDependencyResolver.resolve(library);
        } catch (final UnresolvableDependencyException exception) {
            // dependency could not resolved over maven central -> install locally
            return installLocallyDependencyResolver.resolve(library);
        }
    }

}
