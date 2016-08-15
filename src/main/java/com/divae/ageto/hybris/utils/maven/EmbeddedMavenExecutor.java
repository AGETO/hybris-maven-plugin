package com.divae.ageto.hybris.utils.maven;

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
public class EmbeddedMavenExecutor implements MavenExecutor {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmbeddedMavenExecutor.class);

    @Override
    public void execute(final String[] arguments, final File directory) {
        LOGGER.info(String.format("Execute maven at '%s': %s", directory, Arrays.toString(arguments)));
        System.setProperty("maven.multiModuleProjectDirectory", directory.getAbsolutePath());
        final MavenCli mavenCli = new MavenCli();
        final ByteArrayOutputStream stdOutStream = new ByteArrayOutputStream();
        final ByteArrayOutputStream stdErrStream = new ByteArrayOutputStream();
        final int exitCode = mavenCli.doMain(arguments, directory.getAbsolutePath(), printStream(stdOutStream),
                printStream(stdErrStream));
        final List<String> executionOutput = toString(stdOutStream);
        if (LOGGER.isDebugEnabled() && !executionOutput.isEmpty()) {
            executionOutput.forEach(LOGGER::debug);
        }
        if (exitCode != 0) {
            throw new MavenExecutionException();
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

}
