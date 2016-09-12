package com.divae.ageto.hybris.utils.aether;

import java.util.List;
import java.util.Set;

import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.collection.CollectRequest;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.graph.DependencyFilter;
import org.eclipse.aether.resolution.ArtifactResult;
import org.eclipse.aether.resolution.DependencyRequest;
import org.eclipse.aether.resolution.DependencyResolutionException;
import org.eclipse.aether.util.artifact.JavaScopes;
import org.eclipse.aether.util.filter.DependencyFilterUtils;

import com.divae.ageto.hybris.install.task.dependencies.DependencyWrapper;
import com.divae.ageto.hybris.utils.maven.MavenUtils;
import com.google.common.collect.Sets;

/**
 * Created by mhaagen on 28.08.2016.
 */
public class DependencyResolver {

    public static Set<DependencyWrapper> listTransitiveDependencies(Set<DependencyWrapper> dependencySet) {
        Set<DependencyWrapper> dependencies = Sets.newHashSet();

        for (DependencyWrapper dependency : dependencySet) {
            RepositorySystem system = Booter.newRepositorySystem();

            RepositorySystemSession session = Booter.newRepositorySystemSession(system);

            Artifact artifact = new DefaultArtifact(
                    String.format("%s:%s:%s", dependency.getGroupId(), dependency.getArtifactId(), dependency.getVersion()));

            DependencyFilter classpathFlter = DependencyFilterUtils.classpathFilter(JavaScopes.COMPILE);

            CollectRequest collectRequest = new CollectRequest();
            collectRequest.setRoot(new Dependency(artifact, JavaScopes.COMPILE));
            collectRequest.setRepositories(Booter.newRepositories(system, session));

            DependencyRequest dependencyRequest = new DependencyRequest(collectRequest, classpathFlter);

            List<ArtifactResult> artifactResults = null;
            try {
                artifactResults = system.resolveDependencies(session, dependencyRequest).getArtifactResults();
            } catch (DependencyResolutionException e) {
                continue;
            }

            for (ArtifactResult artifactResult : artifactResults) {

                if (artifactResult.getRepository() == null) {
                    continue;
                }

                Artifact artifact1 = artifactResult.getArtifact();

                org.apache.maven.model.Dependency dependency1 = new org.apache.maven.model.Dependency();
                dependency1.setArtifactId(artifact1.getArtifactId());
                dependency1.setGroupId(artifact1.getGroupId());
                dependency1.setVersion(artifact1.getVersion());
                dependency1.setClassifier(artifact1.getClassifier());
                if (!MavenUtils.isDependencyResolvable(dependency1)) {
                    continue;
                }

                dependencies
                        .add(new DependencyWrapper(artifact1.getGroupId(), artifact1.getArtifactId(), artifact1.getVersion()));
            }
        }

        return dependencies;
    }
}
