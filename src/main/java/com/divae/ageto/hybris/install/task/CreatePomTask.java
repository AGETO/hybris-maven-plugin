package com.divae.ageto.hybris.install.task;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * @author Klaus Hauschild
 */
public class CreatePomTask extends AbstractWorkDirectoryTask {

    private final String              template;
    private final String              targetDirectory;
    private final Map<String, String> parameters;

    public CreatePomTask(final String template, final String targetDirectory, final Map<String, String> parameters) {
        this.template = template;
        this.targetDirectory = targetDirectory;
        this.parameters = parameters;
    }

    @Override
    protected void execute(final TaskContext taskContext, final File workDirectory) {
        Map<String, String> token = Maps.newHashMap();
        token.putAll(parameters);
        token.put("hybris.version", taskContext.getHybrisVersion().getVersion());
        try (final BufferedReader reader = new BufferedReader(
                new TokenReplacingReader(new InputStreamReader(ClassLoader.getSystemResourceAsStream(template)), token))) {

            final List<String> lines = Lists.newArrayList();
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }

            final File directory = new File(workDirectory, targetDirectory);
            directory.mkdirs();
            final File targetFile = new File(directory, "pom.xml");
            targetFile.createNewFile();
            try (final BufferedWriter writer = new BufferedWriter(new FileWriter(targetFile))) {
                for (final String lineToWrite : lines) {
                    try {
                        writer.write(lineToWrite);
                        writer.newLine();
                    } catch (final Exception exception) {
                        throw new RuntimeException(exception);
                    }
                }
            }

        } catch (final Exception exception) {
            throw new RuntimeException(exception);
        }
    }
}
