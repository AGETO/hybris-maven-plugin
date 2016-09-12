package com.divae.ageto.hybris.codegenerator;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;

import org.apache.commons.io.filefilter.FileFilterUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.divae.ageto.hybris.install.extensions.Extension;
import com.divae.ageto.hybris.install.task.metadata.ExtensionMetadataFile;
import com.divae.ageto.hybris.utils.FileUtils;
import com.divae.ageto.hybris.utils.Utils;
import com.google.common.base.Throwables;

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
            final List<Extension> extensions = Utils.readExtensionsFromReactorModules(hybrisReactorDir);

            for (Extension extension : extensions) {

                final File source = new File(String.format("%s/%s/src/main/resources", hybrisReactorDir, extension.getName()));
                LOGGER.info(source.toString());
                Extension extensionProperties = ExtensionMetadataFile.readMetadataFile(hybrisReactorDir, extension.getName());

                final File extensionDirectory = new File(hybrisFakeDirectory, extensionProperties.getBaseDirectory().toString());

                com.divae.ageto.hybris.utils.FileUtils.makeDirectory(extensionDirectory);

                if (new File(source.getParentFile(), "webapp").exists()) {
                    copyDirectory(new File(source.getParentFile(), "webapp"),
                            new File(new File(hybrisFakeDirectory, extension.getBaseDirectory().toString()), "webroot"));
                }

                copyFile(new File(source, String.format("%s-advanced-deployment.xml", extension.getName())),
                        new File(extensionDirectory, String.format("resources/%s-advanced-deployment.xml", extension.getName())));

                copyFile(new File(source, String.format("%s-beans.xml", extension.getName())),
                        new File(extensionDirectory, String.format("resources/%s-beans.xml", extension.getName())));

                copyFile(new File(source, String.format("%s-items.xml", extension.getName())),
                        new File(extensionDirectory, String.format("resources/%s-items.xml", extension.getName())));

                copyFile(new File(source, "project.properties"), new File(extensionDirectory, "project.properties"));

                copyFile(new File(source, "extensioninfo.xml"), new File(extensionDirectory, "extensioninfo.xml"));
            }

            return platformDirectory;
        } catch (final Exception exception) {
            throw Throwables.propagate(exception);
        }
    }

    private static void copyDirectory(final File srcDirectory, final File destDirectory) {
        LOGGER.info(String.format("Copying directory %s to %s", srcDirectory, destDirectory));
        if (!srcDirectory.exists()) {
            LOGGER.info("Source directory not exists");
            return;
        }

        final FileFilter fileFilter = FileFilterUtils.trueFileFilter();

        com.divae.ageto.hybris.utils.FileUtils.makeDirectory(destDirectory.getParentFile());

        if (srcDirectory.isFile()) {
            throw new RuntimeException("Source path is not a directory.");
        }
        if (destDirectory.isFile()) {
            throw new RuntimeException("Target path is not a directory.");
        }

        try {
            Files.walkFileTree(srcDirectory.toPath(), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    if (fileFilter.accept(file.toFile())) {
                        Path relativePath = srcDirectory.toPath().relativize(file);
                        copyFile(file.toFile(), new File(destDirectory, relativePath.toString()));
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    if (fileFilter.accept(dir.toFile())) {
                        Path relativePath = srcDirectory.toPath().relativize(dir);
                        FileUtils.makeDirectory(new File(destDirectory, relativePath.toString()));
                        return FileVisitResult.CONTINUE;
                    }
                    return FileVisitResult.SKIP_SUBTREE;
                }
            });
        } catch (IOException e) {
            Throwables.propagate(e);
        }
    }

    private static void copyFile(final File srcFile, final File destFile) throws IOException {
        LOGGER.info(String.format("Copying file %s to %s", srcFile, destFile));
        if (!srcFile.exists()) {
            LOGGER.info("Source file not exists");
            return;
        }

        FileUtils.makeDirectory(destFile.getParentFile());

        // FileUtils.copyFile(srcFile, destFile);
        Utils.createSymLink(destFile, srcFile);
    }

}
