package com.divae.ageto.hybris.install;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.divae.ageto.hybris.install.extensions.Extension;
import com.divae.ageto.hybris.install.extensions.ExtensionFactory;
import com.divae.ageto.hybris.install.task.TaskChainTask;
import com.divae.ageto.hybris.install.task.TaskContext;
import com.divae.ageto.hybris.version.HybrisVersion;
import com.google.common.collect.Lists;

/**
 * @author Klaus Hauschild
 */
class InstallHybrisArtifacts {

    private static final Logger       LOGGER           = LoggerFactory.getLogger(InstallHybrisArtifacts.class);

    private static final List<String> BASIC_EXTENSIONS = Lists.newArrayList(                                   //
            "advancedsavedquery",                                                                              //
            "catalog",                                                                                         //
            "comments",                                                                                        //
            "commons",                                                                                         //
            // "config", //
            "core",                                                                                            //
            "deliveryzone",                                                                                    //
            "europe1",                                                                                         //
            "hac",                                                                                             //
            "impex",                                                                                           //
            "maintenanceweb",                                                                                  //
            "mediaweb",                                                                                        //
            "paymentstandard",                                                                                 //
            // "platform", //
            "platformservices",                                                                                //
            "processing",                                                                                      //
            "scripting",                                                                                       //
            "testweb",                                                                                         //
            "validation",                                                                                      //
            "workflow"                                                                                         //
    );

    private final TaskContext         taskContext;
    private final TaskChainTask       installTasks;

    InstallHybrisArtifacts(final File hybrisDirectory) {
        final HybrisVersion hybrisVersion = HybrisVersion.of(hybrisDirectory);
        taskContext = new TaskContext(hybrisVersion, hybrisDirectory);
        final List<Extension> extensions = ExtensionFactory.getExtensions(hybrisDirectory);
        final List<Extension> transitiveExtensions = ExtensionFactory.getTransitiveExtensions(extensions);
        final List<Extension> basicExtensions = filterBasicExtensions(transitiveExtensions);
        final List<Extension> basicTransitiveExtensions = ExtensionFactory.getTransitiveExtensions(basicExtensions);
        // TODO only use basic extensions and their transitive extension dependencies
        installTasks = new TaskChainTask("install artifacts",
                InstallStrategy.getInstallTasks(taskContext, basicTransitiveExtensions));
        /*installTasks = new TaskChainTask("install artifacts",
                InstallStrategy.getInstallTasks(taskContext, transitiveExtensions));*/
    }

    private List<Extension> filterBasicExtensions(final List<Extension> extensions) {
        return Lists.newArrayList(
                extensions.stream().filter(input -> BASIC_EXTENSIONS.contains(input.getName())).collect(Collectors.toList()));
    }

    public void execute() {
        LOGGER.info(String.format("Install maven artifacts for hybris suite %s", taskContext.getHybrisVersion()));

        installTasks.execute(taskContext);
    }

    public TaskContext getTaskContext() {
        return taskContext;
    }

}
