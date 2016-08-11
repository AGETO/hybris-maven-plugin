package com.divae.ageto.hybris.install.task.copy;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

/**
 * @author Marvin Haagen
 */
public class CopyFileToDirectoryTask extends CopyFilesTask {

    public CopyFileToDirectoryTask(final File source, final File target) {
        super(source, target);
    }

    @Override
    protected void doCopy(final File source, final File target) throws IOException {
        if (source.isDirectory()) {
            throw new IOException("Source path is not a file.");
        }
        if (target.isFile()) {
            throw new IOException("Target path is not a directory.");
        }
        FileUtils.copyFileToDirectory(source, target);
    }

}
