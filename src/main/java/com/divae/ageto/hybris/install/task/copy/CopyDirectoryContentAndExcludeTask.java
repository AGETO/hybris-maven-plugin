package com.divae.ageto.hybris.install.task.copy;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import com.divae.ageto.hybris.utils.FileUtils;

/**
 * Created by mhaagen on 16.08.2016.
 */
public class CopyDirectoryContentAndExcludeTask extends CopyFilesTask {

    private final Set<File> pathsToExclude;

    public CopyDirectoryContentAndExcludeTask(final File source, final File target, final Set<File> pathsToExclude) {
        super(source, target);
        this.pathsToExclude = pathsToExclude;
    }

    @Override
    protected void doCopy(final File source, final File target) throws IOException {
        if (source.isFile()) {
            throw new IOException("Source path is not a directory.");
        }
        if (target.isFile()) {
            throw new IOException("Target path is not a directory.");
        }

        for (File file : source.listFiles()) {

            if (!pathsToExclude.contains(file)) {
                if (file.isDirectory()) {
                    FileUtils.makeDirectory(target);
                    doCopy(new File(source, file.getName().toString()), new File(target, file.getName().toString()));
                    continue;
                }
                org.apache.commons.io.FileUtils.copyFileToDirectory(file, target);
            }
        }
    }
}
