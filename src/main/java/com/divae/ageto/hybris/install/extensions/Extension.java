package com.divae.ageto.hybris.install.extensions;

import java.io.File;
import java.util.List;
import java.util.Objects;

import com.divae.ageto.hybris.install.extensions.binary.ExtensionBinary;

/**
 * Created by mhaagen on 10.08.2016.
 */
public class Extension {

    private final File            baseDirectory;
    private final String          name;
    private final ExtensionBinary binary;
    private final List<Extension> dependencies;

    public Extension(final File baseDirectory, final String name, final ExtensionBinary binary,
            final List<Extension> dependencies) {
        this.baseDirectory = baseDirectory;
        this.name = name;
        this.binary = binary;
        this.dependencies = dependencies;
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
}
