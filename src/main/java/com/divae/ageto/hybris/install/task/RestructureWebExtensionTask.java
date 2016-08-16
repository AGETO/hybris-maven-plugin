package com.divae.ageto.hybris.install.task;

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
}
