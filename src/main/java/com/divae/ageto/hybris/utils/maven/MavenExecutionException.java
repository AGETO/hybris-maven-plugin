package com.divae.ageto.hybris.utils.maven;

/**
 * @author Klaus Hauschild
 */
class MavenExecutionException extends RuntimeException {

    MavenExecutionException() {
        super();
    }

    MavenExecutionException(final Throwable cause) {
        super(cause);
    }

    MavenExecutionException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
