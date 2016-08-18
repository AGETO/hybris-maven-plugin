package com.divae.ageto.hybris.install.task;

import java.io.File;
import java.io.FileFilter;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.filefilter.FileFilterUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.divae.ageto.hybris.install.extensions.Extension;
import com.divae.ageto.hybris.install.extensions.ExtensionFactory;
import com.divae.ageto.hybris.install.extensions.WebExtension;
import com.divae.ageto.hybris.install.extensions.binary.ClassFolder;
import com.divae.ageto.hybris.install.extensions.binary.ExtensionBinary;
import com.divae.ageto.hybris.install.extensions.binary.JARArchive;
import com.divae.ageto.hybris.install.extensions.binary.None;
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
        LOGGER.info(String.format("Restructure extension %s", extension.getName()));

        final Path hybrisDirectory = taskContext.getHybrisDirectory().toPath();

        final List<InstallTask> extensionTasks = Lists.newArrayList();
        extensionTasks.addAll(Arrays.<InstallTask>asList( //
                new CreatePomFromExtensionTask(extension), //
                new CreateDirectoryTask(extension.getSourcesDirectory()), //
                new CreateDirectoryTask(extension.getResourcesDirectory())) //
        );

        addCopyRootExtensionFolder(extensionTasks, hybrisDirectory, extension, workDirectory);
        decompileOrCopyBinary(extension, hybrisDirectory, extensionTasks, taskContext);

        if (getTestSourcesDirectory(hybrisDirectory.toFile(), extension).exists()) {
            extensionTasks.add(new CopyDirectoryContentToDirectoryTask(
                    getTestSourcesDirectory(hybrisDirectory.toFile(), extension), extension.getTestSourcesDirectory()));
        }

        extensionTasks.add(new CreateExtensionMetadataFileTask(extension));

        if (new File(hybrisDirectory.toFile(), new File(extension.getBaseDirectory(), "web").toString()).exists()) {
            final ExtensionBinary binary = findBinary(hybrisDirectory.toFile(), extension);
            final WebExtension webExtension = new WebExtension(new File(extension.getBaseDirectory(), "web"),
                    extension.getName() + "-web", binary, Collections.singletonList(extension));
            RestructurePlatformTask.addAdditionalExtensionToModules(taskContext, webExtension);
            extensionTasks.add(new RestructureWebExtensionTask(webExtension));
        }

        new TaskChainTask("restructure extension", extensionTasks).execute(taskContext);
    }

    private void decompileOrCopyBinary(final Extension extension, final Path hybrisDirectory,
            final List<InstallTask> installTasks, final TaskContext taskContext) {
        if (extension.getBinary().getClass() != None.class) {
            if (DecompileTask.isEnabled(taskContext) && extension.getBinary().getClass() != ClassFolder.class) {
                final File extensionBinaryPath = extension.getBinary().getExtensionBinaryPath();
                installTasks.addAll(Arrays.asList( //
                        new DecompileTask(extensionBinaryPath, extension.getSourcesDirectory()), //
                        new MoveTestSourcesTask(extension.getSourcesDirectory(), extension.getTestSourcesDirectory()) //
                ));
                return;
            }
            this.copyBinary(extension, extension.getResourcesDirectory(), hybrisDirectory, installTasks);
        }
    }

    private void addCopyRootExtensionFolder(final List<InstallTask> installTasks, final Path hybrisDirectory,
            final Extension extension, final File workdirectory) {
        installTasks.addAll(Arrays.<InstallTask>asList(
                new CopyDirectoryFilesToDirectoryTask(this.getBaseDirectory(hybrisDirectory.toFile(), extension),
                        extension.getResourcesDirectory(), getFileFilter(workdirectory, hybrisDirectory.toFile())), //
                new CopyDirectoryContentToDirectoryTask(getResourcesDirectory(hybrisDirectory.toFile(), extension),
                        extension.getResourcesDirectory(), getFileFilter(workdirectory, hybrisDirectory.toFile()))
        ));
    }

    protected FileFilter getFileFilter(final File workDirectory, final File hybrisDirectory) {
        return FileFilterUtils.trueFileFilter();
    }

    private ExtensionBinary findBinary(final File hybrisDirectory, final Extension extension) {
        return ExtensionFactory.getBinary(extension.getName(),
                Collections.singletonMap(extension.getName(),
                        new File(extension.getBaseDirectory(), "web/webroot/WEB-INF")),
                hybrisDirectory);
    }

    private File getBaseDirectory(final File hybrisDirectory, final Extension extension) {
        return new File(String.format("%s/%s", hybrisDirectory, extension.getBaseDirectory()));
    }

    protected File getResourcesDirectory(final File hybrisDirectory, final Extension extension) {
        return new File(String.format("%s/%s/resources", hybrisDirectory, extension.getBaseDirectory()));
    }

    protected File getTestSourcesDirectory(final File hybrisDirectory, final Extension extension) {
        return new File(String.format("%s/%s/testsrc", hybrisDirectory, extension.getBaseDirectory()));
    }

    protected void copyBinary(final Extension extension, final File resourcesDirectory, final Path hybrisDirectory,
            final List<InstallTask> installTasks) {
        if (extension.getBinary().getClass() == JARArchive.class) {
            Path sourceFile = extension.getBinary().getExtensionBinaryPath().toPath();
            installTasks.add(new ExtractZipTask(sourceFile.toFile(), resourcesDirectory));
        }
        if (extension.getBinary().getClass() == ClassFolder.class) {
            if (this.getClass() == RestructureWebExtensionTask.class) {

                final File extensionBinaryPath = new File(hybrisDirectory.toFile(),
                        extension.getBinary().getExtensionBinaryPath().toString());
                installTasks.add(new CopyDirectoryContentToDirectoryTask(extensionBinaryPath, resourcesDirectory));
                return;
            }
            final File extensionBinaryPath = new File(hybrisDirectory.toFile(),
                    extension.getBinary().getExtensionBinaryPath().toString());
            installTasks.add(new CopyDirectoryContentToDirectoryTask(extensionBinaryPath, resourcesDirectory));
        }
    }

}
