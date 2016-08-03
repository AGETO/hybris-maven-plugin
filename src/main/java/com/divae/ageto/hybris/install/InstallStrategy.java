package com.divae.ageto.hybris.install;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.divae.ageto.hybris.install.task.CreatePomTask;
import com.divae.ageto.hybris.install.task.CreateWorkDirectoryTask;
import com.divae.ageto.hybris.install.task.ExecuteMavenTask;
import com.divae.ageto.hybris.install.task.InstallTask;
import com.divae.ageto.hybris.install.task.OpenWorkDirectoryInExplorerTask;
import com.divae.ageto.hybris.install.task.RestructureExtensionTask;
import com.divae.ageto.hybris.install.task.TaskContext;

/**
 * @author Klaus Hauschild
 */
public enum InstallStrategy {

    ;

    static List<InstallTask> getInstallTasks(final TaskContext taskContext) {
        if (!taskContext.getHybrisVersion().getVersion().startsWith("5")) {
            throw new IllegalStateException("Installation of hybris commerce suite is only supported for version 5.x.x.x!");
        }
        return Arrays.asList( //
                new CreateWorkDirectoryTask(), //

                // core
                new RestructureExtensionTask("ext/core", "core", "coreserver.jar"), //

                // platform
                new CreatePomTask("com/divae/ageto/hybris/install/platform.pom.xml", "", Collections.<String, String>emptyMap()), //

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
                new OpenWorkDirectoryInExplorerTask() //
        // TODO new CleanupTask()
        );
    }

}
