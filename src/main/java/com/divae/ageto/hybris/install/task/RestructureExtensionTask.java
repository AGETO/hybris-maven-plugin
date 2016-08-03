package com.divae.ageto.hybris.install.task;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;

/**
 * @author Klaus Hauschild
 */
public class RestructureExtensionTask extends AbstractWorkDirectoryTask {

    private final String extensionDirectory;
    private final String extensionName;
    private final String binary;

    public RestructureExtensionTask(final String extensionDirectory, final String extensionName, final String binary) {
        this.extensionDirectory = extensionDirectory;
        this.extensionName = extensionName;
        this.binary = binary;
    }

    @Override
    protected void execute(final TaskContext taskContext, final File workDirectory) {
        final String sourcesDirectory = String.format("%s/src/main/java", extensionName);
        final String testSourcesDirectory = String.format("%s/src/test/java", extensionName);
        final String resourcesDirectory = String.format("%s/src/main/resources", extensionName);
        new TaskChainTask(Arrays.<InstallTask>asList( //
                new CreatePomTask(String.format("com/divae/ageto/hybris/install/%s.pom.xml", extensionName), extensionName,
                        Collections.<String, String>emptyMap()), //
                new CreateDirectoryTask(sourcesDirectory), //
                new CreateDirectoryTask(resourcesDirectory), //
                new DecompileTask(String.format("bin/platform/%s/bin/%s", extensionDirectory, binary), sourcesDirectory), //
                new CopyFilesTask(String.format("bin/platform/%s", extensionDirectory), resourcesDirectory), //
                new CopyFilesTask(String.format("bin/platform/%s/resources", extensionDirectory), resourcesDirectory), //
                new CopyFilesTask(String.format("bin/platform/%s/testsrc", extensionDirectory), testSourcesDirectory),
                new MoveTestSourcesTask(sourcesDirectory, testSourcesDirectory) //
        )).execute(taskContext);
    }

}
