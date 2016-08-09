package com.divae.ageto.hybris.codegenerator;

import java.io.File;
import java.io.FileOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import com.google.common.base.Throwables;

/**
 * @author Marvin Haagen
 */
public class HybrisFakeStructure {

    static File generate(final File hybrisReactorDir) {
        try {
            final File platformDirectory = new File(hybrisReactorDir, "target/hybris-fake/hybris/bin/platform");
            platformDirectory.mkdirs();

            final File binDirectory = platformDirectory.getParentFile();
            IOUtils.copy(ClassLoader.getSystemResourceAsStream("com/divae/ageto/hybris/install/platform.extensions.xml"),
                    new FileOutputStream(new File(binDirectory, "extensions.xml")));

            final File coreExtensionDirectory = new File(binDirectory, "ext/core");
            coreExtensionDirectory.mkdirs();
            FileUtils.copyFile(new File(hybrisReactorDir, "core/src/main/resources/extensioninfo.xml"),
                    new File(coreExtensionDirectory, "extensioninfo.xml"));
            FileUtils.copyFile(new File(hybrisReactorDir, "core/src/main/resources/core-advanced-deployment.xml"),
                    new File(coreExtensionDirectory, "resources/core-advanced-deployment.xml"));
            FileUtils.copyFile(new File(hybrisReactorDir, "core/src/main/resources/core-beans.xml"),
                    new File(coreExtensionDirectory, "resources/core-beans.xml"));
            FileUtils.copyFile(new File(hybrisReactorDir, "core/src/main/resources/core-items.xml"),
                    new File(coreExtensionDirectory, "resources/core-items.xml"));
            FileUtils.copyFile(new File(hybrisReactorDir, "core/src/main/resources/project.properties"),
                    new File(coreExtensionDirectory, "project.properties"));

            FileUtils.copyFile(new File(hybrisReactorDir, "src/main/resources/advanced.properties"),
                    new File(binDirectory, "resources/advanced.properties"));

            FileUtils.copyFile(new File(hybrisReactorDir, "src/main/resources/schemas/beans.xsd"),
                    new File(binDirectory, "resources/schemas/beans.xsd"));

            FileUtils.copyFile(new File(hybrisReactorDir, "src/main/resources/project.properties"),
                    new File(binDirectory, "project.properties"));

            FileUtils.copyFile(new File(hybrisReactorDir, "src/main/resources/project.properties"),
                    new File(binDirectory, "project.properties"));

            FileUtils.copyFile(new File(hybrisReactorDir, "src/main/resources/bootstrap/pojo/global-eventtemplate.vm"),
                    new File(binDirectory, "bootstrap/resources/pojo/global-eventtemplate.vm"));

            return binDirectory;
        } catch (final Exception exception) {
            throw Throwables.propagate(exception);
        }
    }

}
