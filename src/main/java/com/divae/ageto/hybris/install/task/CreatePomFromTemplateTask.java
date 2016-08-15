package com.divae.ageto.hybris.install.task;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.model.io.xpp3.MavenXpp3Writer;

import com.divae.ageto.hybris.utils.FileUtils;
import com.google.common.base.Throwables;

/**
 * @author Klaus Hauschild
 */
class CreatePomFromTemplateTask extends AbstractWorkDirectoryTask {

    private final File         template;
    private final File         targetDirectory;
    private final List<String> modules;

    CreatePomFromTemplateTask(final File template, final File targetDirectory, final List<String> modules) {
        this.template = template;
        this.targetDirectory = targetDirectory;
        this.modules = modules;
    }

    @Override
    protected void execute(final TaskContext taskContext, final File workDirectory) {
        FileOutputStream stream = null;
        try {
            // read model
            final MavenXpp3Reader reader = new MavenXpp3Reader();
            final Model model = reader
                    .read(new TokenReplacingReader(
                            new InputStreamReader(ClassLoader.getSystemResourceAsStream(template.toString())),
                            Collections.singletonMap("hybris.version", taskContext.getHybrisVersion().getVersion())));

            // fill with parameters
            if (modules != null) {
                for (final String moduleName : modules) {
                    model.getModules().add(moduleName);
                }
            }

            // write model
            final File target = new File(workDirectory, targetDirectory.toString());
            final File pomFile = new File(target, "pom.xml");
            FileUtils.makeDirectory(pomFile.getParentFile());
            FileUtils.makeFile(pomFile);
            stream = new FileOutputStream(pomFile);
            new MavenXpp3Writer().write(stream, model);
        } catch (final Exception exception) {
            throw Throwables.propagate(exception);
        } finally {
            if (stream != null) {
                IOUtils.closeQuietly(stream);
            }
        }
    }
}
