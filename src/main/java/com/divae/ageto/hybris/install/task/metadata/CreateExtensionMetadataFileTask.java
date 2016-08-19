package com.divae.ageto.hybris.install.task.metadata;

import java.io.File;

import com.divae.ageto.hybris.install.extensions.Extension;
import com.divae.ageto.hybris.install.task.AbstractWorkDirectoryTask;
import com.divae.ageto.hybris.install.task.TaskContext;

/**
 * @author Marvin Haagen
 */
public class CreateExtensionMetadataFileTask extends AbstractWorkDirectoryTask {

    private final Extension extension;

    public CreateExtensionMetadataFileTask(final Extension extension) {
        this.extension = extension;
    }

    @Override
    protected void execute(final TaskContext taskContext, final File workDirectory) {
        ExtensionMetadataFile.createMetadataFile(extension, workDirectory);
    }

}
