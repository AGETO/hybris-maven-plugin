package com.divae.ageto.hybris.install;

import java.io.File;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import com.divae.ageto.hybris.AbstractHybrisDirectoryMojo;

/**
 * @author Klaus Hauschild
 */
@Mojo(name = "install", requiresProject = false)
class InstallMojo extends AbstractHybrisDirectoryMojo {

    @Parameter(property = "hybris.skipTests", defaultValue = "false")
    private boolean skipTests;

    @Parameter(property = "hybris.workDirectory")
    private String workDirectory;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        final InstallHybrisArtifacts installHybrisArtifacts = new InstallHybrisArtifacts(getHybrisDirectory(),
                getWorkDirectory(), skipTests);
        try {
            installHybrisArtifacts.execute();
        } catch (final Exception exception) {
            throw new MojoExecutionException("Error while hybris installation!", exception);
        }
    }

    private File getWorkDirectory() throws MojoExecutionException {
        if (workDirectory == null) {
            return null;
        }
        return new File(workDirectory);
    }

}
