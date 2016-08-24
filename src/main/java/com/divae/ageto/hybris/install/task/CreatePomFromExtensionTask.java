/*
 * Copyright (C) Klaus Hauschild - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Klaus Hauschild <klaus.hauschild.1984@gmail.com>, 2016
 */
package com.divae.ageto.hybris.install.task;

import java.io.File;
import java.util.Map;
import java.util.Set;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Exclusion;
import org.apache.maven.model.Model;
import org.apache.maven.model.Parent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.divae.ageto.hybris.install.extensions.Extension;
import com.divae.ageto.hybris.utils.FileUtils;
import com.divae.ageto.hybris.utils.maven.MavenExecutorUtils;
import com.divae.ageto.hybris.utils.maven.MavenUtils;
import com.google.common.collect.ImmutableMap;
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
        setExtensionPackaging(taskContext, model);
        addExtensionDependencies(taskContext, extension, model);
        addImplicitDependencies(taskContext, extension, model);
        addExternalDependencies(taskContext, extension, model);
        return model;
    }

    private void setExtensionPackaging(TaskContext taskContext, Model model) {
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
        Set<Exclusion> exclusions = getExclusions(dependency);
        if (exclusions.size() > 0) {
            for (Exclusion exclusion : exclusions) {
                dependency.addExclusion(exclusion);
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

    private Set<Exclusion> getExclusions(Dependency dependency) {
        if (getExcludesMap().get(new DependencyWrapper(dependency)) == null) {
            return Sets.newHashSet();
        }
        return getExcludesMap().get(new DependencyWrapper(dependency));
    }

    private Map<DependencyWrapper, Set<Exclusion>> getExcludesMap() {

        final Map<DependencyWrapper, Set<Exclusion>> excludesMap = ImmutableMap.of(
                new DependencyWrapper("displaytag", "displaytag", "1.2"),
                Sets.newHashSet(newExclusion("org.slf4j", "jcl104-over-slf4j")));

        return excludesMap;
    }

    private Exclusion newExclusion(final String groupId, final String artifactId) {
        Exclusion exclusion = new Exclusion();

        exclusion.setArtifactId(artifactId);
        exclusion.setGroupId(groupId);

        return exclusion;
    }
}
