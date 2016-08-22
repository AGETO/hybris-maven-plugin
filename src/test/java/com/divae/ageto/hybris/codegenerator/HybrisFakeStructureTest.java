package com.divae.ageto.hybris.codegenerator;

import static org.testng.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.collections.Sets;

import com.divae.ageto.hybris.AbstractTempDirectoryTests;
import com.divae.ageto.hybris.install.extensions.Extension;
import com.divae.ageto.hybris.install.extensions.binary.ClassFolder;
import com.divae.ageto.hybris.install.extensions.binary.ExtensionBinary;
import com.divae.ageto.hybris.install.extensions.binary.None;
import com.divae.ageto.hybris.install.task.metadata.ExtensionMetadataFile;

/**
 * @author Marvin Haagen
 */
public class HybrisFakeStructureTest extends AbstractTempDirectoryTests {

    private final Set<File> dummyFiles  = Sets.newHashSet();
    private final Set<File> outputFiles = Sets.newHashSet();
    private Logger          LOGGER      = LoggerFactory.getLogger(HybrisFakeStructureTest.class);
    private File            hybrisFakeDirectory;

    @BeforeTest
    public void beforeTest() throws Exception {
        if (getTempDirectory() == null) {
            super.prepareTempDirectory();
        }
        final String[] format = { "extensionfileslist/%s/%s-input.txt", "extensionfileslist/%s/%s-output.txt" };

        final Object[][] ext = { { "hac", "bin/platform/ext/hac", new None() }, { "hac-web", "bin/platform/ext/hac/web",
                new ClassFolder(new File("bin/platform/ext/hac/web/webroot/WEB-INF/classes")) } };

        for (final Object[] extension : ext) {
            readFiles(format[0], (String) extension[0], dummyFiles);
            readFiles(format[1], (String) extension[0], outputFiles);

            final Extension extension2 = new Extension(new File((String) extension[1]), (String) extension[0],
                    (ExtensionBinary) extension[2]);
            LOGGER.debug(String.format("Generating metadata file for %s", extension2.getName()));
            ExtensionMetadataFile.createMetadataFile(extension2, getTempDirectory());

        }

        hybrisFakeDirectory = new File(getTempDirectory(), "target/hybris-fake/hybris/bin");

        for (final File file : dummyFiles) {
            LOGGER.debug(String.format("Creating folder %s", new File(getTempDirectory(), file.toString()).getParentFile()));
            new File(getTempDirectory(), file.toString()).getParentFile().mkdirs();
            LOGGER.debug(String.format("Creating file %s", new File(getTempDirectory(), file.toString())));
            new File(getTempDirectory(), file.toString()).createNewFile();
        }
    }

    private void readFiles(final String format, final String extensionName, final Set<File> files) throws IOException {
        final InputStream inputStream = getClass().getResourceAsStream(String.format(format, extensionName, extensionName));
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while (true) {
            line = bufferedReader.readLine();
            if (line == null) {
                break;
            }

            files.add(new File(line));
        }
    }

    @Test
    public void generate() throws Exception {
        for (final File file : dummyFiles) {
            LOGGER.debug(
                    String.format("Checking if folder %s exists", new File(getTempDirectory(), file.toString()).getParentFile()));
            assertTrue(new File(getTempDirectory(), file.toString()).getParentFile().exists());
            assertTrue(new File(getTempDirectory(), file.toString()).getParentFile().isDirectory());
            LOGGER.debug(String.format("Checking if file %s exists", new File(getTempDirectory(), file.toString())));
            assertTrue(new File(getTempDirectory(), file.toString()).exists());
            assertTrue(new File(getTempDirectory(), file.toString()).isFile());
        }

        LOGGER.debug("Starting generation of hybris fake structure");
        HybrisFakeStructure.generate(getTempDirectory());
        LOGGER.debug("Hybris fake structure generation done");

        for (final File file : outputFiles) {
            LOGGER.debug(String.format("Checking if file %s exists", new File(hybrisFakeDirectory, file.toString())));
            assertTrue(new File(hybrisFakeDirectory, file.toString()).exists());
        }
    }

}
