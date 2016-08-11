package com.divae.ageto.hybris.install.task;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.divae.ageto.hybris.install.extensions.Extension;
import com.divae.ageto.hybris.install.extensions.binary.ExtensionBinary;
import com.divae.ageto.hybris.install.task.copy.CopyDirectoryContentToDirectoryTask;
import com.divae.ageto.hybris.install.task.copy.CopyDirectoryFilesToDirectoryTask;
import com.google.common.collect.Lists;

/**
 * @author Klaus Hauschild
 */
public class RestructureExtensionTask extends AbstractWorkDirectoryTask {

    private final String          extensionDirectory;
    private final String          extensionName;
    private final ExtensionBinary binary;

    /*public RestructureExtensionTask(final String extensionDirectory, final String extensionName, final String binary) {
        this.extensionDirectory = extensionDirectory;
        this.extensionName = extensionName;
        this.binary = binary;
    }*/

    public RestructureExtensionTask(final Extension extension) {
        this.extensionDirectory = extension.getBaseDirectory().toString();
        this.extensionName = extension.getName();
        this.binary = extension.getBinary();
    }

    @Override
    protected void execute(final TaskContext taskContext, final File workDirectory) {
        final File sourcesDirectory = new File(String.format("%s/src/main/java", extensionName));
        final File testSourcesDirectory = new File(String.format("%s/src/test/java", extensionName));
        final File resourcesDirectory = new File(String.format("%s/src/main/resources", extensionName));

        List<InstallTask> installTasks = Lists.newArrayList();
        installTasks.addAll(Arrays.<InstallTask>asList( //
                new CreatePomTask(String.format("com/divae/ageto/hybris/install/%s.pom.xml", extensionName), extensionName,
                        Collections.<String, String>emptyMap()), //
                new CreateDirectoryTask(sourcesDirectory.toString()), //
                new CreateDirectoryTask(resourcesDirectory.toString()))); //

        // if (binary.getClass() == JARArchive.class) {
        // installTasks.add(new ExtractZipTask(
        // String.format("bin/platform/%s/bin/%s", extensionDirectory, binary.getExtensionBinaryPath().toString()),
        // resourcesDirectory.toString()));
        // }

        installTasks.addAll(Arrays.<InstallTask>asList(
                new CopyDirectoryFilesToDirectoryTask(new File(String.format("%s", extensionDirectory)), resourcesDirectory), //
                new CopyDirectoryContentToDirectoryTask(new File(String.format("%s/resources", extensionDirectory)),
                        resourcesDirectory)));

        new TaskChainTask(installTasks).execute(taskContext);
    }
}
