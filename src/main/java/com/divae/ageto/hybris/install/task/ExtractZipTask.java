/*
 * Copyright (C) Klaus Hauschild - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Klaus Hauschild <klaus.hauschild.1984@gmail.com>, 2016
 */
package com.divae.ageto.hybris.install.task;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Klaus Hauschild
 */
class ExtractZipTask extends AbstractWorkDirectoryTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExtractZipTask.class);

    private final String        source;
    private final String        destination;

    ExtractZipTask(final String source, final String destination) {
        this.source = source;
        this.destination = destination;
    }

    @Override
    protected void execute(final TaskContext taskContext, final File workDirectory) {
        LOGGER.trace(String.format("Extracting '%s'...", source));

        final byte[] buffer = new byte[1024];

        try {
            final File destinationDirectory = new File(workDirectory, destination);
            if (!destinationDirectory.exists()) {
                destinationDirectory.mkdirs();
            }

            final ZipInputStream zipInputStream = new ZipInputStream(
                    new FileInputStream(new File(taskContext.getHybrisDirectory(), source)));
            ZipEntry zipEntry = zipInputStream.getNextEntry();

            while (zipEntry != null) {
                final String fileName = zipEntry.getName();
                final File file = new File(destinationDirectory, fileName);

                LOGGER.trace(String.format("  extracting '%s'", file));

                new File(file.getParent()).mkdirs();

                if (zipEntry.isDirectory()) {
                    file.mkdirs();
                    zipEntry = zipInputStream.getNextEntry();
                    continue;
                }

                final FileOutputStream fileOutputStream = new FileOutputStream(file);

                int length;
                while ((length = zipInputStream.read(buffer)) > 0) {
                    fileOutputStream.write(buffer, 0, length);
                }

                fileOutputStream.close();
                zipEntry = zipInputStream.getNextEntry();
            }

            zipInputStream.closeEntry();
            zipInputStream.close();
        } catch (final IOException exception) {
            throw new RuntimeException(exception);
        }
    }

}
