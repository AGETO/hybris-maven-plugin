package com.divae.ageto.hybris.install.task;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * @author Klaus Hauschild
 */
public class MoveTestSourcesTask extends AbstractWorkDirectoryTask {

    private final File fromDirectory;
    private final File toDirectory;

    public MoveTestSourcesTask(final File fromDirectory, final File toDirectory) {
        this.fromDirectory = fromDirectory;
        this.toDirectory = toDirectory;
    }

    @Override
    protected void execute(final TaskContext taskContext, final File workDirectory) {
        // TODO khauschild: prevent empty directories

        final File from = new File(workDirectory, fromDirectory.toString());
        final File to = new File(workDirectory, toDirectory.toString());

        try {
            Files.walkFileTree(from.toPath(), new SimpleFileVisitor<Path>() {

                @Override
                public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs) throws IOException {
                    Files.createDirectories(to.toPath().resolve(from.toPath().relativize(dir)));
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
                    if (file.endsWith("Test.java")) {
                        Files.copy(file, to.toPath().resolve(from.toPath().relativize(file)));
                        if (file.toFile().exists() && !file.toFile().delete()) {
                            throw new RuntimeException(
                                    String.format("file %s has been copied to %s, but source file at %s can not be deleted",
                                            file.getFileName(), to, file));
                        }
                    }
                    return FileVisitResult.CONTINUE;

                }
            });
        } catch (final Exception exception) {
            throw new RuntimeException(exception);
        }
    }

}
