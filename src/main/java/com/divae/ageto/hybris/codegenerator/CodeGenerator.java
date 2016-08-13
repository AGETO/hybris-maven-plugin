package com.divae.ageto.hybris.codegenerator;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;

import org.apache.commons.io.FileUtils;

import com.google.common.base.Throwables;

/**
 * @author Klaus Hauschild
 */
class CodeGenerator {

    static void generate(final File hybrisReactorDir) {
        final File hybrisFakeDirectory = HybrisFakeStructure.generate(hybrisReactorDir);
        invokeBootstrapCodeGenerator(hybrisFakeDirectory);
        createModelsArtifacts(hybrisFakeDirectory, hybrisReactorDir);
    }

    private static void createModelsArtifacts(final File hybrisFakeDirectory, final File hybrisReactorDir) {
        try {
            FileUtils.copyDirectory(new File(hybrisFakeDirectory, "bootstrap/gensrc"),
                    new File(hybrisReactorDir, "models/src/main/java"));
        } catch (IOException e) {
            throw new RuntimeException(
                    String.format("Unable to copy files from [%s] to [%s]", hybrisFakeDirectory, hybrisReactorDir), e);
        }
    }

    private static void invokeBootstrapCodeGenerator(final File hybrisFakeDirectory) {
        try {
            final Class<?> bootstrapCodeGeneratorClass = Class.forName("de.hybris.bootstrap.codegenerator.CodeGenerator");
            final Method mainMethod = bootstrapCodeGeneratorClass.getMethod("main", String[].class);
            mainMethod.invoke(null, new Object[] { new String[] { hybrisFakeDirectory.toString() } });
        } catch (final Exception exception) {
            throw Throwables.propagate(exception);
        }
    }

}
