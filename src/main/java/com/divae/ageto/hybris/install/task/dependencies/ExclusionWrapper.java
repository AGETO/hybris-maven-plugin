package com.divae.ageto.hybris.install.task.dependencies;

import java.util.Objects;

import org.apache.maven.model.Exclusion;

/**
 * Created by mhaagen on 26.08.2016.
 */
public class ExclusionWrapper {
    private final Exclusion exclusion;
    private final String    groupId;
    private final String    artifactId;

    public ExclusionWrapper(final String groupId, final String artifactId) {
        exclusion = new Exclusion();
        exclusion.setArtifactId(artifactId);
        exclusion.setGroupId(groupId);
        this.groupId = groupId;
        this.artifactId = artifactId;
    }

    public Exclusion getExclusion() {
        return exclusion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ExclusionWrapper that = (ExclusionWrapper) o;
        return Objects.equals(groupId, that.groupId) && Objects.equals(artifactId, that.artifactId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupId, artifactId);
    }
}
