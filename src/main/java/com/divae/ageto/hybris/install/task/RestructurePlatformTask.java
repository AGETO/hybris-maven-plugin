package com.divae.ageto.hybris.install.task;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import com.divae.ageto.hybris.install.extensions.Extension;
import com.divae.ageto.hybris.install.task.copy.CopyDirectoryContentToDirectoryTask;
import com.divae.ageto.hybris.install.task.copy.CopyFileToDirectoryTask;
import com.divae.ageto.hybris.utils.EnvironmentUtils;
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
                Arrays.asList( //
                        new CreatePomFromTemplateTask(new File("com/divae/ageto/hybris/install/platform.pom.xml"), new File(""),
                                getExtensionNames(extensions)), //
                        new CopyFileToDirectoryTask(new File(hybrisInstallationDirectory + "/bin/platform/project.properties"),
                                resourcesDirectory), //
                        new CopyDirectoryContentToDirectoryTask(new File(hybrisInstallationDirectory + "/bin/platform/resources"),
                                resourcesDirectory), //
                        new CopyDirectoryContentToDirectoryTask(
                                new File(hybrisInstallationDirectory + "/bin/platform/bootstrap/resources"),
                                new File(resourcesDirectory + "/bootstrap")), //
                        new CreatePomFromTemplateTask(new File("com/divae/ageto/hybris/install/models.pom.xml"),
                                new File("models"), null) //
                )).execute(taskContext);
    }

    private List<String> getExtensionNames(final List<Extension> extensions) {
        return Lists.newArrayList(Iterables.transform(extensions, (Extension input) -> input.getName()));
    }

}
