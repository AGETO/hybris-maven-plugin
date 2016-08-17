package com.divae.ageto.hybris.install.task;

import java.io.File;
import java.util.Collections;

import org.testng.annotations.Test;

import com.divae.ageto.hybris.install.extensions.Extension;
import com.divae.ageto.hybris.install.extensions.WebExtension;
import com.divae.ageto.hybris.install.extensions.binary.ClassFolder;
import com.divae.ageto.hybris.utils.EnvironmentUtils;
import com.divae.ageto.hybris.version.HybrisVersion;

/**
 * Created by mhaagen on 16.08.2016.
 */
public class RestructureWebExtensionTaskIT {
    @Test
    public void testExecute() throws Exception {
        File hybrisDirectory = EnvironmentUtils.getHybrisInstallationDirectory();
        final HybrisVersion hybrisVersion = HybrisVersion.of(hybrisDirectory);
        TaskContext taskContext = new TaskContext(hybrisVersion, hybrisDirectory);

        new RestructureWebExtensionTask(new WebExtension(new File("bin/platform/ext/hac/web"), "hac-web",
                new ClassFolder(new File("C:\\Users\\mhaagen\\hybris\\bin\\platform\\ext\\hac\\web\\webroot\\WEB-INF\\classes")),
                Collections.singletonList(new Extension(new File(""), "hac", null)))).execute(taskContext,
                        new File("C:\\Users\\mhaagen\\AppData\\Local\\Temp\\hybris-test-temp"));
    }

}
