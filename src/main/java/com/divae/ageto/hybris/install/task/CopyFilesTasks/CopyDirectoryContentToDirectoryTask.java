package com.divae.ageto.hybris.install.task.CopyFilesTasks;

import com.divae.ageto.hybris.install.task.CopyFilesTask;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by mhaagen on 09.08.2016.
 */
public class CopyDirectoryContentToDirectoryTask extends CopyFilesTask {

    public CopyDirectoryContentToDirectoryTask(final File source, final File target) {
        super(source, target);
    }

    @Override
    protected void doCopy(final File source, final File target) throws IOException {
        if (source.isFile()) {
            throw new IOException("Source path is not a directory.");
        }
        if (target.isFile()) {
            throw new IOException("Target path is not a directory.");
        }
        FileUtils.copyDirectory(source, target);
    }
}
