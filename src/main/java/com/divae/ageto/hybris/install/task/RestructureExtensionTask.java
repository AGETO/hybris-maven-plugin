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

        final File extensionDirectory = new File(extension.getName());
        final File sourcesDirectory = new File(extensionDirectory, "src/main/java");
        final File testSourcesDirectory = new File(extensionDirectory, "src/test/java");
        final File resourcesDirectory = new File(extensionDirectory, "src/main/resources");

        List<InstallTask> installTasks = Lists.newArrayList();
        installTasks.addAll(Arrays.<InstallTask>asList( //
                new CreatePomFromExtensionTask(extension), //
                new CreateDirectoryTask(sourcesDirectory.toString()), //
                new CreateDirectoryTask(resourcesDirectory.toString())) //
        );

        if (extension.getBinary().getClass() == JARArchive.class) {
            Path hybrisDirectory = taskContext.getHybrisDirectory().toPath();
            installTasks.add(new ExtractZipTask(
                    hybrisDirectory.relativize(extension.getBinary().getExtensionBinaryPath().toPath()).toString(),
                    resourcesDirectory.toString()));
        }
        if (extension.getBinary().getClass() == ClassFolder.class) {
            installTasks.add(
                    new CopyDirectoryContentToDirectoryTask(extension.getBinary().getExtensionBinaryPath(), resourcesDirectory));
        }

        installTasks.addAll(Arrays.<InstallTask>asList(
                new CopyDirectoryFilesToDirectoryTask(new File(String.format("%s", extension.getBaseDirectory())),
                        resourcesDirectory), //
                new CopyDirectoryContentToDirectoryTask(new File(String.format("%s/resources", extension.getBaseDirectory())),
                        resourcesDirectory)) //
        );

        new TaskChainTask("restructure extension", installTasks).execute(taskContext);
    }
}
