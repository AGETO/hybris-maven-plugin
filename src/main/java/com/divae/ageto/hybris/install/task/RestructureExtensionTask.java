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

        final Path hybrisDirectory = taskContext.getHybrisDirectory().toPath();

        List<InstallTask> installTasks = Lists.newArrayList();
        installTasks.addAll(Arrays.<InstallTask>asList( //
                new CreatePomFromExtensionTask(extension), //
                new CreateDirectoryTask(extension.getSourcesDirectory()), //
                new CreateDirectoryTask(extension.getResourcesDirectory())) //
        );

        this.copyBinary(extension, extension.getResourcesDirectory(), hybrisDirectory, installTasks);
        addCopyRootExtensionFolder(installTasks, hybrisDirectory, extension, workDirectory);

        if (getTestSourcesDirectory(hybrisDirectory.toFile(), extension).exists()) {
            installTasks.add(new CopyDirectoryContentToDirectoryTask(
                    getTestSourcesDirectory(hybrisDirectory.toFile(), extension),
                    extension.getTestSourcesDirectory()));
        }

        installTasks.add(new CreateExtensionMetadataFileTask(extension));

        if (new File(hybrisDirectory.toFile(), new File(extension.getBaseDirectory(), "web").toString()).exists()) {
            ExtensionBinary binary = findBinary(hybrisDirectory.toFile(), extension);
            WebExtension ext = new WebExtension(new File(extension.getBaseDirectory(), "web"), extension.getName() + "-web",
                    binary, Collections.singletonList(extension));
            installTasks.add(new RestructureWebExtensionTask(ext));
        }

        new TaskChainTask("restructure extension", installTasks).execute(taskContext);
    }

    protected void addCopyRootExtensionFolder(final List<InstallTask> installTasks, final Path hybrisDirectory,
            final Extension extension, final File workdirectory) {

        installTasks.addAll(Arrays.<InstallTask>asList(
                new CopyDirectoryFilesToDirectoryTask(this.getBaseDirectory(hybrisDirectory.toFile(), extension),
                        extension.getResourcesDirectory(), getFileFilter(workdirectory, hybrisDirectory.toFile())), //
                new CopyDirectoryContentToDirectoryTask(getResourcesDirectory(hybrisDirectory.toFile(), extension),
                        extension.getResourcesDirectory(), getFileFilter(workdirectory, hybrisDirectory.toFile())) //
        ));
    }

    protected FileFilter getFileFilter(final File workDirectory, final File hybrisDirectory) {
        return FileFilterUtils.trueFileFilter();
    }

    private ExtensionBinary findBinary(final File hybrisDirectory, final Extension extension) {
        return ExtensionFactory.getBinary(extension.getName(),
                Collections.singletonMap(extension.getName(),
                        new File(hybrisDirectory, new File(extension.getBaseDirectory(), "web/webroot/WEB-INF").toString())));
    }

    protected File getBaseDirectory(final File hybrisDirectory, final Extension extension) {
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
            installTasks.add(new ExtractZipTask(hybrisDirectory.relativize(sourceFile).toFile(), resourcesDirectory));
        }
        if (extension.getBinary().getClass() == ClassFolder.class) {
            if (this.getClass() == RestructureWebExtensionTask.class) {

                installTasks.add(new CopyDirectoryContentToDirectoryTask(extension.getBinary().getExtensionBinaryPath(),
                        resourcesDirectory));
                return;
            }
            installTasks.add(
                    new CopyDirectoryContentToDirectoryTask(extension.getBinary().getExtensionBinaryPath(), resourcesDirectory));
        }
    }
}
