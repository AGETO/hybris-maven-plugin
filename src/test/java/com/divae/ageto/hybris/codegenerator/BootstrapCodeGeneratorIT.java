package com.divae.ageto.hybris.codegenerator;

import com.divae.ageto.hybris.utils.EnvironmentUtils;
import de.hybris.bootstrap.codegenerator.CodeGenerator;

import org.testng.annotations.Test;

/**
 * @author Klaus Hauschild
 */
public class BootstrapCodeGeneratorIT {

    @Test
    public void generateCodeTest() {
        CodeGenerator
                .main(new String[] { "C:\\Users\\mhaagen\\AppData\\Local\\Temp\\1458829098347-0\\hybris\\bin\\platform" });
                //.main(new String[] { EnvironmentUtils.getHybrisInstallationDirectory().getAbsolutePath() + "/bin/platform" });


    }

}
