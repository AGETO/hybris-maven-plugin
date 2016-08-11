package com.divae.ageto.hybris.codegenerator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.divae.ageto.hybris.install.extensions.Extension;
import com.google.common.base.Throwables;

/**
 * @author Marvin Haagen
 */
public class HybrisFakeStructure {

    public static File generate(final File hybrisReactorDir) {
        try {
            final File platformDirectory = new File(hybrisReactorDir, "target/hybris-fake/hybris/bin/platform");
            platformDirectory.mkdirs();

            final File binDirectory = platformDirectory.getParentFile();
            final File platformExtensionsXML = new File(
                    "src/main/resources/com/divae/ageto/hybris/install/platform.extensions.xml");
            // final File platformExtensionsXML = new File("com/divae/ageto/hybris/install/platform.extensions.xml");

            copyPlatformExtensionsXML(platformExtensionsXML, new File(binDirectory, "extensions.xml"));
            final List<Extension> extensions = readExtensionList(new FileInputStream(platformExtensionsXML));

            for (Extension extension : extensions) {
                File coreExtensionDirectory = new File(binDirectory, "ext/" + extension.getName());
                coreExtensionDirectory.mkdirs();
                FileUtils.copyFile(
                        new File(hybrisReactorDir, extension.getName() + "/src/main/resources/extensioninfo.xml"),
                        new File(coreExtensionDirectory, "extensioninfo.xml"));

                // TODO
                // if (extension.getClass() == Core.class) {
                    FileUtils.copyFile(new File(hybrisReactorDir, "core/src/main/resources/core-advanced-deployment.xml"),
                            new File(coreExtensionDirectory, "resources/core-advanced-deployment.xml"));
                    FileUtils.copyFile(new File(hybrisReactorDir, "core/src/main/resources/core-beans.xml"),
                            new File(coreExtensionDirectory, "resources/core-beans.xml"));
                    FileUtils.copyFile(new File(hybrisReactorDir, "core/src/main/resources/core-items.xml"),
                            new File(coreExtensionDirectory, "resources/core-items.xml"));
                    FileUtils.copyFile(new File(hybrisReactorDir, "core/src/main/resources/project.properties"),
                            new File(coreExtensionDirectory, "project.properties"));
                // }
            }

            FileUtils.copyFile(new File(hybrisReactorDir, "src/main/resources/advanced.properties"),
                    new File(binDirectory, "resources/advanced.properties"));

            FileUtils.copyFile(new File(hybrisReactorDir, "src/main/resources/schemas/beans.xsd"),
                    new File(binDirectory, "resources/schemas/beans.xsd"));

            FileUtils.copyFile(new File(hybrisReactorDir, "src/main/resources/project.properties"),
                    new File(binDirectory, "project.properties"));

            FileUtils.copyFile(new File(hybrisReactorDir, "src/main/resources/project.properties"),
                    new File(binDirectory, "project.properties"));

            FileUtils.copyFile(new File(hybrisReactorDir, "src/main/resources/bootstrap/pojo/global-eventtemplate.vm"),
                    new File(binDirectory, "bootstrap/resources/pojo/global-eventtemplate.vm"));

            return binDirectory;
        } catch (final Exception exception) {
            throw Throwables.propagate(exception);
        }
    }

    protected static List<com.divae.ageto.hybris.install.extensions.Extension> readExtensionList(InputStream inputStream) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException("Can not initiate XML parser", e);
        }
        Document document = null;
        try {
            document = builder.parse(inputStream);
        } catch (SAXException e) {
            throw new RuntimeException("Can not parse input stream", e);
        } catch (IOException e) {
            throw new RuntimeException("Can not parse input stream", e);
        }

        List<Extension> extensions = new ArrayList<>();

        NodeList nodeList = document.getElementsByTagName("extension");

        for (int a = 0; a < nodeList.getLength(); a++) {

            // TODO
            Extension[] extension = {};

            Node node = nodeList.item(a);
            String extensionName = node.getAttributes().getNamedItem("name").getNodeValue().toLowerCase();

            for (Extension ext : extension) {
                if (extensionName.equals(ext.getName().toLowerCase())) {
                    try {
                        extensions.add(ext.getClass().newInstance());
                    } catch (ReflectiveOperationException e) {
                        throw new RuntimeException("Extension '" + extensionName + "' can not be added to extensions list", e);
                    }
                }
            }
        }

        return extensions;
    }

    private static void copyPlatformExtensionsXML(final File platformExtensionsXML, final File targetFile) {
        try {
            FileUtils.copyFile(platformExtensionsXML, targetFile);
            /*IOUtils.copy(ClassLoader.getSystemResourceAsStream(platformExtensionsXML.toString()),
                    new FileOutputStream(targetFile));*/
        } catch (IOException e) {
            throw new RuntimeException("Can not copy file '" + platformExtensionsXML + "' to '" + targetFile + "'", e);
        }
    }

}
