package com.divae.ageto.hybris.install.task;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.maven.cli.MavenCli;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Throwables;

/**
 * @author Klaus Hauschild
 */
public class ExecuteMavenTask extends AbstractWorkDirectoryTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExecuteMavenTask.class);

    private final String        directory;
    private final String[]      arguments;
    private final boolean       workDirectoryRelative;

    public ExecuteMavenTask(final String directory, final String[] arguments, final boolean workDirectoryRelative) {
        this.directory = directory;
        this.arguments = arguments;
        this.workDirectoryRelative = workDirectoryRelative;
    }

    @Override
    protected void execute(final TaskContext taskContext, final File workDirectory) {
        LOGGER.info(String.format("Execute maven at '%s': %s", workDirectory, Arrays.toString(arguments)));
        System.setProperty("maven.multiModuleProjectDirectory", workDirectory.getAbsolutePath());
        final MavenCli mavenCli = new MavenCli();
        final ByteArrayOutputStream stdOutStream = new ByteArrayOutputStream();
        final ByteArrayOutputStream stdErrStream = new ByteArrayOutputStream();
        final int exitCode = mavenCli.doMain(arguments, getDirectory(workDirectory), printStream(stdOutStream),
                printStream(stdErrStream));
        final List<String> executionOutput = toString(stdOutStream);
        if (!executionOutput.isEmpty()) {
            for (final String line : executionOutput) {
                LOGGER.debug(line);
            }
        }
        if (exitCode != 0) {
            throw new RuntimeException("Maven execution failed.");
        }
    }

    private List<String> toString(final ByteArrayOutputStream byteArrayOutputStream) {
        final String result = new String(byteArrayOutputStream.toByteArray(), StandardCharsets.UTF_8);
        if (result.isEmpty()) {
            return Collections.emptyList();
        }
        return Arrays.asList(result.split(System.lineSeparator()));
    }

    private PrintStream printStream(final OutputStream outputStream) {
        try {
            return new PrintStream(outputStream, true, StandardCharsets.UTF_8.name());
        } catch (final UnsupportedEncodingException exception) {
            throw Throwables.propagate(exception);
        }
    }

    private String getDirectory(final File workDirectory) {
        if (workDirectoryRelative) {
            return new File(workDirectory, directory).getAbsolutePath();
        }
        return directory;
    }

}
