package com.divae.ageto.hybris.install.extensions;

import java.io.File;
import java.util.Collections;
import java.util.List;

import org.apache.commons.configuration2.XMLConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;

import com.google.common.base.Throwables;

/**
 * @author Marvin Haagen
 */
enum ExtensionInfo {

    ;

    static String getExtensionName(final File extensionInfo) {
        try {
            final FileBasedConfigurationBuilder<XMLConfiguration> builder = new FileBasedConfigurationBuilder<>(
                    XMLConfiguration.class).configure(new Parameters().xml().setFile(extensionInfo));
            final XMLConfiguration platformExtensionsConfiguration = builder.getConfiguration();
            return platformExtensionsConfiguration.getString("extension[@name]");
        } catch (final Exception exception) {
            throw Throwables.propagate(exception);
        }
    }

    static List<String> getDependencyNames(final File extensionInfoDir, File hybrisInstallDirectory) {

        final File extensionInfo = new File(hybrisInstallDirectory, extensionInfoDir.toString());

        if (getExtensionName(extensionInfo).equals("core")) {
            return Collections.emptyList();
        }

        try {
            final FileBasedConfigurationBuilder<XMLConfiguration> builder = new FileBasedConfigurationBuilder<>(
                    XMLConfiguration.class).configure(new Parameters().xml().setFile(extensionInfo));
            final XMLConfiguration platformExtensionsConfiguration = builder.getConfiguration();
            final List<String> dependencies = platformExtensionsConfiguration.getList(String.class,
                    "extension.requires-extension[@name]");
            if (dependencies == null) {
                // return Collections.singletonList("core");
                return Collections.emptyList();
            }
            return dependencies;
        } catch (final Exception exception) {
            throw Throwables.propagate(exception);
        }
    }
}
