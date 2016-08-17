package com.divae.ageto.hybris.install.task.copy;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;

/**
 * @author Marvin Haagen
 */
public class CopyDirectoryFilesToDirectoryTask extends CopyFilesTask {

    private final FileFilter fileFilter;

    public CopyDirectoryFilesToDirectoryTask(final File source, final File target, final FileFilter fileFilter) {
        super(source, target);
        this.fileFilter = fileFilter;
    }

    public CopyDirectoryFilesToDirectoryTask(final File source, final File target) {
        super(source, target);
        this.fileFilter = FileFilterUtils.trueFileFilter();
    }

    @Override
    protected void doCopy(File source, File target) throws IOException {
        if (source.isFile()) {
            throw new IOException("Source path is not a directory.");
        }
        if (target.isFile()) {
            throw new IOException("Target path is not a directory.");
        }

        FileFilter fileF = ((File file) -> {
            return fileFilter.accept(file) && file.isFile();
        });

        Files.walkFileTree(source.toPath(), new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if (fileFilter.accept(file.toFile())) {
                    Path relativePath = source.toPath().relativize(file);
                    FileUtils.copyFile(file.toFile(), new File(target, relativePath.toString()));
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                return FileVisitResult.SKIP_SUBTREE;
            }
        });
    }

}
