package com.divae.ageto.hybris.install.extensions;

import static org.testng.Assert.assertEquals;

import java.io.File;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import com.divae.ageto.hybris.utils.EnvironmentUtils;

/**
 * @author Marvin Haagen
 */
public class ExtensionFactoryIT {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExtensionFactoryIT.class);

    @Test
    public void getExtensionsFromPlatformTest() {
        final File hybrisInstallationDirectory = EnvironmentUtils.getHybrisInstallationDirectory();
        final List<Extension> extensions = ExtensionFactory.getExtensions(hybrisInstallationDirectory);
        assertEquals(extensions.size(), 70);

        if (LOGGER.isTraceEnabled()) {
            for (final Extension extension : extensions) {
                printExtension(extension, "  ");
            }
        }

        assertEquals(ExtensionFactory.getTransitiveExtensions(extensions).size(), 90);
    }

    private void printExtension(Extension extension, String indent) {
        LOGGER.trace(String.format("%s%s", indent, extension.getName()));
        for (Extension dependency : extension.getDependencies()) {
            printExtension(dependency, indent + "  ");
        }
    }

}
