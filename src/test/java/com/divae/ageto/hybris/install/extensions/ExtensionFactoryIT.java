package com.divae.ageto.hybris.install.extensions;

import static org.testng.Assert.assertEquals;

import java.io.File;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import com.divae.ageto.hybris.utils.EnvironmentUtils;
import com.google.common.collect.Sets;

/**
 * @author Marvin Haagen
 */
public class ExtensionFactoryIT {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExtensionFactoryIT.class);

    @Test
    public void getExtensionsFromPlatformTest() {
        final File hybrisInstallationDirectory = EnvironmentUtils.getHybrisInstallationDirectory();
        final List<Extension> extensions = ExtensionFactory.getExtensions(hybrisInstallationDirectory);
        assertEquals(extensions.size(), 69);

        if (LOGGER.isDebugEnabled()) {
            for (final Extension extension : extensions) {
                printExtension(extension, "  ");
            }
        }

        final Set<Extension> allExtensions = Sets.newHashSet();
        for (final Extension extension : extensions) {
            collectExtension(extension, allExtensions);
        }
        assertEquals(allExtensions.size(), 89);
    }

    private void collectExtension(final Extension extension, final Set<Extension> allExtensions) {
        allExtensions.add(extension);
        for (Extension dependency : extension.getDependencies()) {
            collectExtension(dependency, allExtensions);
        }
    }

    private void printExtension(Extension extension, String indent) {
        LOGGER.debug(String.format("%s%s", indent, extension.getName()));
        for (Extension dependency : extension.getDependencies()) {
            printExtension(dependency, indent + "  ");
        }
    }

}
