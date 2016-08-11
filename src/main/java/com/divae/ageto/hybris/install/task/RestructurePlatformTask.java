package com.divae.ageto.hybris.install.task;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import com.divae.ageto.hybris.install.extensions.Extension;
import com.divae.ageto.hybris.install.task.copy.CopyDirectoryContentToDirectoryTask;
import com.divae.ageto.hybris.install.task.copy.CopyFileToDirectoryTask;
import com.divae.ageto.hybris.utils.EnvironmentUtils;
import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

/**
 * @author Marvin Haagen
 */
public class RestructurePlatformTask extends AbstractWorkDirectoryTask {

    private final List<Extension> extensions;

    public RestructurePlatformTask(final List<Extension> extensions) {
        this.extensions = extensions;
    }

    @Override
    protected void execute(final TaskContext taskContext, final File workDirectory) {
        final File resourcesDirectory = new File("src/main/resources");
        File hybrisInstallationDirectory = EnvironmentUtils.getHybrisInstallationDirectory();
        new TaskChainTask("restructure platform",
                Arrays.<InstallTask>asList( //
                        new CreatePomFromTemplateTask("com/divae/ageto/hybris/install/platform.pom.xml", "",
                                getExtensionNames(extensions)), //
                        new CopyFileToDirectoryTask(new File(hybrisInstallationDirectory + "/bin/platform/project.properties"),
                                resourcesDirectory), //
                        new CopyDirectoryContentToDirectoryTask(new File(hybrisInstallationDirectory + "/bin/platform/resources"),
                                resourcesDirectory), //
                        new CopyDirectoryContentToDirectoryTask(
                                new File(hybrisInstallationDirectory + "/bin/platform/bootstrap/resources"),
                                new File(resourcesDirectory + "/bootstrap")), //
                        new CreatePomFromTemplateTask("com/divae/ageto/hybris/install/models.pom.xml", "models", null) //
                )).execute(taskContext);
    }

    private List<String> getExtensionNames(final List<Extension> extensions) {
        return Lists.newArrayList(Iterables.transform(extensions, new Function<Extension, String>() {

            @Override
            public String apply(final Extension input) {
                return input.getName();
            }

        }));
    }

}
