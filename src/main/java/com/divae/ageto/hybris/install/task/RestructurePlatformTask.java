package com.divae.ageto.hybris.install.task;

import com.divae.ageto.hybris.codegenerator.HybrisFakeStructure;
import com.google.common.collect.Lists;
import de.hybris.bootstrap.codegenerator.CodeGenerator;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 * Created by mhaagen on 05.08.2016.
 */
public class RestructurePlatformTask extends AbstractWorkDirectoryTask {

    @Override
    protected void execute(TaskContext taskContext, File workDirectory) {
        List<InstallTask> tasks = Lists.newArrayList();

        tasks.add(new CopyFilesTask("core", "ext/core"));

        for (Path path : HybrisFakeStructure.getFiles()) {
            tasks.add(new CopyFilesTask("bin/platform/" + path, new File(path.toString()).getParent()));
        }

        new TaskChainTask(tasks).execute(taskContext);
    }

}
