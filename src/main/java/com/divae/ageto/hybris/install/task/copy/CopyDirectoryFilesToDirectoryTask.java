package com.divae.ageto.hybris.install.task.copy;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFileFilter;

import java.io.File;
import java.io.IOException;

/**
 * @author Marvin Haagen
 */
public class CopyDirectoryFilesToDirectoryTask extends CopyFilesTask {

    public CopyDirectoryFilesToDirectoryTask(final File source, final File target) {
        super(source, target);
    }

    @Override
    protected void doCopy(File source, File target) throws IOException {
        if (source.isFile()) {
            throw new IOException("Source path is not a directory.");
        }
        if (target.isFile()) {
            throw new IOException("Target path is not a directory.");
        }
        FileUtils.copyDirectory(source, target, FileFileFilter.FILE);
    }

}
