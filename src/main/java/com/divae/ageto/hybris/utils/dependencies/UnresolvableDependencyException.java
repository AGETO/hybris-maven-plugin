package com.divae.ageto.hybris.utils.dependencies;

/**
 * @author Klaus Hauschild
 */
public class UnresolvableDependencyException extends RuntimeException {

    public UnresolvableDependencyException(final String artifactId, final String version) {
        super(String.format("Unable to resolve library %s-%s", artifactId, version));
    }

}
