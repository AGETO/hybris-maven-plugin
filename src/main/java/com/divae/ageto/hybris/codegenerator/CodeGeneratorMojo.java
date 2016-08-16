package com.divae.ageto.hybris.codegenerator;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

/**
 * @author Klaus Hauschild
 */
@Mojo(name = "code-generator", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
class CodeGeneratorMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project}", readonly = true)
    private MavenProject project;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        try {
            CodeGenerator.generate(project.getBasedir());
        } catch (final Exception exception) {
            throw new MojoFailureException("Code generation failed!", exception);
        }
    }

}
