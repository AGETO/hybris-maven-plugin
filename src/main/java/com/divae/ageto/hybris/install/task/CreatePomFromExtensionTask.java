/*
 * Copyright (C) Klaus Hauschild - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Klaus Hauschild <klaus.hauschild.1984@gmail.com>, 2016
 */
package com.divae.ageto.hybris.install.task;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.Parent;
import org.apache.maven.model.io.xpp3.MavenXpp3Writer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.divae.ageto.hybris.install.extensions.Extension;
import com.divae.ageto.hybris.utils.dependencies.DependencyResolver;
import com.divae.ageto.hybris.utils.dependencies.DependencyResolverDelegator;
import com.google.common.base.Throwables;
import com.google.common.io.PatternFilenameFilter;

/**
 * @author Klaus Hauschild
 */
class CreatePomFromExtensionTask extends AbstractWorkDirectoryTask {

    private static final Logger      LOGGER                = LoggerFactory.getLogger(CreatePomFromExtensionTask.class);

    private static final String      MODEL_VERSION         = "4.0.0";
    private static final String      HYBRIS__GROUP_ID      = "de.hybris";
    private static final String      PLATFORM__ARTIFACT_ID = "platform";

    private final DependencyResolver dependencyResolver    = new DependencyResolverDelegator();
    private final Extension          extension;

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
        addExternalDependency(taskContext, extension, model);
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

    private void addExternalDependency(final TaskContext taskContext, final Extension extension, final Model model) {
        final File libDirectory = new File(
                taskContext.getHybrisDirectory().toPath().resolve(extension.getBaseDirectory().toPath()).toFile(), "lib");
        final File[] libraryFiles = libDirectory.listFiles(new PatternFilenameFilter(".*\\.jar"));
        if (libraryFiles == null) {
            return;
        }
        for (final File libraryFile : libraryFiles) {
            final Dependency dependency = dependencyResolver.resolve(libraryFile);
            model.getDependencies().add(dependency);
        }
    }

    private void writeModel(final File workDirectory, final Model model) {
        final File extensionPom = new File(new File(workDirectory, extension.getName()), "pom.xml");
        FileOutputStream stream = null;
        try {
            if (!extensionPom.getParentFile().exists() && !extensionPom.getParentFile().mkdirs()) {
                throw new RuntimeException(String.format("Can not create directory %s", extensionPom.getParentFile()));
            }
            if (!extensionPom.exists() && !extensionPom.createNewFile()) {
                throw new RuntimeException(String.format("Can not create file %s", extensionPom));
            }
            stream = new FileOutputStream(extensionPom);
            new MavenXpp3Writer().write(stream, model);
        } catch (final IOException exception) {
            throw Throwables.propagate(exception);
        } finally {
            if (stream != null) {
                IOUtils.closeQuietly(stream);
            }
        }
    }

}
