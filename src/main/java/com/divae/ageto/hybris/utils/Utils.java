package com.divae.ageto.hybris.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.divae.ageto.hybris.install.extensions.Extension;
import com.divae.ageto.hybris.install.task.metadata.ExtensionMetadataFile;
import com.divae.ageto.hybris.install.task.metadata.MetadataFile;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;

/**
 * @author Klaus Hauschild
 */
public enum Utils {

    ;

    private static Logger LOGGER = LoggerFactory.getLogger(Utils.class);

    public static File getHybrisPlatformDirectory(final File hybrisDirectory) {
        final File hybrisPlatformDirectory = new File(new File(hybrisDirectory, "bin"), "platform");
        if (!hybrisPlatformDirectory.exists()) {
            throw new IllegalStateException("Unrecognizable hybris suite folder structure in " + hybrisPlatformDirectory + ".");
        }
        return hybrisPlatformDirectory;
    }

    public static List<Extension> readExtensionsFromReactorModules(final File hybrisReactorDir) {
        LOGGER.debug("Searching extensions in reactor directory...");
        final List<Extension> extensions = Lists.newArrayList();

        final File[] files = hybrisReactorDir.listFiles((File file) -> file.isDirectory() && !file.getName().equals("target"));

        for (File file : files) {
            LOGGER.debug(String.format("Extension %s found", file.getName()));
            if (new File(file, String.format("src/main/resources/%s", MetadataFile.getFileName(file.getName()))).exists()) {
                final Extension extension = ExtensionMetadataFile.readMetadataFile(hybrisReactorDir, file.getName());
                extension.setOriginalLocation(file);
                extensions.add(extension);
            }
        }

        return extensions;
    }

    public static void createSymLink(File fileName, File linkTarget) {
        try {
            if (!fileName.exists()) {
                FileUtils.makeDirectory(fileName.getParentFile());
                Files.createSymbolicLink(fileName.toPath(), linkTarget.toPath());
            }
        } catch (IOException e) {
            Throwables.propagate(e);
        }
    }
}
