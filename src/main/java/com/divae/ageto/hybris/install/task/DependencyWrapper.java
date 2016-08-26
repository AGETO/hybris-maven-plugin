package com.divae.ageto.hybris.install.task;

import java.util.Objects;

import org.apache.maven.model.Dependency;

/**
 * Created by mhaagen on 24.08.2016.
 */
public class DependencyWrapper extends Dependency {

    private final Dependency dependency;
    private final String     artifactId;
    private final String     groupId;
    private final String     version;

    public DependencyWrapper(final Dependency dependency) {
        this.dependency = dependency;
        this.artifactId = dependency.getArtifactId();
        this.groupId = dependency.getGroupId();
        this.version = dependency.getVersion();
    }

    public DependencyWrapper(final String groupId, final String artifactId, final String version) {
        dependency = new Dependency();
        dependency.setArtifactId(artifactId);
        dependency.setGroupId(groupId);
        dependency.setVersion(version);
        this.artifactId = artifactId;
        this.groupId = groupId;
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        DependencyWrapper that = (DependencyWrapper) o;
        return Objects.equals(artifactId, that.artifactId) && Objects.equals(groupId, that.groupId)
                && Objects.equals(version, that.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(artifactId, groupId, version);
    }
}
