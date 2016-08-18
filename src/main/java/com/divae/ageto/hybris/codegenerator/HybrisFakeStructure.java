package com.divae.ageto.hybris.codegenerator;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.divae.ageto.hybris.install.extensions.Extension;
import com.divae.ageto.hybris.install.task.metadata.ExtensionMetadataFile;
import com.divae.ageto.hybris.install.task.metadata.MetadataFile;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;

/**
 * @author Marvin Haagen
 */
class HybrisFakeStructure {

    private static Logger LOGGER = LoggerFactory.getLogger(HybrisFakeStructure.class);

    static File generate(final File hybrisReactorDir) {
        try {
            LOGGER.info("Creating hybris fake structure");
            final File hybrisFakeDirectory = new File(hybrisReactorDir, "target/hybris-fake/hybris");
            final File binDirectory = new File(hybrisFakeDirectory, "bin");
            final File platformDirectory = new File(binDirectory, "platform");
            final File resourcesDirectory = new File(hybrisReactorDir, "src/main/resources");

            com.divae.ageto.hybris.utils.FileUtils.makeDirectory(platformDirectory);

            copyFile(new File(resourcesDirectory, "platform.extensions.xml"), new File(platformDirectory, "extensions.xml"));
            copyFile(new File(resourcesDirectory, "advanced.properties"),
                    new File(platformDirectory, "resources/advanced.properties"));
            copyFile(new File(resourcesDirectory, "project.properties"), new File(platformDirectory, "project.properties"));
            copyFile(new File(resourcesDirectory, "schemas/beans.xsd"),
                    new File(platformDirectory, "resources/schemas/beans.xsd"));
            copyFile(new File(resourcesDirectory, "bootstrap/pojo/global-eventtemplate.vm"),
                    new File(platformDirectory, "bootstrap/resources/pojo/global-eventtemplate.vm"));

            // TODO read extensions from reactor modules
            final List<Extension> extensions = readExtensionsFromReactorModules(hybrisReactorDir);

            for (Extension extension : extensions) {
                final File source = new File(String.format("%s/%s/src/main/resources", hybrisReactorDir, extension.getName()));
                LOGGER.info(source.toString());
                Extension extensionProperties = ExtensionMetadataFile.readMetadataFile(hybrisReactorDir, extension.getName());

                final File extensionDirectory = new File(hybrisFakeDirectory, extensionProperties.getBaseDirectory().toString());

                com.divae.ageto.hybris.utils.FileUtils.makeDirectory(extensionDirectory);

                copyFile(
                        new File(source, String.format("%s-advanced-deployment.xml", extension.getName())),
                        new File(extensionDirectory, String.format("resources/%s-advanced-deployment.xml", extension.getName())));

                copyFile(
                        new File(source, String.format("%s-beans.xml", extension.getName())),
                        new File(extensionDirectory, String.format("resources/%s-beans.xml", extension.getName())));

                copyFile(
                        new File(source, String.format("%s-items.xml", extension.getName())),
                        new File(extensionDirectory, String.format("resources/%s-items.xml", extension.getName())));

                copyFile(new File(source, "project.properties"),
                        new File(extensionDirectory, "project.properties"));

                copyFile(new File(source, "extensioninfo.xml"),
                        new File(extensionDirectory, "extensioninfo.xml"));
            }

            return platformDirectory;
        } catch (final Exception exception) {
            throw Throwables.propagate(exception);
        }
    }

    private static List<Extension> readExtensionsFromReactorModules(final File hybrisReactorDir) {
        LOGGER.debug("Searching extensions in reactor directory...");
        final List<Extension> extensions = Lists.newArrayList();

        final File[] files = hybrisReactorDir.listFiles((File file) -> file.isDirectory() && !file.getName().equals("target"));

        for (File file : files) {
            LOGGER.debug(String.format("Extension %s found", file.getName()));
            if (new File(file, String.format("src/main/resources/%s", MetadataFile.getFileName(file.getName()))).exists()) {
                extensions.add(ExtensionMetadataFile.readMetadataFile(hybrisReactorDir, file.getName()));
            }
        }

        return extensions;
    }

    private static void copyFile(final File srcFile, final File destFile) throws IOException {
        LOGGER.info(String.format("Copying file %s to %s", srcFile, destFile));
        if (!srcFile.exists()) {
            LOGGER.info("Source file not exists");
            return;
        }

        com.divae.ageto.hybris.utils.FileUtils.makeDirectory(destFile.getParentFile());

        FileUtils.copyFile(srcFile, destFile);
    }

}
