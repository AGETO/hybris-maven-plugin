/*
 * Copyright (C) Klaus Hauschild - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Klaus Hauschild <klaus.hauschild.1984@gmail.com>, 2016
 */
package com.divae.ageto.hybris.install.task;

import static com.divae.ageto.hybris.utils.aether.DependencyResolver.findNewestVersion;
import static com.divae.ageto.hybris.utils.aether.DependencyResolver.listTransitiveDependencies;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.Parent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.divae.ageto.hybris.install.extensions.Extension;
import com.divae.ageto.hybris.install.task.dependencies.DependencyVersion;
import com.divae.ageto.hybris.install.task.dependencies.DependencyWrapper;
import com.divae.ageto.hybris.install.task.dependencies.ExclusionWrapper;
import com.divae.ageto.hybris.utils.FileUtils;
import com.divae.ageto.hybris.utils.maven.MavenExecutorUtils;
import com.divae.ageto.hybris.utils.maven.MavenUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * @author Klaus Hauschild
 */
class CreatePomFromExtensionTask extends AbstractWorkDirectoryTask {

    private static final Logger LOGGER                = LoggerFactory.getLogger(CreatePomFromExtensionTask.class);

    private static final String MODEL_VERSION         = "4.0.0";
    private static final String HYBRIS__GROUP_ID      = "de.hybris";
    private static final String PLATFORM__ARTIFACT_ID = "platform";

    private final Extension     extension;
    private final String        packaging;

    CreatePomFromExtensionTask(final Extension extension, final String packaging) {
        this.extension = extension;
        this.packaging = packaging;
    }

    CreatePomFromExtensionTask(final Extension extension) {
        this.extension = extension;
        this.packaging = null;
    }

    @Override
    protected void execute(final TaskContext taskContext, final File workDirectory) {
        final Model model = createModel(taskContext, extension);
        writeModel(workDirectory, model);
    }

    private Model createModel(TaskContext taskContext, final Extension extension) {
        final Model model = new Model();
        model.setModelVersion(MODEL_VERSION);
        model.setArtifactId(extension.getName());
        model.setVersion(taskContext.getHybrisVersion().getVersion());
        final Parent parent = new Parent();
        parent.setGroupId(HYBRIS__GROUP_ID);
        parent.setArtifactId(PLATFORM__ARTIFACT_ID);
        parent.setVersion(taskContext.getHybrisVersion().getVersion());
        model.setParent(parent);
        setExtensionPackaging(model);
        addExtensionDependencies(taskContext, extension, model);
        addImplicitDependencies(taskContext, extension, model);
        addExternalDependencies(taskContext, extension, model);
        resolveDependencyVersionConflicts(taskContext, extension, model);
        return model;
    }

    private void resolveDependencyVersionConflicts(TaskContext taskContext, Extension extension, Model model) {
        Set<DependencyWrapper> dependencySet = Sets.newHashSet();
        for (Dependency dependency : model.getDependencies()) {
            dependencySet.add(new DependencyWrapper(dependency));
        }

        addTransitiveDependencies(dependencySet);
        unifyDependencyVersions(dependencySet);

        List<Dependency> dependencies = Lists.newArrayList();
        for (DependencyWrapper dependency : dependencySet) {
            dependencies.add(dependency);
        }

        model.setDependencies(dependencies);
    }

    private void unifyDependencyVersions(Set<DependencyWrapper> dependencySet) {
        for (DependencyWrapper dependency : dependencySet) {
            final DependencyWrapper key = new DependencyWrapper(dependency.getGroupId(), dependency.getArtifactId());
            final Map<DependencyWrapper, String> dependencyVersion = getDependencyVersion(dependencySet);
            if (dependencyVersion.containsKey(key)) {
                dependency.setVersion(dependencyVersion.get(key));
            }
        }
    }

    private void addTransitiveDependencies(Set<DependencyWrapper> dependencySet) {
        dependencySet.addAll(listTransitiveDependencies(dependencySet));
        for (DependencyWrapper dependency : dependencySet) {
            dependency.setVersion(findNewestVersion(dependency).getVersion());
        }
    }

    private Map<DependencyWrapper, String> getDependencyVersion(Set<DependencyWrapper> dependencies) {
        Map<DependencyWrapper, String> dependencyMap = Maps.newHashMap();

        final Map<DependencyWrapper, DependencyVersion> dependencyGroupMap = newDependencyGroupMap();
        for (DependencyWrapper dependency : dependencies) {
            final DependencyWrapper key = new DependencyWrapper(dependency.getGroupId(), dependency.getArtifactId());
            if (dependencyGroupMap.containsKey(key)) {
                final DependencyVersion groupVersion = dependencyGroupMap.get(key);

                if (groupVersion.getVersion().compareTo(dependency.getVersion()) < 0) {
                    groupVersion.setVersion(dependency.getVersion());
                }
            }
        }

        for (DependencyWrapper dependencyWrapper : dependencyGroupMap.keySet()) {
            dependencyMap.put(dependencyWrapper, dependencyGroupMap.get(dependencyWrapper).getVersion());
        }

        return dependencyMap;
    }

    private Map<DependencyWrapper, DependencyVersion> newDependencyGroupMap() {
        Map<DependencyWrapper, DependencyVersion> dependencyGroupMap = Maps.newHashMap();

        for (Set<DependencyWrapper> dependencyWrappers : getDependencyGroups()) {
            DependencyVersion dependencyVersion = new DependencyVersion();

            for (DependencyWrapper dependencyWrapper : dependencyWrappers) {
                dependencyGroupMap.put(dependencyWrapper, dependencyVersion);
            }
        }

        return dependencyGroupMap;
    }

