package com.divae.ageto.hybris.install.task;

import java.io.File;

import com.divae.ageto.hybris.install.extensions.Extension;

/**
 * Created by mhaagen on 16.08.2016.
 */
public class RestructureWebExtensionTask extends RestructureExtensionTask {

    private final File      sourceFolder;
    private final File      destinationFolder;
    private final Extension extension;

    public RestructureWebExtensionTask(File sourceFolder, File destinationFolder, Extension extension) {
        super(extension);
        this.sourceFolder = sourceFolder;
        this.destinationFolder = destinationFolder;
        this.extension = extension;
    }

}
