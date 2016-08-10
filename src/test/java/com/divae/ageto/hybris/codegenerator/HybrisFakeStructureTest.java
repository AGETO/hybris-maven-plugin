package com.divae.ageto.hybris.codegenerator;

import com.divae.ageto.hybris.install.extensions.Extension;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import static org.testng.Assert.*;

/**
 * Created by mhaagen on 10.08.2016.
 */
public class HybrisFakeStructureTest {
    @Test
    public void testReadExtensionList() throws Exception {
        List<Extension> extensionList = HybrisFakeStructure.readExtensionList(
                new FileInputStream(new File("src/main/resources/com/divae/ageto/hybris/install/platform.extensions.xml")));

        assertTrue(extensionList.size() > 0);
    }

}
