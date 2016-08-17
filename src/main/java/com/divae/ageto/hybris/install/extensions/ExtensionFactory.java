package com.divae.ageto.hybris.install.extensions;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.divae.ageto.hybris.install.extensions.binary.ClassFolder;
import com.divae.ageto.hybris.install.extensions.binary.ExtensionBinary;
import com.divae.ageto.hybris.install.extensions.binary.JARArchive;
import com.divae.ageto.hybris.install.extensions.binary.None;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * @author Marvin Haagen
 */
public enum ExtensionFactory {

    ;

    private static final Logger                 LOGGER     = LoggerFactory.getLogger(ExtensionFactory.class);

    private static final Map<String, Extension> EXTENSIONS = Maps.newHashMap();

    public static List<Extension> getExtensions(final File hybrisInstallDirectory, final File extensionsDirectory,
            List<File> excludeExtensionsPaths) {
        LOGGER.info(String.format("Read extensions of hybris installed in %s", hybrisInstallDirectory));
        final List<String> extensionNames = Extensions.getExtensionNames(hybrisInstallDirectory);
        final Map<String, File> extensionPaths = getExtensionPaths(extensionsDirectory, extensionsDirectory,
                excludeExtensionsPaths);
        final List<Extension> extensions = Lists.newArrayList();

        for (final String extensionName : extensionNames) {
            final Extension extension = createExtension(extensionName, extensionPaths, hybrisInstallDirectory,
                    extensionsDirectory);
            extensions.add(extension);
        }

        return extensions;
    }

    public static List<Extension> getExtensions(final File hybrisInstallDirectory) {

        final List<String> extensionNames = Extensions.getExtensionNames(hybrisInstallDirectory);
        final Map<String, File> extensionPaths = getExtensionPaths(hybrisInstallDirectory);
        final List<Extension> extensions = Lists.newArrayList();

        for (final String extensionName : extensionNames) {
            final Extension extension = createExtension(extensionName, extensionPaths, hybrisInstallDirectory,
                    hybrisInstallDirectory);
            extensions.add(extension);
        }

        return extensions;
    }

    public static List<Extension> getTransitiveExtensions(final List<Extension> extensions) {
        final Set<Extension> transitiveExtensions = Sets.newHashSet();

        for (final Extension extension : extensions) {
            collectExtension(extension, transitiveExtensions);
        }

        return Lists.newArrayList(transitiveExtensions);
    }

    private static void collectExtension(Extension extension, Set<Extension> extensions) {
        extensions.add(extension);

        for (Extension dependency : extension.getDependencies()) {
            collectExtension(dependency, extensions);
        }
    }

    private static Map<String, File> getExtensionPaths(final File hybrisInstallDirectory) {
        return getExtensionPaths(hybrisInstallDirectory, new File(hybrisInstallDirectory, "bin"), Lists.newArrayList());
    }

    private static Map<String, File> getExtensionPaths(final File hybrisInstallDirectory, final File hybrisBinDirectory,
            final List<File> excludePaths) {
        try {
            final Map<String, File> extensionPaths = Maps.newHashMap();
            Files.walkFileTree(hybrisBinDirectory.toPath(), new SimpleFileVisitor<Path>() {

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    if (excludePaths.contains(hybrisInstallDirectory.toPath().relativize(dir).toFile())) {
                        return FileVisitResult.SKIP_SUBTREE;
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
                    if (file.getFileName().toString().equals("extensioninfo.xml")) {
                        String extensionName = ExtensionInfo.getExtensionName(file.toFile());
                        Path extensionDirectory = hybrisInstallDirectory.toPath().relativize(file.getParent());

                        extensionPaths.put(extensionName, extensionDirectory.toFile());
                    }
                    return FileVisitResult.CONTINUE;
                }

            });
            return extensionPaths;
        } catch (final IOException e) {
            throw Throwables.propagate(e);
        }
    }

    private static List<Extension> getDependencies(final String extensionName, final Map<String, File> extensionPaths,
            File hybrisInstallDirectory, File hybrisBinDirectory) {
        File extensionPath = extensionPaths.get(extensionName);
        File extensionInfo = new File(extensionPath.toString(), "extensioninfo.xml");
        List<String> dependencyNames;
        dependencyNames = ExtensionInfo.getDependencyNames(extensionInfo, hybrisBinDirectory);

        List<Extension> extensions = Lists.newArrayList();

        for (String dependencyName : dependencyNames) {
            if (!EXTENSIONS.containsKey(dependencyName)) {
                createExtension(dependencyName, extensionPaths, hybrisInstallDirectory, hybrisBinDirectory);
            }
            extensions.add(EXTENSIONS.get(dependencyName));
        }

        return extensions;
    }

    private static Extension createExtension(final String extensionName, final Map<String, File> extensionPaths,
            File hybrisInstallDirectory, File hybrisBinDirectory) {
        LOGGER.debug(String.format("Read extension informations for: %s", extensionName));
        File baseDirectory = extensionPaths.get(extensionName);
        ExtensionBinary binary = getBinary(extensionName, extensionPaths);
        List<Extension> dependencies = getDependencies(extensionName, extensionPaths, hybrisInstallDirectory, hybrisBinDirectory);
        final Extension extension = new Extension(baseDirectory, extensionName, binary, dependencies);
        EXTENSIONS.put(extensionName, extension);
        return extension;
    }

    public static ExtensionBinary getBinary(final String extensionName, final Map<String, File> extensionPaths) {

        File extensionFolder = extensionPaths.get(extensionName);
        final File binPath = new File(extensionFolder, "bin");
        final File classPath = new File(extensionFolder, "classes");

        if (binPath.exists()) {
            return new JARArchive(getJARArchive(binPath, extensionName));
        }
        if (classPath.exists()) {
            return new ClassFolder(classPath);
        }

        return new None(); // extension has no binary
    }

    private static File getJARArchive(File binPath, String extensionName) {
        File[] files = binPath.listFiles((File pathname) -> {

                File fileName = new File(pathname.getName());

            return fileName.toString().equals(String.format("%sserver.jar", extensionName));

        });

        return files[0];
    }

}
