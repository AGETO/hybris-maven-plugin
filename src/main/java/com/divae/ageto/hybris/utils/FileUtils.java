package com.divae.ageto.hybris.utils;

import java.io.File;
import java.io.IOException;

import com.google.common.base.Throwables;

/**
 * @author Marvin Haagen
 */
public enum FileUtils {

    ;

    public static void makeDirectory(File directory) {
        if (!directory.exists() && !directory.mkdirs()) {
            throw new RuntimeException(String.format("Directory %s can not be created", directory));
        }
    }

    public static void makeFile(File file) {
        try {
            if (!file.exists() && !file.createNewFile()) {
                throw new RuntimeException(String.format("Directory %s can not be created", file));
            }
        } catch (Exception e) {
            throw Throwables.propagate(e);
        }
    }

    public static void delete(final File directory) {
        try {
            org.apache.commons.io.FileUtils.forceDelete(directory);
        } catch (IOException e) {
            Throwables.propagate(e);
        }
    }

}
