package com.divae.ageto.hybris.install.task.metadata;

import java.io.File;

/**
 * @author Marvin Haagen
 */
public enum MetadataFile {

    ;

    public static File getFileName(String extensionName) {
        return new File(String.format("%s-metadata.properties", extensionName));
    }

    public static File getFilePath(String extensionName) {
        return new File(String.format("%s/src/main/resources", extensionName));
    }

}