    private Set<Set<DependencyWrapper>> getDependencyGroups() {
        Set<Set<DependencyWrapper>> dependencyGroups = Sets.newHashSet();

        dependencyGroups
                .addAll(Collections.singletonList(Sets.newHashSet(Arrays.asList(new DependencyWrapper("org.slf4j", "slf4j-api"),
                        new DependencyWrapper("org.slf4j", "slf4j-jcl"), new DependencyWrapper("org.slf4j", "slf4j-log4j12")))));

        return dependencyGroups;
    }

    private void setExtensionPackaging(Model model) {
        if (packaging != null) {
            model.setPackaging(packaging);
        }
    }

    private void addExtensionDependencies(final TaskContext taskContext, final Extension extension, final Model model) {
        for (final Extension extensionDependency : extension.getDependencies()) {
            final Dependency dependency = new Dependency();
            dependency.setGroupId(HYBRIS__GROUP_ID);
            dependency.setArtifactId(extensionDependency.getName());
            dependency.setVersion(taskContext.getHybrisVersion().getVersion());
            addExcludes(dependency);
            model.getDependencies().add(dependency);
        }
    }

    private void addImplicitDependencies(final TaskContext taskContext, final Extension extension, final Model model) {
        final Dependency dependency = new Dependency();
        dependency.setGroupId(HYBRIS__GROUP_ID);
        dependency.setVersion(taskContext.getHybrisVersion().getVersion());
        switch (extension.getName()) {
            case "core":
                dependency.setArtifactId("models");

                final Dependency servletApi = new Dependency();
                servletApi.setGroupId("javax.servlet");
                servletApi.setVersion("3.0.1");
                servletApi.setArtifactId("javax.servlet-api");
                model.addDependency(servletApi);
                break;
            default:
                dependency.setArtifactId("core");
                break;
        }
        addExcludes(dependency);
        model.getDependencies().add(dependency);
    }

    private void addExternalDependencies(final TaskContext taskContext, final Extension extension, final Model model) {
        final File extensionDirectory = taskContext.getHybrisDirectory().toPath().resolve(extension.getBaseDirectory().toPath())
                .toFile();
        final File externalDependenciesDefinition = extension.getExternalDependenciesXML(taskContext.getHybrisDirectory());
        if (externalDependenciesDefinition == null) {
            return;
        }
        final Model externalDependenciesModel = MavenUtils.readModel(externalDependenciesDefinition);
        externalDependenciesModel.getDependencies().forEach(dependency -> {
            if (dependency.getVersion().startsWith("${") && dependency.getVersion().endsWith("}")) {
                final String versionProperty = dependency.getVersion().substring(2, dependency.getVersion().length() - 1);
                final String version = externalDependenciesModel.getProperties().getProperty(versionProperty);
                dependency.setVersion(version);
            }
            installDependencyLocallyIfNeeded(extensionDirectory, dependency);
            addExcludes(dependency);

            model.getDependencies().add(dependency);
        });
    }

    private void addExcludes(Dependency dependency) {
        Set<ExclusionWrapper> exclusions = getExclusions(dependency);
        if (exclusions.size() > 0) {
            for (ExclusionWrapper exclusion : exclusions) {
                dependency.addExclusion(exclusion.getExclusion());
            }
        }
    }

    private void installDependencyLocallyIfNeeded(final File extensionDirectory, final Dependency dependency) {
        if (MavenUtils.isDependencyResolvable(dependency)) {
            return;
        }
        final String artifactName = String.format("%s-%s", dependency.getArtifactId(), dependency.getVersion());
        LOGGER.info(String.format("Dependency %s unresolvable, install it locally.", artifactName));
        final File extensionLibDirectory = new File(extensionDirectory, "lib");
        final File artifactFile = new File(extensionLibDirectory, String.format("%s.jar", artifactName));
        if (artifactFile.exists()) {
            MavenExecutorUtils.installLibrary(artifactFile, dependency);
        } else {
            LOGGER.warn(String.format("Unresolvable dependency %s declared, but does not exist in %s", artifactName,
                    extensionLibDirectory));
            // TODO this case occurs while restructuring core
            // bsh-2.0b4 is declared, but bsh-2.0b5.jar is contained in /lib
        }
    }

    private void writeModel(final File workDirectory, final Model model) {
        final File extensionPom = new File(new File(workDirectory, extension.getName()), "pom.xml");
        FileUtils.makeDirectory(extensionPom.getParentFile());
        FileUtils.makeFile(extensionPom);
        MavenUtils.writeModel(model, extensionPom);
    }

    private Set<ExclusionWrapper> getExclusions(Dependency dependency) {
        if (getExcludesMap().get(new DependencyWrapper(dependency)) == null) {
            return Sets.newHashSet();
        }
        return getExcludesMap().get(new DependencyWrapper(dependency));
    }

    private Map<DependencyWrapper, Set<ExclusionWrapper>> getExcludesMap() {

        Map<DependencyWrapper, Set<ExclusionWrapper>> dependencyMap = new HashMap<>();
        dependencyMap.put(new DependencyWrapper("org.apache.ddlutils", "ddlutils", "1.0"),
                Sets.newHashSet(new ExclusionWrapper("commons-logging", "commons-logging-api")));
        dependencyMap.put(new DependencyWrapper("de.hybris", "core", "5.5.1.1"), Sets.newHashSet(
                new ExclusionWrapper("org.slf4j", "slf4j-jcl"), new ExclusionWrapper("commons-logging", "commons-logging")));

        return dependencyMap;
    }
}
