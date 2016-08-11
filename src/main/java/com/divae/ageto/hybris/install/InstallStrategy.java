package com.divae.ageto.hybris.install;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import com.divae.ageto.hybris.install.extensions.Extension;
import com.divae.ageto.hybris.install.task.CleanupTask;
import com.divae.ageto.hybris.install.task.CreateWorkDirectoryTask;
import com.divae.ageto.hybris.install.task.ExecuteMavenTask;
import com.divae.ageto.hybris.install.task.InstallTask;
import com.divae.ageto.hybris.install.task.OpenWorkDirectoryInExplorerTask;
import com.divae.ageto.hybris.install.task.RestructureExtensionTask;
import com.divae.ageto.hybris.install.task.RestructurePlatformTask;
import com.divae.ageto.hybris.install.task.TaskContext;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

/**
 * @author Klaus Hauschild
 */
enum InstallStrategy {

    ;

    static List<InstallTask> getInstallTasks(final TaskContext taskContext, final List<Extension> extensions) {
        Preconditions.checkArgument(!extensions.isEmpty(), "at least one extension is expected");

        if (!taskContext.getHybrisVersion().getVersion().startsWith("5")) {
            throw new IllegalStateException("Installation of hybris commerce suite is only supported for version 5.x.x.x!");
        }
        final List<InstallTask> installTasks = Lists.newArrayList();

        installTasks.addAll(Arrays.asList( //
                // TODO remove this for production
                new CreateWorkDirectoryTask(), //
                new OpenWorkDirectoryInExplorerTask() //
        ));

        for (final Extension extension : extensions) {
            installTasks.add(new RestructureExtensionTask(extension));
        }

        installTasks.addAll(Arrays.asList( //

                // platform
                new RestructurePlatformTask(extensions),

                // prepare code generator
                new ExecuteMavenTask("",
                        new String[] { "install:install-file",
                                String.format("-Dfile=%s",
                                        new File(taskContext.getHybrisDirectory(), "bin/platform/bootstrap/bin/ybootstrap.jar")),
                                "-DgroupId=de.hybris", "-DartifactId=bootstrap",
                                String.format("-Dversion=%s", taskContext.getHybrisVersion().getVersion()), "-Dpackaging=jar" },
                        true), //

                // install
                new ExecuteMavenTask("", new String[] { "clean", "install" }, true), //

                // cleanup
                new CleanupTask() //
        ));

        return installTasks;
    }

}
