package com.divae.ageto.hybris.install;

import java.io.File;

import com.divae.ageto.hybris.install.extensions.ExtensionFactory;
import com.divae.ageto.hybris.install.task.TaskChainTask;
import com.divae.ageto.hybris.install.task.TaskContext;
import com.divae.ageto.hybris.version.HybrisVersion;

/**
 * @author Klaus Hauschild
 */
class InstallHybrisArtifacts {

    private final TaskContext   taskContext;
    private final TaskChainTask installTasks;

    InstallHybrisArtifacts(final File hybrisDirectory) {
        final HybrisVersion hybrisVersion = HybrisVersion.of(hybrisDirectory);
        taskContext = new TaskContext(hybrisVersion, hybrisDirectory);
        installTasks = new TaskChainTask(InstallStrategy.getInstallTasks(taskContext,
                ExtensionFactory.getTransitiveExtensions(ExtensionFactory.getExtensions(hybrisDirectory))));
    }

    public void execute() {
        installTasks.execute(taskContext);
    }

    public TaskContext getTaskContext() {
        return taskContext;
    }

}
