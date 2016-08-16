package com.divae.ageto.hybris.utils.maven;

import java.io.File;

import org.apache.maven.model.Dependency;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Klaus Hauschild
 */
public enum MavenExecutorUtils {

    ;

    private static final Logger LOGGER = LoggerFactory.getLogger(MavenExecutorUtils.class);

    public static void installLibrary(final File library, final Dependency mavenCoordinates) {
        LOGGER.debug(String.format("Installing library '%s' locally.", library));
        final String[] arguments = new String[] { //
                "install:install-file", //
                String.format("-Dfile=%s", library), //
                String.format("-DgroupId=%s", mavenCoordinates.getGroupId()), //
                String.format("-DartifactId=%s", mavenCoordinates.getArtifactId()), //
                String.format("-Dversion=%s", mavenCoordinates.getVersion()), //
                "-Dpackaging=jar" //
        };
        final MavenExecutor mavenExecutor = new ExternalInstalledMavenExecutor();
        mavenExecutor.execute(arguments, new File("."));
    }

}
