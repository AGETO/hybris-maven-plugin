package com.divae.ageto.hybris.install.task;

import java.io.File;
import java.io.FileFilter;
import java.nio.file.Path;
import java.util.List;

import com.divae.ageto.hybris.install.extensions.Extension;
import com.divae.ageto.hybris.install.extensions.WebExtension;

/**
 * @author Klaus Hauschild
 */
class RestructureWebExtensionTask extends RestructureExtensionTask {

    private final Extension extension;

    RestructureWebExtensionTask(final Extension extension) {
        super(extension);
        this.extension = extension;
    }

    @Override
    protected void execute(final TaskContext taskContext, final File workDirectory) {
        super.execute(taskContext, workDirectory);
    }

    @Override
    protected File getResourcesDirectory(final File hybrisDirectory, final Extension extension) {
        return new File(hybrisDirectory, new File(extension.getBaseDirectory(), "webroot").toString());
    }

    @Override
    protected File getTestSourcesDirectory(final File hybrisDirectory, final Extension extension) {
        return super.getTestSourcesDirectory(hybrisDirectory, extension);
    }

    @Override
    protected void copyBinary(final Extension extension, final File resourcesDirectory, final Path hybrisDirectory,
            final List<InstallTask> installTasks) {
        WebExtension webExtension = (WebExtension) extension;
        super.copyBinary(extension, webExtension.getWebResourcesFolder(), hybrisDirectory, installTasks);
    }

    @Override
    protected FileFilter getFileFilter(final File workDirectory, final File hybrisDirectory) {
        final File exclude = extension.getBinary().getExtensionBinaryPath();
        return (File file) -> !file.toPath().startsWith(exclude.toPath());
    }

}
