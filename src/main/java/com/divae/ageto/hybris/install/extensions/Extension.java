package com.divae.ageto.hybris.install.extensions;

import java.io.File;
import java.util.List;
import java.util.Objects;

import com.divae.ageto.hybris.install.extensions.binary.ExtensionBinary;
import com.google.common.collect.Lists;

/**
 * @author Marvin Haagen
 */
public class Extension {

    private final File            baseDirectory;
    private final String          name;
    private final ExtensionBinary binary;
    private final List<Extension> dependencies;

    public Extension(final File baseDirectory, String name, ExtensionBinary binary) {
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

    public File getExtensionDirectory() {
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
        final List<String> dependencyNames = Lists.newArrayList();
        for (final Extension dependency : dependencies) {
            dependencyNames.add(dependency.getName());
        }
        return String.format("%s %s", name, dependencyNames);
    }

}
