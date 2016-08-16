package com.divae.ageto.hybris.install.task;

import java.io.File;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;

import org.apache.maven.model.Model;

import com.divae.ageto.hybris.utils.FileUtils;
import com.divae.ageto.hybris.utils.maven.MavenUtils;

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
        // readModel model
        final Model model = MavenUtils
                .readModel(new TokenReplacingReader(new InputStreamReader(ClassLoader.getSystemResourceAsStream(template.toString())),
                        Collections.singletonMap("hybris.version", taskContext.getHybrisVersion().getVersion())));

        // fill with parameters
        if (modules != null) {
            for (final String moduleName : modules) {
                model.getModules().add(moduleName);
            }
        }

        // writeModel model
        final File target = new File(workDirectory, targetDirectory.toString());
        final File pomFile = new File(target, "pom.xml");
        FileUtils.makeDirectory(pomFile.getParentFile());
        FileUtils.makeFile(pomFile);
        MavenUtils.writeModel(model, pomFile);
    }

}
