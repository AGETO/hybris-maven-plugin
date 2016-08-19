package com.divae.ageto.hybris.install.extensions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.beust.jcommander.internal.Sets;
import com.divae.ageto.hybris.install.extensions.binary.ClassFolder;
import com.divae.ageto.hybris.install.extensions.binary.JARArchive;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;

/**
 * Created by mhaagen on 19.08.2016.
 */
public class ExtensionFactoryTest {

    private final Set<String> extensionNames = Sets.newHashSet();
    private final Set<File>   jarFiles       = Sets.newHashSet();
    private final Set<File>   classFiles     = Sets.newHashSet();

    @Rule
    public TemporaryFolder    folder         = new TemporaryFolder();

    @Before
    public void setUp() throws Exception {
        try {
            Set<String> extensionNames = readExtensionNames(folder.getRoot());

            this.extensionNames.addAll(extensionNames);
            createExtensionFolders(folder.getRoot(), extensionNames);
            createClassFolders(folder.getRoot());
            createJarFiles(folder.getRoot());

        } catch (final Exception exception) {
            throw Throwables.propagate(exception);
        }
    }

    private void createJarFiles(File folder) throws Exception {
        final InputStream jarFiles = getClass().getResourceAsStream("jarfiles.txt");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(jarFiles));
        String line;

        while (true) {
            line = bufferedReader.readLine();
            if (line == null) {
                break;
            }

            new File(folder, line).getParentFile().mkdirs();
            new File(folder, line).createNewFile();
            this.jarFiles.add(new File(folder, line));
        }
    }

    private void createClassFolders(File folder) throws Exception {
        final InputStream classFilesTXT = getClass().getResourceAsStream("classfiles.txt");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(classFilesTXT));
        String line;

        while (true) {
            line = bufferedReader.readLine();
            if (line == null) {
                break;
            }

            new File(folder, line).mkdirs();
            this.classFiles.add(new File(folder, line));
        }
    }

    private void createExtensionFolders(File folder, Set<String> extensionNames) throws Exception {
        for (String extensionName : extensionNames) {
            File extensionDir = new File(folder, String.format("bin/platform/ext/%s", extensionName));
            extensionDir.mkdirs();
            InputStream extensioninfo = getClass().getResourceAsStream(String.format("extensioninfo/%s.xml", extensionName));
            Files.copy(extensioninfo, new File(extensionDir, "extensioninfo.xml").toPath());
        }
    }

    private Set<String> readExtensionNames(File folder) throws Exception {
        final InputStream extensionsXML = getClass().getResourceAsStream("extensions.xml");
        new File(folder, "bin/platform").mkdirs();
        Files.copy(extensionsXML, new File(folder, "bin/platform/extensions.xml").toPath());

        final File hybrisPlatformDirectory = new File(folder, "bin/platform");
        final File platformExtensionsFile = new File(hybrisPlatformDirectory, "extensions.xml");
        final FileBasedConfigurationBuilder<XMLConfiguration> builder = new FileBasedConfigurationBuilder<>(
                XMLConfiguration.class).configure(new Parameters().xml().setFile(platformExtensionsFile));
        final XMLConfiguration platformExtensionsConfiguration = builder.getConfiguration();
        final List<String> extensionNames = Lists
                .newArrayList(platformExtensionsConfiguration.getList(String.class, "extensions.extension[@name]"));
        return new HashSet<>(extensionNames);
    }

    @After
    public void tearDown() throws Exception {
        folder.delete();
    }

    @Test
    public void getExtensions() throws Exception {
        final Set<Extension> extensions = ExtensionFactory.getExtensions(folder.getRoot());

        assertEquals(14, extensions.size());

        for (Extension extension : extensions) {
            assertTrue(extensionNames.contains(extension.getName()));
            assertTrue(!extension.getBaseDirectory().isAbsolute());
            checkBinary(folder.getRoot(), extension);
        }
    }

    private void checkBinary(final File folder, final Extension extension) {
        assertNotNull(extension.getBinary());
        if (extension.getBinary().getClass() == JARArchive.class) {
            assertTrue(!extension.getBinary().getExtensionBinaryPath().isAbsolute());
        }
        if (extension.getBinary().getClass() == ClassFolder.class) {
            assertTrue(!extension.getBinary().getExtensionBinaryPath().isAbsolute());
        }
    }
}
