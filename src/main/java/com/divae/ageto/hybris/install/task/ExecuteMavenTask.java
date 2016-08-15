package com.divae.ageto.hybris.install.task;

import java.io.File;

import com.divae.ageto.hybris.utils.maven.EmbeddedMavenExecutor;
import com.divae.ageto.hybris.utils.maven.MavenExecutor;

/**
 * @author Klaus Hauschild
 */
public class ExecuteMavenTask extends AbstractWorkDirectoryTask {

    private final MavenExecutor mavenExecutor = new EmbeddedMavenExecutor();
    private final File          directory;
    private final String[]      arguments;

    public ExecuteMavenTask(final File directory, final String[] arguments) {
        this.directory = directory;
        this.arguments = arguments;
    }

    @Override
    protected void execute(final TaskContext taskContext, final File workDirectory) {
        mavenExecutor.execute(arguments, getDirectory(workDirectory));
    }

    private File getDirectory(final File workDirectory) {
        return new File(workDirectory, directory.toString()).getAbsoluteFile();
    }

}
