package com.divae.ageto.hybris.codegenerator;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.divae.ageto.hybris.install.task.CopyFilesTasks.CopyDirectoryContentToDirectoryTask;
import com.divae.ageto.hybris.utils.EnvironmentUtils;
import com.divae.ageto.hybris.utils.PomCreator;
import com.divae.ageto.hybris.version.HybrisVersion;
import com.google.common.base.Throwables;
import org.apache.commons.io.FileUtils;

/**
 * @author Klaus Hauschild
 */
class CodeGenerator {

    static void generate(final File hybrisReactorDir) {
        final File hybrisFakeDirectory = HybrisFakeStructure.generate(hybrisReactorDir);
        invokeBootstrapCodeGenerator(hybrisFakeDirectory.toString());
        createModelsArtifacts(hybrisFakeDirectory, hybrisReactorDir);
        createPom(hybrisFakeDirectory, hybrisReactorDir,
                HybrisVersion.of(EnvironmentUtils.getHybrisInstallationDirectory()).getVersion());
    }

    private static void createPom(final File hybrisFakeDirectory, final File hybrisReactorDir, String hybrisVersion) {
        PomCreator.create(hybrisVersion, new HashMap<String, String>(), "com/divae/ageto/hybris/install/models.pom.xml",
                hybrisReactorDir, "target/models");
    }

    private static void createModelsArtifacts(final File hybrisFakeDirectory, final File hybrisReactorDir) {
        try {
            FileUtils.copyDirectory(new File(hybrisFakeDirectory, "bootstrap/gensrc"),
                    new File(hybrisReactorDir, "target/models/src/main/java"));
        } catch (IOException e) {
            throw new RuntimeException(
                    String.format("Unable to copy files from [%s] to [%s]", hybrisFakeDirectory, hybrisReactorDir), e);
        }
    }

    private static void invokeBootstrapCodeGenerator(final String hybrisFakeDirectory) {
        try {
            final Class<?> bootstrapCodeGeneratorClass = Class.forName("de.hybris.bootstrap.codegenerator.CodeGenerator");
            final Method mainMethod = bootstrapCodeGeneratorClass.getMethod("main", String[].class);
            mainMethod.invoke(null, new Object[] { new String[] { hybrisFakeDirectory } });
        } catch (final Exception exception) {
            throw Throwables.propagate(exception);
        }
    }

}
