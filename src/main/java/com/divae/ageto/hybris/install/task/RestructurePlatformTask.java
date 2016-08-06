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
 * @author Marvin Haagen
 */
public class RestructurePlatformTask extends AbstractWorkDirectoryTask {

    @Override
    protected void execute(final TaskContext taskContext, final File workDirectory) {
        final List<InstallTask> tasks = Lists.newArrayList();

        tasks.add(new CopyFilesTask("core", "ext/core"));

        for (Path path : HybrisFakeStructure.getFiles()) {
            tasks.add(new CopyFilesTask("bin/platform/" + path, new File(path.toString()).getParent()));
        }

        new TaskChainTask(tasks).execute(taskContext);
    }

}
