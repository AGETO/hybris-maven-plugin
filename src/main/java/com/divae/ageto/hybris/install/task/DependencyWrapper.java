package com.divae.ageto.hybris.install.task;

import java.util.Objects;

import org.apache.maven.model.Dependency;

/**
 * Created by mhaagen on 24.08.2016.
 */
public class DependencyWrapper extends Dependency {

    private final Dependency dependency;

    public DependencyWrapper(final Dependency dependency) {
        this.dependency = dependency;
    }

    public DependencyWrapper(final String groupId, final String artifactId, final String version) {
        dependency = new Dependency();
        dependency.setArtifactId(artifactId);
        dependency.setGroupId(groupId);
        dependency.setVersion(version);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        DependencyWrapper that = (DependencyWrapper) o;
        return (dependency.getArtifactId().equals(that.dependency.getArtifactId())
                && dependency.getGroupId().equals(that.dependency.getGroupId())
                && dependency.getVersion().equals(that.dependency.getVersion()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(dependency);
    }
}
