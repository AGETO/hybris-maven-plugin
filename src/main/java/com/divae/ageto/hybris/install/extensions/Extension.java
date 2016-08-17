package com.divae.ageto.hybris.install.extensions;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.divae.ageto.hybris.install.extensions.binary.ExtensionBinary;
import com.divae.ageto.hybris.install.extensions.binary.None;
import com.google.common.base.Throwables;

/**
 * @author Marvin Haagen
 */
public class Extension {

    private final File            baseDirectory;
    private final String          name;
    private final ExtensionBinary binary;
    private final List<Extension> dependencies;

    public Extension(final File baseDirectory, final String name, final ExtensionBinary binary) {

        this.baseDirectory = baseDirectory;
        this.name = name;
        this.binary = binary;
        this.dependencies = null;
    }

    public Extension(final File baseDirectory, final String name, final ExtensionBinary binary,
            final List<Extension> dependencies) {
        this.baseDirectory = baseDirectory;
        this.name = name;
        this.binary = binary;
        this.dependencies = dependencies;
    }

    public File getExternalDependenciesXML(final File hybrisDirectory) {
        if (new File(hybrisDirectory, new File(baseDirectory, "external-dependencies.xml").toString()).exists()) {
            return new File(hybrisDirectory, new File(baseDirectory, "external-dependencies.xml").toString());
        }
        if (!(binary instanceof None)) {
            if (new File(binary.getExtensionBinaryPath(), "external-dependencies.xml").exists()) {
                return new File(binary.getExtensionBinaryPath(), "external-dependencies.xml");
            }
        }
        return findExternalDependenciesXML(hybrisDirectory);
    }

    private File findExternalDependenciesXML(final File hybrisDirectory) {
        final File[] externalDependenciesFile = { null };
        try {
            Files.walkFileTree(new File(hybrisDirectory, baseDirectory.toString()).toPath(), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    if (file.endsWith("external-dependencies.xml")) {
                        externalDependenciesFile[0] = file.toFile();
                        return FileVisitResult.TERMINATE;
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
        return externalDependenciesFile[0];
    }

    File getExtensionDirectory() {
        return new File(getName());
    }

    public File getSourcesDirectory() {
        return new File(getExtensionDirectory(), "src/main/java");
    }

    public File getTestSourcesDirectory() {
        return new File(getExtensionDirectory(), "src/test/java");
    }

    public File getResourcesDirectory() {
        return new File(getExtensionDirectory(), "src/main/resources");
    }

    public File getBaseDirectory() {
        return baseDirectory;
    }

    public String getName() {
        return name;
    }

    public ExtensionBinary getBinary() {
        return binary;
    }

    public List<Extension> getDependencies() {
        return dependencies;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Extension extension = (Extension) o;
        return Objects.equals(name, extension.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(name);
        if (dependencies != null) {
            builder.append(" ");
            builder.append(dependencies.stream().map(Extension::getName).collect(Collectors.toList()));
        }
        return builder.toString();
    }

}
