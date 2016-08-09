package com.divae.ageto.hybris.install.task.CopyFilesTasks;

import com.divae.ageto.hybris.install.task.CopyFilesTask;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.FileFileFilter;

import java.io.File;
import java.io.IOException;

/**
 * Created by mhaagen on 09.08.2016.
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
