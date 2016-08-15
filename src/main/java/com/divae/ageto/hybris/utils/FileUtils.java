package com.divae.ageto.hybris.utils;

import java.io.File;

import com.google.common.base.Throwables;

/**
 * Created by mhaagen on 15.08.2016.
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
}
