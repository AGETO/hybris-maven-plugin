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
import org.apache.maven.model.Model;
import org.apache.maven.model.Parent;
import org.apache.maven.model.io.xpp3.MavenXpp3Writer;

import com.divae.ageto.hybris.install.extensions.Extension;
import com.google.common.base.Throwables;

/**
 * @author Klaus Hauschild
 */
class CreatePomFromExtensionTask extends AbstractWorkDirectoryTask {

    private final Extension extension;

    CreatePomFromExtensionTask(final Extension extension) {
        this.extension = extension;
    }

    @Override
    protected void execute(final TaskContext taskContext, final File workDirectory) {
        final Model model = createModel(taskContext, extension);

        final File extensionPom = new File(new File(workDirectory, extension.getName()), "pom.xml");

        FileOutputStream stream = null;
        try {
            extensionPom.getParentFile().mkdirs();
            extensionPom.createNewFile();
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

    private Model createModel(TaskContext taskContext, final Extension extension) {
        final Model model = new Model();
        model.setModelVersion("4.0.0");
        model.setArtifactId(extension.getName());
        model.setVersion(taskContext.getHybrisVersion().getVersion());
        final Parent parent = new Parent();
        parent.setGroupId("de.hybris");
        parent.setArtifactId("platform");
        parent.setVersion(taskContext.getHybrisVersion().getVersion());
        model.setParent(parent);
        // TODO add dependencies
        return model;
    }

}
