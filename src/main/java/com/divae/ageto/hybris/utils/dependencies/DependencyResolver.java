package com.divae.ageto.hybris.utils.dependencies;

import org.apache.maven.model.Dependency;

import java.io.File;

/**
 * @author Klaus Hauschild
 */
public interface DependencyResolver {

    Dependency resolve(File library);

}
