package com.divae.ageto.hybris.install.task;

import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * @author Klaus Hauschild
 */
public class OpenWorkDirectoryInExplorerTask extends AbstractWorkDirectoryTask {

    @Override
    protected void execute(final TaskContext taskContext, final File workDirectory) {
        try {
            Desktop.getDesktop().open(workDirectory);
        } catch (final IOException exception) {
            throw new RuntimeException(exception);
        }
    }

}
