/*
 * Copyright (C) Klaus Hauschild - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Klaus Hauschild <klaus.hauschild.1984@gmail.com>, 2016
 */
package com.divae.ageto.hybris.install.task;

import java.io.File;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.Parent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.divae.ageto.hybris.install.extensions.Extension;
import com.divae.ageto.hybris.utils.FileUtils;
import com.divae.ageto.hybris.utils.maven.MavenExecutorUtils;
import com.divae.ageto.hybris.utils.maven.MavenUtils;

/**
 * @author Klaus Hauschild
 */
class CreatePomFromExtensionTask extends AbstractWorkDirectoryTask {

    private static final Logger LOGGER                = LoggerFactory.getLogger(CreatePomFromExtensionTask.class);

    private static final String MODEL_VERSION         = "4.0.0";
    private static final String HYBRIS__GROUP_ID      = "de.hybris";
    private static final String PLATFORM__ARTIFACT_ID = "platform";

    private final Extension     extension;

    CreatePomFromExtensionTask(final Extension extension) {
        this.extension = extension;
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
        addExtensionDependencies(taskContext, extension, model);
        addModelsDependency(taskContext, extension, model);
        addExternalDependencies(taskContext, extension, model);
        return model;
    }

    private void addExtensionDependencies(final TaskContext taskContext, final Extension extension, final Model model) {
        for (final Extension extensionDependency : extension.getDependencies()) {
            final Dependency dependency = new Dependency();
            dependency.setGroupId(HYBRIS__GROUP_ID);
            dependency.setArtifactId(extensionDependency.getName());
            dependency.setVersion(taskContext.getHybrisVersion().getVersion());
            model.getDependencies().add(dependency);
        }
    }

    private void addModelsDependency(final TaskContext taskContext, final Extension extension, final Model model) {
        if (!extension.getName().equals("core")) {
            return;
        }
        final Dependency dependency = new Dependency();
        dependency.setGroupId(HYBRIS__GROUP_ID);
        dependency.setArtifactId("models");
        dependency.setVersion(taskContext.getHybrisVersion().getVersion());
        model.getDependencies().add(dependency);
    }

    private void addExternalDependencies(final TaskContext taskContext, final Extension extension, final Model model) {
        final File extensionDirectory = taskContext.getHybrisDirectory().toPath().resolve(extension.getBaseDirectory().toPath())
                .toFile();
        final File externalDependenciesDefinition = new File(extensionDirectory, "external-dependencies.xml");
        final Model externalDependenciesModel = MavenUtils.readModel(externalDependenciesDefinition);
        externalDependenciesModel.getDependencies().forEach(dependency -> {
            if (dependency.getVersion().startsWith("${") && dependency.getVersion().endsWith("}")) {
                final String versionProperty = dependency.getVersion().substring(2, dependency.getVersion().length() - 1);
                final String version = externalDependenciesModel.getProperties().getProperty(versionProperty);
                dependency.setVersion(version);
            }
            installDependencyLocallyIfNeeded(extensionDirectory, dependency);
            model.getDependencies().add(dependency);
        });
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

}
