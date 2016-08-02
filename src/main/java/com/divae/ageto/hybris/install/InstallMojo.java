package com.divae.ageto.hybris.install;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;

import com.divae.ageto.hybris.AbstractHybrisDirectoryMojo;

/**
 * @author Klaus Hauschild
 */
@Mojo(name = "install", requiresProject = false)
public class InstallMojo extends AbstractHybrisDirectoryMojo {

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        final InstallHybrisArtifacts installHybrisArtifacts = new InstallHybrisArtifacts(getHybrisDirectory());
        try {
            installHybrisArtifacts.execute();
        } catch (final Exception exception) {
            throw new MojoExecutionException("Error while hybris installation!", exception);
        }
    }

}
