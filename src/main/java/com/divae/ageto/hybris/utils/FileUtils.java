package com.divae.ageto.hybris.utils;

import java.io.File;

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
        if (directory.isDirectory()) {
            final File[] files = directory.listFiles();
            if (files != null) {
                for (final File child : files) {
                    delete(child);
                }
            }
        }
        if (directory.exists() && !directory.delete()) {
            throw new RuntimeException(String.format("Directory %s can not be deleted", directory));
        }
    }

}
