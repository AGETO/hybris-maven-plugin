package com.divae.ageto.hybris.utils.maven;

import java.io.File;

/**
 * @author Klaus Hauschild
 */
public interface MavenExecutor {

    void execute(String[] arguments, File directory);

}
