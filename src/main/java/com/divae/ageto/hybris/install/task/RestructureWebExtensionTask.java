package com.divae.ageto.hybris.install.task;

import java.io.File;

import com.divae.ageto.hybris.install.extensions.Extension;

/**
 * Created by mhaagen on 16.08.2016.
 */
public class RestructureWebExtensionTask extends RestructureExtensionTask {

    private final Extension extension;

    public RestructureWebExtensionTask(final Extension extension) {
        super(extension);
        this.extension = extension;
    }

    @Override
    protected void execute(TaskContext taskContext, File workDirectory) {
        super.execute(taskContext, workDirectory);
    }

    @Override
    protected File getResourcesDirectory(File hybrisDirectory, Extension extension) {
        return new File(hybrisDirectory, new File(extension.getBaseDirectory(), "webroot").toString());
    }

    @Override
    protected File getTestSourcesDirectory(File hybrisDirectory, Extension extension) {
        return super.getTestSourcesDirectory(hybrisDirectory, extension);
    }
}
