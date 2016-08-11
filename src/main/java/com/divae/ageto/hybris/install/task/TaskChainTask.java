package com.divae.ageto.hybris.install.task;

import java.util.List;

/**
 * @author Klaus Hauschild
 */
public class TaskChainTask implements InstallTask {

    private final String            chainName;
    private final List<InstallTask> tasks;

    public TaskChainTask(final String chainName, final List<InstallTask> tasks) {
        this.chainName = chainName;
        this.tasks = tasks;
    }

    @Override
    public void execute(final TaskContext taskContext) {
        for (final InstallTask installTask : tasks) {
            installTask.execute(taskContext);
        }
    }

    @Override
    public String toString() {
        return String.format("%s %s", chainName, tasks);
    }

}
