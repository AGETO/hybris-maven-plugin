package com.divae.ageto.hybris.install.task.metadata;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.divae.ageto.hybris.install.extensions.Extension;
import com.divae.ageto.hybris.install.task.AbstractWorkDirectoryTask;
import com.divae.ageto.hybris.install.task.TaskContext;

/**
 * Created by mhaagen on 12.08.2016.
 */
public class CreateExtensionMetadataFileTask extends AbstractWorkDirectoryTask {

    private final Extension extension;
    private final Logger    LOGGER = LoggerFactory.getLogger(CreateExtensionMetadataFileTask.class);

    public CreateExtensionMetadataFileTask(Extension extension) {
        this.extension = extension;
    }

    @Override
    protected void execute(TaskContext taskContext, File workDirectory) {
        LOGGER.debug(String.format("Metadata file for extension '%s' created (%s)", extension.getName(),
                ExtensionMetadataFile.createMetadataFile(extension, workDirectory)));
    }
}
