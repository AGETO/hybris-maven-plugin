package com.divae.ageto.hybris.codegenerator;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;

import com.google.common.base.Throwables;

/**
 * @author Marvin Haagen
 */
class HybrisFakeStructure {

    static File generate(final File hybrisReactorDir) {
        Arrays.asList(1, 2, 3).stream().map(i -> i + 1);

        try {
            final File platformDirectory = new File(hybrisReactorDir, "target/hybris-fake/hybris/bin/platform");
            platformDirectory.mkdirs();

            final File binDirectory = platformDirectory.getParentFile();
            // TODO use restructured platform.extensions.xml
            final File platformExtensionsXML = new File(
                    "src/main/resources/com/divae/ageto/hybris/install/platform.extensions.xml");

            copyFile(platformExtensionsXML, new File(binDirectory, "extensions.xml"));

            // TODO read this list dynamically from reactor and classpath
            for (final String extension : Arrays.asList("core", "catalog", "comments", "commons", "deliveryzone", "europe1",
                    "impex", "paymentstandard", "platformservices", "processing", "scripting", "validation", "workflow")) {
                final File extensionDirectory = new File(binDirectory, "ext/" + extension);
                extensionDirectory.mkdirs();
                copyFile(new File(hybrisReactorDir, extension + "/src/main/resources/extensioninfo.xml"),
                        new File(extensionDirectory, "extensioninfo.xml"));

                copyFile(
                        new File(hybrisReactorDir,
                                String.format("%s/src/main/resources/%s-advanced-deployment.xml", extension, extension)),
                        new File(extensionDirectory, String.format("resources/%s-advanced-deployment.xml", extension)));
                copyFile(new File(hybrisReactorDir, String.format("%s/src/main/resources/%s-beans.xml", extension, extension)),
                        new File(extensionDirectory, String.format("resources/%s-beans.xml", extension)));
                copyFile(new File(hybrisReactorDir, String.format("%s/src/main/resources/%s-items.xml", extension, extension)),
                        new File(extensionDirectory, String.format("resources/%s-items.xml", extension)));
                copyFile(new File(hybrisReactorDir, String.format("%s/src/main/resources/project.properties", extension)),
                        new File(extensionDirectory, "project.properties"));
            }

            copyFile(new File(hybrisReactorDir, "src/main/resources/advanced.properties"),
                    new File(binDirectory, "resources/advanced.properties"));

            copyFile(new File(hybrisReactorDir, "src/main/resources/schemas/beans.xsd"),
                    new File(binDirectory, "resources/schemas/beans.xsd"));

            copyFile(new File(hybrisReactorDir, "src/main/resources/project.properties"),
                    new File(binDirectory, "project.properties"));

            copyFile(new File(hybrisReactorDir, "src/main/resources/project.properties"),
                    new File(binDirectory, "project.properties"));

            copyFile(new File(hybrisReactorDir, "src/main/resources/bootstrap/pojo/global-eventtemplate.vm"),
                    new File(binDirectory, "bootstrap/resources/pojo/global-eventtemplate.vm"));

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
