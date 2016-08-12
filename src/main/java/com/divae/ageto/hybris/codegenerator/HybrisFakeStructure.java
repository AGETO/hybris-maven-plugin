package com.divae.ageto.hybris.codegenerator;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.divae.ageto.hybris.install.extensions.Extension;
import com.divae.ageto.hybris.install.extensions.ExtensionFactory;
import com.divae.ageto.hybris.install.task.metadata.ExtensionMetadataFile;
import com.divae.ageto.hybris.install.task.metadata.ExtensionProperties;
import com.google.common.base.Throwables;

/**
 * @author Marvin Haagen
 */
class HybrisFakeStructure {

    static File generate(final File hybrisReactorDir) {
        Arrays.asList(1, 2, 3).stream().map(i -> i + 1);

        try {
            final File hybrisFakeDirectory = new File(hybrisReactorDir, "target/hybris-fake/hybris");
            final File binDirectory = new File(hybrisFakeDirectory, "bin");
            final File platformDirectory = new File(binDirectory, "platform");
            final File extensionsDirectory = hybrisReactorDir;

            // TODO use restructured platform.extensions.xml
            final File platformExtensionsXML = new File(
                    "src/main/resources/com/divae/ageto/hybris/install/platform.extensions.xml");

            platformDirectory.mkdirs();
            copyFile(platformExtensionsXML, new File(platformDirectory, "extensions.xml"));

            List<Extension> extensions = ExtensionFactory.getExtensions(hybrisFakeDirectory, extensionsDirectory,
                    Arrays.asList(new File("target")));

            for (Extension extension : extensions) {
                ExtensionProperties extensionProperties = ExtensionMetadataFile.readMetadataFile(hybrisReactorDir,
                        extension.getName());

                final File extensionDirectory = new File(hybrisFakeDirectory,
                        extensionProperties.getExtensionBaseDirectory().toString());

                extensionDirectory.mkdirs();

                copyFile(
                        new File(hybrisReactorDir,
                                String.format("%s/src/main/resources/%s-advanced-deployment.xml", extension.getName(),
                                        extension.getName())),
                        new File(extensionDirectory, String.format("resources/%s-advanced-deployment.xml", extension.getName())));
                copyFile(
                        new File(hybrisReactorDir,
                                String.format("%s/src/main/resources/%s-beans.xml", extension.getName(), extension.getName())),
                        new File(extensionDirectory, String.format("resources/%s-beans.xml", extension.getName())));
                copyFile(
                        new File(hybrisReactorDir,
                                String.format("%s/src/main/resources/%s-items.xml", extension.getName(), extension.getName())),
                        new File(extensionDirectory, String.format("resources/%s-items.xml", extension.getName())));
                copyFile(
                        new File(hybrisReactorDir,
                                String.format("%s/src/main/resources/project.properties", extension.getName())),
                        new File(extensionDirectory, "project.properties"));
            }

            return binDirectory;
        } catch (final Exception exception) {
            throw Throwables.propagate(exception);
        }
    }

    private static void copyFile(final File srcFile, final File destFile) throws IOException {
        if (!srcFile.exists()) {
            return;
        }
        FileUtils.copyFile(srcFile, destFile);
    }

}
