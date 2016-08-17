package com.divae.ageto.hybris.install.extensions;

import java.io.File;
import java.util.List;

import org.apache.commons.configuration2.XMLConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Throwables;
import com.google.common.collect.Lists;

/**
 * @author Marvin Haagen
 */
public enum Extensions {

    ;

    private static final Logger LOGGER = LoggerFactory.getLogger(Extensions.class);

    public static List<String>  EXTENSION_NAMES;

    static List<String> getExtensionNames(final File hybrisInstallDirectory) {
        if (EXTENSION_NAMES != null) {
            LOGGER.warn("External configured extension names used!");
            return EXTENSION_NAMES;
        }

        try {
            final File hybrisPlatformDirectory = new File(hybrisInstallDirectory, "bin/platform");
            final File platformExtensionsFile = new File(hybrisPlatformDirectory, "extensions.xml");
            final FileBasedConfigurationBuilder<XMLConfiguration> builder = new FileBasedConfigurationBuilder<>(
                    XMLConfiguration.class).configure(new Parameters().xml().setFile(platformExtensionsFile));
            final XMLConfiguration platformExtensionsConfiguration = builder.getConfiguration();
            final List<String> extensionNames = Lists
                    .newArrayList(platformExtensionsConfiguration.getList(String.class, "extensions.extension[@name]"));
            extensionNames.add("hac");
            return extensionNames;
        } catch (final Exception exception) {
            throw Throwables.propagate(exception);
        }
    }

}
