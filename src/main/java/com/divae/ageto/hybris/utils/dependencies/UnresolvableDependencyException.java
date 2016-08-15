package com.divae.ageto.hybris.utils.dependencies;

import java.io.File;

/**
 * @author Klaus Hauschild
 */
class UnresolvableDependencyException extends RuntimeException {

    UnresolvableDependencyException(final File library) {
        super(String.format("Unable to resolve library %s", library));
    }

}
