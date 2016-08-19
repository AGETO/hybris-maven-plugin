package com.divae.ageto.hybris.install.task;

import java.io.File;
import java.util.Collections;

import com.divae.ageto.hybris.install.extensions.Extension;
import com.divae.ageto.hybris.install.extensions.WebExtension;
import com.divae.ageto.hybris.install.extensions.binary.ClassFolder;
import com.divae.ageto.hybris.utils.EnvironmentUtils;
import com.divae.ageto.hybris.version.HybrisVersion;

/**
 * @author Marvin Haagen
 */
public class RestructureWebExtensionTaskIT {

    // @Test
    public void testExecute() throws Exception {
        final File hybrisDirectory = EnvironmentUtils.getHybrisInstallationDirectory();
        final HybrisVersion hybrisVersion = HybrisVersion.of(hybrisDirectory);
        final TaskContext taskContext = new TaskContext(hybrisVersion, hybrisDirectory);

        new RestructureWebExtensionTask(new WebExtension(new File("bin/platform/ext/hac/web"), "hac-web",
                new ClassFolder(new File(hybrisDirectory, "bin/platform/ext/hac/web/webroot/WEB-INF/classes")),
                Collections.singleton(new Extension(new File(""), "hac", null)))).execute(taskContext,
                        new File(new File(System.getProperty("java.io.tmpdir")), "hybris-test-temp"));
    }

}
