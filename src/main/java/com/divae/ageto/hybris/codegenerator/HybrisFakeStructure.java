package com.divae.ageto.hybris.codegenerator;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.divae.ageto.hybris.install.extensions.Extension;
import com.divae.ageto.hybris.install.extensions.ExtensionFactory;
import com.divae.ageto.hybris.install.task.metadata.ExtensionMetadataFile;
import com.google.common.base.Throwables;

/**
 * @author Marvin Haagen
 */
class HybrisFakeStructure {

    private static Logger LOGGER = LoggerFactory.getLogger(HybrisFakeStructure.class);

    static File generate(final File hybrisReactorDir) {
        Arrays.asList(1, 2, 3).stream().map(i -> i + 1);

        try {
            final File hybrisFakeDirectory = new File(hybrisReactorDir, "target/hybris-fake/hybris");
            final File binDirectory = new File(hybrisFakeDirectory, "bin");
            final File platformDirectory = new File(binDirectory, "platform");
            final File resourcesDirectory = new File(hybrisReactorDir, "src/main/resources");

            // TODO use restructured platform.extensions.xml
            final File platformExtensionsXML = new File(
                    "src/main/resources/com/divae/ageto/hybris/install/platform.extensions.xml");

            com.divae.ageto.hybris.utils.FileUtils.makeDirectory(platformDirectory);

            copyFile(platformExtensionsXML, new File(platformDirectory, "extensions.xml"));
            copyFile(new File(resourcesDirectory, "advanced.properties"),
                    new File(platformDirectory, "resources/advanced.properties"));
            copyFile(new File(resourcesDirectory, "project.properties"), new File(platformDirectory, "project.properties"));
            copyFile(new File(resourcesDirectory, "schemas/beans.xsd"),
                    new File(platformDirectory, "resources/schemas/beans.xsd"));
            copyFile(new File(resourcesDirectory, "bootstrap/pojo/global-eventtemplate.vm"),
                    new File(platformDirectory, "bootstrap/resources/pojo/global-eventtemplate.vm"));

            List<Extension> extensions = ExtensionFactory.getExtensions(hybrisFakeDirectory, hybrisReactorDir,
                    Collections.singletonList(new File("target")));

            for (Extension extension : extensions) {
                Extension extensionProperties = ExtensionMetadataFile.readMetadataFile(hybrisReactorDir,
                        extension.getName());

                final File extensionDirectory = new File(hybrisFakeDirectory,
                        extensionProperties.getBaseDirectory().toString());

                com.divae.ageto.hybris.utils.FileUtils.makeDirectory(extensionDirectory);

                copyFile(
                        new File(hybrisReactorDir,
                                String.format("%s/%s-advanced-deployment.xml", extension.getBaseDirectory(),
                                        extension.getName())),
                        new File(extensionDirectory, String.format("resources/%s-advanced-deployment.xml", extension.getName())));

                copyFile(
                        new File(hybrisReactorDir,
                                String.format("%s/%s-beans.xml", extension.getBaseDirectory(), extension.getName())),
                        new File(extensionDirectory, String.format("resources/%s-beans.xml", extension.getName())));

                copyFile(
                        new File(hybrisReactorDir,
                                String.format("%s/%s-items.xml", extension.getBaseDirectory(), extension.getName())),
                        new File(extensionDirectory, String.format("resources/%s-items.xml", extension.getName())));

                copyFile(new File(hybrisReactorDir, String.format("%s/project.properties", extension.getBaseDirectory())),
                        new File(extensionDirectory, "project.properties"));

                copyFile(new File(hybrisReactorDir, String.format("%s/extensioninfo.xml", extension.getBaseDirectory())),
                        new File(extensionDirectory, "extensioninfo.xml"));

                FileUtils.copyDirectory(new File(hybrisReactorDir, String.format("%s/src/test/java", extension.getName())),
                        new File(extensionDirectory, "testsrc"));
            }

            return platformDirectory;
        } catch (final Exception exception) {
            throw Throwables.propagate(exception);
        }
    }

    private static void copyFile(final File srcFile, final File destFile) throws IOException {
        if (!srcFile.exists()) {
            return;
        }

        com.divae.ageto.hybris.utils.FileUtils.makeDirectory(destFile.getParentFile());

        FileUtils.copyFile(srcFile, destFile);
    }

}
