package com.divae.ageto.hybris.install.task;

import org.apache.maven.cli.MavenCli;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;

/**
 * @author Klaus Hauschild
 */
public class ExecuteMavenTask extends AbstractWorkDirectoryTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExecuteMavenTask.class);

    private final String        directory;
    private final String[]      args;
    private final boolean       workDirectoryRelative;

    public ExecuteMavenTask(final String directory, final String[] args, final boolean workDirectoryRelative) {
        this.directory = directory;
        this.args = args;
        this.workDirectoryRelative = workDirectoryRelative;
    }

    @Override
    protected void execute(final TaskContext taskContext, final File workDirectory) {
        System.setProperty("maven.multiModuleProjectDirectory", workDirectory.getAbsolutePath());
        final MavenCli mavenCli = new MavenCli();
        final ByteArrayOutputStream stream = new ByteArrayOutputStream();
        final PrintStream stdout = new PrintStream(stream);
        final int exitCode = mavenCli.doMain(args, getDirectory(workDirectory), stdout, stdout);
        if (exitCode != 0) {
            throw new RuntimeException(String.format("Maven execution failed: %s", stream.toString()));
        }
        LOGGER.debug(String.format("Execution result: %s", stream.toString()));
    }

    private String getDirectory(final File workDirectory) {
        if (workDirectoryRelative) {
            return new File(workDirectory, directory).getAbsolutePath();
        }
        return directory;
    }

}
