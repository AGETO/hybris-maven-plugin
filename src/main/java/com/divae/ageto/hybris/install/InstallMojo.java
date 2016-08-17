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

    @Parameter(property = "hybris.work-directory", defaultValue = "HYBRIS.WORK_DIRECTORY.EMPTY")
    private File workdirectory;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        final InstallHybrisArtifacts installHybrisArtifacts = getInstallHybrisArtifactsTask(getWorkdirectory());
        try {
            installHybrisArtifacts.execute();
        } catch (final Exception exception) {
            throw new MojoExecutionException("Error while hybris installation!", exception);
        }
    }

    private File getWorkdirectory() throws MojoExecutionException {
        if (workdirectory.toString().equals("HYBRIS.WORK_DIRECTORY.EMPTY")) {
            return null;
        }
        return workdirectory;
    }

    private InstallHybrisArtifacts getInstallHybrisArtifactsTask(final File workdirectory) {
        if (!workdirectory.toString().equals("")) {
            new InstallHybrisArtifacts(getHybrisDirectory(), workdirectory);
        }
        return new InstallHybrisArtifacts(getHybrisDirectory());
    }

}
