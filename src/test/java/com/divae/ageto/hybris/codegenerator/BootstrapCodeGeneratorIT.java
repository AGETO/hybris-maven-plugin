package com.divae.ageto.hybris.codegenerator;

import de.hybris.bootstrap.codegenerator.CodeGenerator;

import org.testng.annotations.Test;

import com.divae.ageto.hybris.Constants;

/**
 * @author Klaus Hauschild
 */
public class BootstrapCodeGeneratorIT {

    @Test
    public void generateCodeTest() {
        CodeGenerator.main(new String[] { Constants.HYBRIS_INSTALLATION_DIR + "/bin/platform" });
    }

}
