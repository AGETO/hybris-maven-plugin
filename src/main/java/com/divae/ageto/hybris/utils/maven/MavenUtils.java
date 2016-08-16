package com.divae.ageto.hybris.utils.maven;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URI;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.model.io.xpp3.MavenXpp3Writer;
import org.eclipse.aether.repository.RemoteRepository;
import org.sonatype.aether.util.artifact.DefaultArtifact;
import org.sonatype.aether.util.layout.MavenDefaultLayout;

import com.google.common.base.Throwables;

/**
 * @author Klaus Hauschild
 */
public enum MavenUtils {

    ;

    public static Model readModel(final File file) {
        try (final Reader reader = new FileReader(file)) {
            return MavenUtils.readModel(reader);
        } catch (final Exception exception) {
            throw Throwables.propagate(exception);
        }
    }

    public static Model readModel(final Reader reader) {
        try {
            final MavenXpp3Reader mavenXpp3Reader = new MavenXpp3Reader();
            return mavenXpp3Reader.read(reader);
        } catch (final Exception exception) {
            throw Throwables.propagate(exception);
        }
    }

    public static void writeModel(final Model model, final File file) {
        try (final OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file))) {
            final MavenXpp3Writer mavenXpp3Writer = new MavenXpp3Writer();
            mavenXpp3Writer.write(outputStream, model);
        } catch (final Exception exception) {
            throw Throwables.propagate(exception);
        }
    }

    public static boolean isDependencyResolvable(final Dependency dependency) {
        final MavenDefaultLayout defaultLayout = new MavenDefaultLayout();
        final RemoteRepository centralRepository = new RemoteRepository.Builder( //
                "central", //
                "default", //
                "http://repo1.maven.org/maven2/" //
        ).build();
        final URI centralUri = URI.create(centralRepository.getUrl());
        final URI artifactUri = centralUri.resolve(defaultLayout.getPath(
                new DefaultArtifact(dependency.getGroupId(), dependency.getArtifactId(), "jar", dependency.getVersion())));
        try {
            final HttpURLConnection connection = (HttpURLConnection) artifactUri.toURL().openConnection();
            connection.setRequestMethod("HEAD");
            connection.connect();
            return connection.getResponseCode() != 404;
        } catch (final Exception exception) {
            throw Throwables.propagate(exception);
        }
    }

}
