package com.divae.ageto.hybris.utils.dependencies;

import java.io.File;

import org.apache.maven.model.Dependency;

/**
 * @author Klaus Hauschild
 */
class InstallLocallyDependencyResolver implements DependencyResolver {

    @Override
    public Dependency resolve(final File library) {
        // TODO implement me
        return new Dependency();
    }

}
