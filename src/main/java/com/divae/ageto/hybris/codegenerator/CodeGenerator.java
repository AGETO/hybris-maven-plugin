package com.divae.ageto.hybris.codegenerator;

import java.io.File;

/**
 * @author Klaus Hauschild
 */
class CodeGenerator {

    static void generate(final File hybrisReactorDir) {
        HybrisFakeStructure.generate(hybrisReactorDir);
        de.hybris.bootstrap.codegenerator.CodeGenerator.main(new String[] { hybrisReactorDir.toString() });
    }

}
