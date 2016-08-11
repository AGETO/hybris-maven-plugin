package com.divae.ageto.hybris.codegenerator;

import java.util.List;

import org.testng.annotations.Test;

import com.divae.ageto.hybris.install.extensions.Extension;

/**
 * @author Marvin Haagen
 */
public class HybrisFakeStructureTest {

    @Test
    public void testReadExtensionList() throws Exception {
        List<Extension> extensionList = HybrisFakeStructure.readExtensionList(
                ClassLoader.getSystemResourceAsStream("com/divae/ageto/hybris/install/platform.extensions.xml"));
    }

}
