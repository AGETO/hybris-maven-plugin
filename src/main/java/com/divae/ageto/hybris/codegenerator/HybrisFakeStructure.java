package com.divae.ageto.hybris.codegenerator;

import com.google.common.collect.Lists;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by mhaagen on 05.08.2016.
 */
public class HybrisFakeStructure {

    public static List<Path> getFiles() {
        return Lists.newArrayList(Paths.get("extensions.xml"), Paths.get("project.properties"),
                Paths.get("bootstrap/resources/pojo/global-eventtemplate.vm"),
                Paths.get("ext/core/resources/core-advanced-deployment.xml"), Paths.get("ext/core/resources/core-beans.xml"),
                Paths.get("ext/core/resources/core-items.xml"), Paths.get("ext/core/extensioninfo.xml"),
                Paths.get("ext/core/project.properties"), Paths.get("resources/schemas/beans.xsd"),
                Paths.get("resources/advanced.properties"));
    }

    static void generate(File hybrisReactorDir) {
        Path homePath = Paths.get(hybrisReactorDir.toString(), "target", "bin", "platform");

        Path paths[] = { Paths.get(homePath.toString()), Paths.get(homePath.toString(), "bootstrap", "resources", "pojo"),
                Paths.get(homePath.toString(), "ext", "core", "resources"),
                Paths.get(homePath.toString(), "resources", "schemas") };

        for (Path p : paths) {
            try {
                Files.createDirectories(p);
            } catch (IOException e) {
                throw new RuntimeException("Can not create directory '" + p.toString() + "'\n" + e.getStackTrace());
            }
        }

        for (Path f : getFiles()) {
            try {
                Files.copy(Paths.get(hybrisReactorDir + "/" + f), Paths.get(homePath + "/" + f));
            } catch (IOException e) {
                throw new RuntimeException("Can not copy file '" + f.toString() + "'\n" + e.getStackTrace());
            }
        }
    }

}
