package com.divae.ageto.hybris.install.task;

import java.util.List;

/**
 * @author Klaus Hauschild
 */
public class TaskChainTask implements InstallTask {

    private final List<InstallTask> installTasks;

    public TaskChainTask(final List<InstallTask> installTasks) {
        this.installTasks = installTasks;
    }

    @Override
    public void execute(final TaskContext taskContext) {
        for (final InstallTask installTask : installTasks) {
            installTask.execute(taskContext);
        }
    }

}
