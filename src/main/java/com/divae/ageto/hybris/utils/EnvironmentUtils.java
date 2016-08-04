package com.divae.ageto.hybris.utils;

import java.io.*;

import com.google.common.base.Strings;
import de.hybris.bootstrap.config.SystemConfig;

/**
 * @author Klaus Hauschild
 */
public enum EnvironmentUtils {

    ;

    private static final String HYBRIS_INSTALL_DIR_ENVKEY = "HYBRIS_INSTALL_DIR";

    public static File getHybrisInstallationDirectory() {
        final String hybrisInstallDirectory = System.getenv(HYBRIS_INSTALL_DIR_ENVKEY);
        if (Strings.isNullOrEmpty(hybrisInstallDirectory)) {
            throw new IllegalStateException(String.format(
                    "Configure hybris installation directory by setting environment variable '%s'", HYBRIS_INSTALL_DIR_ENVKEY));
        }
        return new File(hybrisInstallDirectory);
    }

    public static File getHybrisPlatformDir()
    {
        return new File(getHybrisInstallationDirectory().toString()+"/bin/platform");
    }

    public static File getPropertyFromEnvPropFile(final String property) {
        final File hybrisInstallDir = getHybrisInstallationDirectory();
        return new File(getPropertyValue(property, new File(hybrisInstallDir.toString()+"/bin/platform/env.properties")));
    }

    private static String replaceAllVars(final String string)
    {
        String result = string.replaceAll("\\$\\{"+SystemConfig.PLATFORM_HOME+"\\}", getHybrisPlatformDir().toString());
        return result;
    }

    private static String getPropertyValue(final String property, final File file) {
        InputStream fileStream = null;
        try {
            fileStream = new FileInputStream(file.toString());
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("File not found: '"+file.toString()+"'");
        }
        InputStreamReader fileReader = new InputStreamReader(fileStream);
        BufferedReader lineReader = new BufferedReader(fileReader);

        while(true) {
            String line = null;
            try {
                line = lineReader.readLine();
            } catch (IOException e) {
                throw new IllegalArgumentException("Error reading from file '"+file.toString()+"'");
            }

            if (line == null)
            {
                break;
            }

            if (line.startsWith(property+"="))
            {
                return replaceAllVars(line.substring((property+"=").length()));
            }
        }

        throw new IllegalArgumentException("File '"+file.toString()+"' does not contain key '"+property+"'");
    }
}