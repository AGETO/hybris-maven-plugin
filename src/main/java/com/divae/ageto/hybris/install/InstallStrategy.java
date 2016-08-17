package com.divae.ageto.hybris.install;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.maven.model.Dependency;

import com.divae.ageto.hybris.install.extensions.Extension;
import com.divae.ageto.hybris.install.task.AbstractWorkDirectoryTask;
import com.divae.ageto.hybris.install.task.CleanupTask;
import com.divae.ageto.hybris.install.task.CreateWorkDirectoryTask;
import com.divae.ageto.hybris.install.task.ExecuteMavenTask;
import com.divae.ageto.hybris.install.task.InstallTask;
import com.divae.ageto.hybris.install.task.OpenWorkDirectoryInExplorerTask;
import com.divae.ageto.hybris.install.task.RestructureExtensionTask;
import com.divae.ageto.hybris.install.task.RestructurePlatformTask;
import com.divae.ageto.hybris.install.task.TaskContext;
import com.divae.ageto.hybris.utils.maven.MavenExecutorUtils;
import com.divae.ageto.hybris.version.HybrisVersion;
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

        installTasks.addAll(extensions.stream().map(RestructureExtensionTask::new).collect(Collectors.toList()));

        installTasks.addAll(Arrays.asList( //

                // platform
                new RestructurePlatformTask(extensions),

                // prepare code generator
                new AbstractWorkDirectoryTask() {

                    @Override
                    protected void execute(final TaskContext taskContext, final File workDirectory) {
                        MavenExecutorUtils.installLibrary(
                                new File(taskContext.getHybrisDirectory(), "bin/platform/bootstrap/bin/ybootstrap.jar"),
                                getBootstrapDependency(taskContext.getHybrisVersion()));
                    }

                },

                // install
                new ExecuteMavenTask(new File(""), new String[] { "clean", "install" }), //

                // cleanup
                new CleanupTask() //
        ));

        return installTasks;
    }

    private static Dependency getBootstrapDependency(final HybrisVersion hybrisVersion) {
        final Dependency dependency = new Dependency();
        dependency.setGroupId("de.hybris");
        dependency.setArtifactId("bootstrap");
        dependency.setVersion(hybrisVersion.getVersion());
        return dependency;
    }

}
