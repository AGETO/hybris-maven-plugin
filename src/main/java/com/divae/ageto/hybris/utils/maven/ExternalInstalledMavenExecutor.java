package com.divae.ageto.hybris.utils.maven;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeroturnaround.exec.ProcessExecutor;
import org.zeroturnaround.exec.stream.slf4j.Slf4jStream;

import com.google.common.collect.Lists;

/**
 * @author Klaus Hauschild
 */
public class ExternalInstalledMavenExecutor implements MavenExecutor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExternalInstalledMavenExecutor.class);

    static {
        try {
            new ExternalInstalledMavenExecutor().execute(new String[] { "--version" }, new File("."));
        } catch (final MavenExecutionException exception) {
            throw new MavenExecutionException("Test invocation of maven failed. Probably it is not installed.", exception);
        }
    }

    @Override
    public void execute(final String[] arguments, final File directory) {
        LOGGER.info(String.format("Execute maven at '%s': %s", directory, Arrays.toString(arguments)));
        try {
            final List<String> argumentsAsList = Lists.newArrayList(Arrays.asList(arguments));
            argumentsAsList.add(0, getMavenCommand());
            new ProcessExecutor() //
                    .command(argumentsAsList) //
                    .directory(directory) //
                    .redirectOutput(Slf4jStream.of(LOGGER).asDebug()) //
                    .redirectError(Slf4jStream.of(LOGGER).asError()) //
                    .execute();
        } catch (final Exception exception) {
            throw new MavenExecutionException(exception);
        }
    }

    private String getMavenCommand() {
        if (SystemUtils.IS_OS_WINDOWS) {
            return "mvn.cmd";
        }
        return "mvn";
    }

}
