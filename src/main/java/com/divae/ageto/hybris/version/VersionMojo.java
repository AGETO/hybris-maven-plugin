package com.divae.ageto.hybris.version;

import com.divae.ageto.hybris.AbstractHybrisDirectoryMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;

import java.text.SimpleDateFormat;

/**
 * @author Klaus Hauschild
 */
@Mojo(name = "version", requiresProject = false)
public class VersionMojo extends AbstractHybrisDirectoryMojo {

    private final SimpleDateFormat prettyDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        final HybrisVersion hybrisVersion = HybrisVersion.of(getHybrisDirectory());
        getLog().info(String.format("hybris version:..........%s", hybrisVersion.getVersion()));
        getLog().info(String.format("hybris api version:......%s", hybrisVersion.getApiVersion()));
        getLog().info(String.format("build date:..............%s", prettyDateFormat.format(hybrisVersion.getBuildDate())));
        getLog().info(String.format("release date:............%s", prettyDateFormat.format(hybrisVersion.getReleaseDate())));
    }

}
