package com.divae.ageto.hybris.version;

import com.divae.ageto.hybris.utils.Utils;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * @author Klaus Hauschild
 */
public class HybrisVersion {

    private static final String BUILD_NUMBER__FILE     = "build.number";
    private static final String VERSION__PROPERTY      = "version";
    private static final String API_VERSION__PROPERTY  = "version.api";
    private static final String BUILD_DATE__PROPERTY   = "builddate";
    private static final String RELEASE_DATE__PROPERTY = "releasedate";
    private static final String DATE_FORMAT            = "yyyyMMdd HHmm";

    private final String        version;
    private final String        apiVersion;
    private final Date          buildDate;
    private final Date          releaseDate;

    HybrisVersion(final Properties buildNumberProperties) {
        version = buildNumberProperties.getProperty(VERSION__PROPERTY);
        apiVersion = buildNumberProperties.getProperty(API_VERSION__PROPERTY);
        try {
            final SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
            buildDate = dateFormat.parse(buildNumberProperties.getProperty(BUILD_DATE__PROPERTY));
            releaseDate = dateFormat.parse(buildNumberProperties.getProperty(RELEASE_DATE__PROPERTY));
        } catch (final ParseException exception) {
            throw new IllegalStateException("Unable to parse 'builddate' or 'releasedate'.", exception);
        }
    }

    public static HybrisVersion of(final File hybrisDirectory) {
        final File hybrisPlatformDirectory = Utils.getHybrisPlatformDirectory(hybrisDirectory);
        final File buildNumberFile = new File(hybrisPlatformDirectory, BUILD_NUMBER__FILE);
        final Properties buildNumberProperties = new Properties();
        try (final InputStream stream = new BufferedInputStream(new FileInputStream(buildNumberFile))) {
            buildNumberProperties.load(stream);
            return new HybrisVersion((buildNumberProperties));
        } catch (final IOException exception) {
            throw new IllegalStateException(String.format("Unable to find %s", buildNumberFile), exception);
        }
    }

    public String getVersion() {
        return version;
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public Date getBuildDate() {
        return buildDate;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

}
