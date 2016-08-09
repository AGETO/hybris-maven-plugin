package com.divae.ageto.hybris.utils;

import com.divae.ageto.hybris.install.task.TokenReplacingReader;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

/**
 * Created by mhaagen on 09.08.2016.
 */
public class PomCreator {

    public static void create(final String hybrisVersion, final Map<String, String> parameters, final String template,
            final File workDirectory, final String targetDirectory) {
        final Map<String, String> token = Maps.newHashMap();
        token.putAll(parameters);
        token.put("hybris.version", hybrisVersion);
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
