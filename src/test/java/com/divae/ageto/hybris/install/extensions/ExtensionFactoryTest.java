package com.divae.ageto.hybris.install.extensions;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.configuration2.XMLConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.beust.jcommander.internal.Sets;
import com.divae.ageto.hybris.AbstractTempDirectoryTests;
import com.divae.ageto.hybris.install.extensions.binary.ClassFolder;
import com.divae.ageto.hybris.install.extensions.binary.JARArchive;
import com.google.common.collect.Lists;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

/**
 * @author Marvin Haagen
 */
public class ExtensionFactoryTest extends AbstractTempDirectoryTests {

    private Set<String> extensionNames = Sets.newHashSet();
    private final Set<File> jarFiles = Sets.newHashSet();
    private final Set<File> classFiles = Sets.newHashSet();

    @BeforeTest
    public void beforeTest() throws Exception {
        extensionNames = readExtensionNames(getTempDirectory());

        createExtensionFolders(getTempDirectory(), extensionNames);
        createClassFolders(getTempDirectory());
        createJarFiles(getTempDirectory());
    }

    private void createFiles(final File folder, final String source, final Set<File> target) throws Exception {
        final InputStream jarFiles = getClass().getResourceAsStream(source);
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(jarFiles))) {
            String line;

            while (true) {
                line = bufferedReader.readLine();
                if (line == null) {
                    break;
                }

                new File(folder, line).getParentFile().mkdirs();
                new File(folder, line).createNewFile();
                target.add(new File(folder, line));
            }
        }
    }

    private void createJarFiles(final File folder) throws Exception {
        createFiles(folder, "jarfiles.txt", jarFiles);
    }

    private void createClassFolders(final File folder) throws Exception {
        createFiles(folder, "classfiles.txt", classFiles);
    }

    private void createExtensionFolders(final File folder, final Set<String> extensionNames) throws Exception {
        for (final String extensionName : extensionNames) {
            final File extensionDir = new File(folder, String.format("bin/platform/ext/%s", extensionName));
            extensionDir.mkdirs();
            try (InputStream extensioninfo = getClass()
                    .getResourceAsStream(String.format("extensioninfo/%s.xml", extensionName))) {
                Files.copy(extensioninfo, new File(extensionDir, "extensioninfo.xml").toPath());
            }
        }
    }

    private Set<String> readExtensionNames(final File folder) throws Exception {
        try (final InputStream extensionsXML = getClass().getResourceAsStream("extensions.xml")) {
            new File(folder, "bin/platform").mkdirs();
            Files.copy(extensionsXML, new File(folder, "bin/platform/extensions.xml").toPath());
        }

        final File hybrisPlatformDirectory = new File(folder, "bin/platform");
        final File platformExtensionsFile = new File(hybrisPlatformDirectory, "extensions.xml");
        final FileBasedConfigurationBuilder<XMLConfiguration> builder = new FileBasedConfigurationBuilder<>(
                XMLConfiguration.class).configure(new Parameters().xml().setFile(platformExtensionsFile));
        final XMLConfiguration platformExtensionsConfiguration = builder.getConfiguration();
        final List<String> extensionNames = Lists
                .newArrayList(platformExtensionsConfiguration.getList(String.class, "extensions.extension[@name]"));
        return new HashSet<>(extensionNames);
    }

    @Test
    public void getExtensions() throws Exception {
        final Set<Extension> extensions = ExtensionFactory.getExtensions(getTempDirectory());

        assertEquals(extensions.size(), 14);

        for (final Extension extension : extensions) {
            assertTrue(extensionNames.contains(extension.getName()));
            assertTrue(!extension.getBaseDirectory().isAbsolute());
            checkBinary(extension);
        }
    }

    private void checkBinary(final Extension extension) {
        assertNotNull(extension.getBinary());
        if (extension.getBinary() instanceof JARArchive) {
            assertTrue(!extension.getBinary().getExtensionBinaryPath().isAbsolute());
        }
        if (extension.getBinary() instanceof ClassFolder) {
            assertTrue(!extension.getBinary().getExtensionBinaryPath().isAbsolute());
        }
    }
}
