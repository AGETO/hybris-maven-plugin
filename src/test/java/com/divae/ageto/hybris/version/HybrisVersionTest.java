package com.divae.ageto.hybris.version;

import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.testng.annotations.Test;

/**
 * @author Klaus Hauschild
 */
public class HybrisVersionTest {

    @Test
    public void versionTest() throws IOException {
        try (final InputStream stream = getClass().getResourceAsStream("5.5.1.1-build.number")) {
            final Properties buildNumberProperties = new Properties();
            buildNumberProperties.load(stream);
            final HybrisVersion hybrisVersion = new HybrisVersion(buildNumberProperties);
            assertEquals(hybrisVersion.getVersion(), "5.5.1.1");
            assertEquals(hybrisVersion.getApiVersion(), "5.5.1");
        }
    }

}
