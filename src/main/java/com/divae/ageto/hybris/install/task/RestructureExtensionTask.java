package com.divae.ageto.hybris.install.task;

import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.divae.ageto.hybris.install.extensions.Extension;
import com.divae.ageto.hybris.install.extensions.binary.ClassFolder;
import com.divae.ageto.hybris.install.extensions.binary.JARArchive;
import com.divae.ageto.hybris.install.task.copy.CopyDirectoryContentToDirectoryTask;
import com.divae.ageto.hybris.install.task.copy.CopyDirectoryFilesToDirectoryTask;
import com.divae.ageto.hybris.install.task.metadata.CreateExtensionMetadataFileTask;
import com.google.common.collect.Lists;

/**
 * @author Klaus Hauschild
 */
public class RestructureExtensionTask extends AbstractWorkDirectoryTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestructureExtensionTask.class);

    private final Extension     extension;

    public RestructureExtensionTask(final Extension extension) {
        this.extension = extension;
    }

    @Override
    protected void execute(final TaskContext taskContext, final File workDirectory) {

        LOGGER.debug(String.format("Restructure extension %s", extension.getName()));

        final File extensionDirectory = extension.getExtensionDirectory();
        final File sourcesDirectory = extension.getSourcesDirectory();
        final File testSourcesDirectory = extension.getTestSourcesDirectory();
        final File resourcesDirectory = extension.getResourcesDirectory();

        final Path hybrisDirectory = taskContext.getHybrisDirectory().toPath();

        List<InstallTask> installTasks = Lists.newArrayList();
        installTasks.addAll(Arrays.<InstallTask>asList( //
                new CreatePomFromExtensionTask(extension), //
                new CreateDirectoryTask(sourcesDirectory), //
                new CreateDirectoryTask(resourcesDirectory)) //
        );

        copyBinary(extension, resourcesDirectory, hybrisDirectory, installTasks);

        installTasks.addAll(Arrays.<InstallTask>asList(
                new CopyDirectoryFilesToDirectoryTask(
                        getResourcesDirectory(hybrisDirectory.toFile(), extension),
                        resourcesDirectory), //
                new CopyDirectoryContentToDirectoryTask(
                        getSourcesDirectory(hybrisDirectory.toFile(), extension),
                        resourcesDirectory) //
        ));

        if (getTestSourcesDirectory(hybrisDirectory.toFile(), extension).exists()) {
            installTasks.add(new CopyDirectoryContentToDirectoryTask(
                    getTestSourcesDirectory(hybrisDirectory.toFile(), extension),
                    testSourcesDirectory));
        }

        installTasks.add(new CreateExtensionMetadataFileTask(extension));

        if (new File(extension.getBaseDirectory(), "web").exists()) {
            Extension ext = new Extension(new File(extension.getBaseDirectory(), "web"), extension.getName() + "-web");
            installTasks.add(new RestructureWebExtensionTask(ext));
        }

        new TaskChainTask("restructure extension", installTasks).execute(taskContext);
    }

    private File getResourcesDirectory(final File hybrisDirectory, final Extension extension) {
        return new File(String.format("%s/%s", hybrisDirectory, extension.getBaseDirectory()));
    }

    private File getSourcesDirectory(final File hybrisDirectory, final Extension extension) {
        return new File(String.format("%s/%s/resources", hybrisDirectory, extension.getBaseDirectory()));
    }

    private File getTestSourcesDirectory(final File hybrisDirectory, final Extension extension) {
        return new File(String.format("%s/%s/testsrc", hybrisDirectory, extension.getBaseDirectory()));
    }

    private void copyBinary(final Extension extension, final File resourcesDirectory, final Path hybrisDirectory,
            final List<InstallTask> installTasks) {
        if (extension.getBinary().getClass() == JARArchive.class) {
            Path sourceFile = extension.getBinary().getExtensionBinaryPath().toPath();
            installTasks.add(new ExtractZipTask(hybrisDirectory.relativize(sourceFile).toFile(), resourcesDirectory));
        }
        if (extension.getBinary().getClass() == ClassFolder.class) {
            installTasks.add(
                    new CopyDirectoryContentToDirectoryTask(extension.getBinary().getExtensionBinaryPath(), resourcesDirectory));
        }
    }
}
