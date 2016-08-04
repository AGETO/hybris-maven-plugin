package com.divae.ageto.hybris.codegenerator;

import java.io.File;

import org.testng.annotations.Test;

/**
 * @author Klaus Hauschild
 */
public class CodeGeneratorIT {

    @Test
    public void generateTest() {
        File tempDirectory = new File(System.getProperty("java.io.tmpdir"));

        CodeGenerator.generate(new File(tempDirectory, "1458829098347-0"));
    }

}
