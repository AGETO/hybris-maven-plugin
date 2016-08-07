package com.divae.ageto.hybris.codegenerator;

import java.io.File;
import java.lang.reflect.Method;

import com.google.common.base.Throwables;

/**
 * @author Klaus Hauschild
 */
class CodeGenerator {

    static void generate(final File hybrisReactorDir) {
        final File hybrisFakeDirectory = HybrisFakeStructure.generate(hybrisReactorDir);
        invokeBootstrapCodeGenerator(hybrisFakeDirectory.toString());
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
