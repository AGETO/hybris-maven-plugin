package com.divae.ageto.hybris.install.extensions;

import java.io.File;
import java.util.List;

import org.apache.commons.configuration2.XMLConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;

import com.google.common.base.Throwables;

/**
 * Created by mhaagen on 11.08.2016.
 */
enum Extensions {

    ;

    static List<String> getExtensionNames(final File hybrisInstallDirectory) {
        try {
            final File hybrisPlatformDirectory = new File(hybrisInstallDirectory, "bin/platform");
            final File platformExtensionsFile = new File(hybrisPlatformDirectory, "extensions.xml");
            final FileBasedConfigurationBuilder<XMLConfiguration> builder = new FileBasedConfigurationBuilder<>(
                    XMLConfiguration.class).configure(new Parameters().xml().setFile(platformExtensionsFile));
            final XMLConfiguration platformExtensionsConfiguration = builder.getConfiguration();
            return platformExtensionsConfiguration.getList(String.class, "extensions.extension[@name]");
        } catch (final Exception exception) {
            throw Throwables.propagate(exception);
        }
    }

}
