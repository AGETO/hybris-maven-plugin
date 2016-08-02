package com.divae.ageto.hybris.codegenerator;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.project.MavenProject;

/**
 * @author Klaus Hauschild
 */
@Mojo(name = "code-generator", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class CodeGeneratorMojo extends AbstractMojo {

    @Component
    private MavenProject project;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        CodeGenerator.generate(project.getBasedir());
    }

}
