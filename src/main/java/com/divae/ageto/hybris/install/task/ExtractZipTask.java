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

    private final File          sourceFile;
    private final File          destinationDirectory;

    ExtractZipTask(final File sourceFile, final File destinationDirectory) {
        this.sourceFile = sourceFile;
        this.destinationDirectory = destinationDirectory;
    }

    @Override
    protected void execute(final TaskContext taskContext, final File workDirectory) {
        LOGGER.trace(String.format("Extracting '%s'...", sourceFile));

        final byte[] buffer = new byte[1024];

        try {
            final File destinationDirectory = new File(workDirectory, this.destinationDirectory.toString());
            if (!destinationDirectory.exists() && !destinationDirectory.mkdirs()) {
                LOGGER.error(String.format("Directory %s can not be created", destinationDirectory));
                throw new RuntimeException(String.format("Directory %s can not be created", destinationDirectory));
            }

            final ZipInputStream zipInputStream = new ZipInputStream(
                    new FileInputStream(new File(taskContext.getHybrisDirectory(), sourceFile.toString())));
            ZipEntry zipEntry = zipInputStream.getNextEntry();

            while (zipEntry != null) {
                final String fileName = zipEntry.getName();
                final File file = new File(destinationDirectory, fileName);

                LOGGER.trace(String.format("  extracting '%s'", file));

                if (!file.getParentFile().mkdirs()) {
                    LOGGER.error(String.format("Directory %s can not be created", file.getParentFile()));
                    throw new RuntimeException(String.format("Directory %s can not be created", file.getParentFile()));
                }

                if (zipEntry.isDirectory()) {
                    if (!file.mkdirs()) {
                        LOGGER.error(String.format("Directory %s can not be created", file));
                        throw new RuntimeException(String.format("Directory %s can not be created", file));
                    }
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
