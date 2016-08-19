package com.divae.ageto.hybris.codegenerator;

import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Set;

import org.junit.Rule;
import org.junit.rules.TemporaryFolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.collections.Sets;

import com.divae.ageto.hybris.install.extensions.Extension;
import com.divae.ageto.hybris.install.extensions.binary.ClassFolder;
import com.divae.ageto.hybris.install.extensions.binary.ExtensionBinary;
import com.divae.ageto.hybris.install.extensions.binary.None;
import com.divae.ageto.hybris.install.task.metadata.ExtensionMetadataFile;

/**
 * Created by mhaagen on 18.08.2016.
 */
public class HybrisFakeStructureTest {
    private final Set<File> dummyFiles  = Sets.newHashSet();
    private final Set<File> outputFiles = Sets.newHashSet();
    @Rule
    public TemporaryFolder  folder      = new TemporaryFolder();
    private Logger          LOGGER      = LoggerFactory.getLogger(HybrisFakeStructureTest.class);
    private File            hybrisFakeDirectory;

    @org.junit.Before
    public void setUp() throws Exception {

        String[] format = { "extensionfileslist/%s/%s-input.txt", "extensionfileslist/%s/%s-output.txt" };

        Object[][] ext = { { "hac", "bin/platform/ext/hac", new None() }, { "hac-web", "bin/platform/ext/hac/web",
                new ClassFolder(new File("bin/platform/ext/hac/web/webroot/WEB-INF/classes")) } };

        for (Object[] extension : ext) {
            readFiles(format[0], (String) extension[0], dummyFiles);
            readFiles(format[1], (String) extension[0], outputFiles);

            Extension extension2 = new Extension(new File((String) extension[1]), (String) extension[0],
                    (ExtensionBinary) extension[2]);
            LOGGER.debug(String.format("Generating metadata file for %s", extension2.getName()));
            ExtensionMetadataFile.createMetadataFile(extension2, new File(folder.toString()));

        }

        hybrisFakeDirectory = new File(new File(folder.toString()), "target/hybris-fake/hybris/bin");

        for (File file : dummyFiles) {
            LOGGER.debug(
                    String.format("Creating folder %s", new File(new File(folder.toString()), file.toString()).getParentFile()));
            new File(new File(folder.toString()), file.toString()).getParentFile().mkdirs();
            LOGGER.debug(String.format("Creating file %s", new File(new File(folder.toString()), file.toString())));
            new File(new File(folder.toString()), file.toString()).createNewFile();
        }
    }

    private void readFiles(final String format, final String extensionName, Set<File> files) throws IOException {
        InputStream inputStream = getClass().getResourceAsStream(String.format(format, extensionName, extensionName));
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while (true) {
            line = bufferedReader.readLine();
            if (line == null) {
                break;
            }

            files.add(new File(line));
        }
    }

    @org.junit.Test
    public void generate() throws Exception {
        for (File file : dummyFiles) {
            LOGGER.debug(String.format("Checking if folder %s exists",
                    new File(new File(folder.toString()), file.toString()).getParentFile()));
            assertTrue(new File(new File(folder.toString()), file.toString()).getParentFile().exists());
            assertTrue(new File(new File(folder.toString()), file.toString()).getParentFile().isDirectory());
            LOGGER.debug(String.format("Checking if file %s exists", new File(new File(folder.toString()), file.toString())));
            assertTrue(new File(new File(folder.toString()), file.toString()).exists());
            assertTrue(new File(new File(folder.toString()), file.toString()).isFile());
        }

        LOGGER.debug("Starting generation of hybris fake structure");
        HybrisFakeStructure.generate(new File(folder.toString()));
        LOGGER.debug("Hybris fake structure generation done");

        for (File file : outputFiles) {
            LOGGER.debug(String.format("Checking if file %s exists", new File(hybrisFakeDirectory, file.toString())));
            assertTrue(new File(hybrisFakeDirectory, file.toString()).exists());
        }
    }

    @org.junit.After
    public void clearUp() {
        folder.delete();
    }

}
