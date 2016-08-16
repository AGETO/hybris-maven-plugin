package com.divae.ageto.hybris.install.task.metadata;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import com.divae.ageto.hybris.install.extensions.Extension;
import com.divae.ageto.hybris.install.extensions.binary.ClassFolder;
import com.divae.ageto.hybris.install.extensions.binary.ExtensionBinary;
import com.divae.ageto.hybris.install.extensions.binary.JARArchive;
import com.divae.ageto.hybris.install.extensions.binary.None;
import com.google.common.base.Throwables;

/**
 * @author Marvin Haagen
 */
public enum ExtensionMetadataFile {
    ;

    public static File createMetadataFile(final Extension extension, final File workDirectory) {
        File metadataFolder = MetadataFile.getFilePath(extension.getName());
        final File metadataFile = new File(new File(workDirectory, metadataFolder.toString()),
                MetadataFile.getFileName(extension.getName()).toString());
        final Properties properties = new Properties();
        try {
            properties.setProperty("extension.name", extension.getName());
            properties.setProperty("extension.directory", extension.getBaseDirectory().toString());
            addExtensionBinaryProperties(properties, extension);
            properties.store(new FileOutputStream(metadataFile), null);
            return metadataFile;
        } catch (IOException exception) {
            throw Throwables.propagate(exception);
        }
    }

    public static Extension readMetadataFile(final File workDirectory, final String extensionName) {
        String metadataFolder = MetadataFile.getFilePath(extensionName).toString();
        final File metadataFile = new File(new File(workDirectory, metadataFolder),
                MetadataFile.getFileName(extensionName).toString());
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(metadataFile));
            final String name = properties.getProperty("extension.name");
            final File baseFile = new File(properties.getProperty("extension.directory"));
            final ExtensionBinary binary = getExtensionBinary(properties);
            return new Extension(baseFile, name, binary);
        } catch (final IOException exception) {
            throw Throwables.propagate(exception);
        }
    }

    private static ExtensionBinary getExtensionBinary(final Properties properties) {
        final String type = properties.getProperty("extension.binary.type");
        if (type.equals(new None().getType())) {
            return new None();
        }
        if (type.equals(new JARArchive(new File("")).getType())) {
            return new JARArchive(new File(properties.getProperty("extension.binary.path")));
        }
        if (type.equals(new ClassFolder(new File("")).getType())) {
            return new ClassFolder(new File(properties.getProperty("extension.binary.path")));
        }
        throw new RuntimeException("Invalid type: " + type);
    }

    private static void addExtensionBinaryProperties(final Properties config, final Extension extension) {
        config.setProperty("extension.binary.type", extension.getBinary().getType());
        if (extension.getBinary().getClass() != None.class) {
            config.setProperty("extension.binary.path", extension.getBinary().getExtensionBinaryPath().toString());
        }
    }

}
