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
 * Created by mhaagen on 12.08.2016.
 */
public enum ExtensionMetadataFile {
    ;

    private static String fileNameFormat = "%s-metadata.properties";

    public static File createMetadataFile(final Extension extension, final File workDirectory) {
        File metadataFile = new File(new File(workDirectory, extension.getName()),
                getMetadataFileName(extension.getName()).toString());

        Properties properties = new Properties();

        try {
            properties.setProperty("extension.name", extension.getName());
            properties.setProperty("extension.directory", extension.getBaseDirectory().toString());
            addExtensionBinaryProperties(properties, extension);
            properties.store(new FileOutputStream(metadataFile), null);
        } catch (IOException e) {
            Throwables.propagate(e);
        }

        return metadataFile;
    }

    public static ExtensionProperties readMetadataFile(final File workDirectory, final String extensionName) {
        File metadataFile = new File(new File(workDirectory, extensionName), getMetadataFileName(extensionName).toString());

        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(metadataFile));
            String name = properties.getProperty("extension.name");
            File baseFile = new File(properties.getProperty("extension.directory"));
            ExtensionBinary binary = getExtensionBinary(properties);
            return new ExtensionProperties(baseFile);
        } catch (IOException e) {
            Throwables.propagate(e);
        }
        return null;
    }

    private static ExtensionBinary getExtensionBinary(Properties properties) {
        String type = properties.getProperty("extension.binary.type");
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

    private static void addExtensionBinaryProperties(Properties config, Extension extension) {
        config.setProperty("extension.binary.type", extension.getBinary().getType());
        if (extension.getBinary().getClass() != None.class) {
            config.setProperty("extension.binary.path", extension.getBinary().getExtensionBinaryPath().toString());
        }
    }

    private static File getMetadataFileName(final String extensionName) {
        return new File(String.format(fileNameFormat, extensionName));
    }
}
