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
    private File    workdirectory;

    @Parameter(property = "hybris.decompiler", defaultValue = "false")
    private boolean decompile;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        final InstallHybrisArtifacts installHybrisArtifacts = new InstallHybrisArtifacts(getHybrisDirectory(), getWorkDirectory(),
                decompile);
        try {
            installHybrisArtifacts.execute();
        } catch (final Exception exception) {
            throw new MojoExecutionException("Error while hybris installation!", exception);
        }
    }

    private File getWorkDirectory() throws MojoExecutionException {
        if (workdirectory.toString().equals("HYBRIS.WORK_DIRECTORY.EMPTY")) {
            return null;
        }
        return workdirectory;
    }

}
