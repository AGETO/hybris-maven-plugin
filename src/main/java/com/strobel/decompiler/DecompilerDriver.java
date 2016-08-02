package com.strobel.decompiler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.EnumSet;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.strobel.assembler.metadata.CompositeTypeLoader;
import com.strobel.assembler.metadata.DeobfuscationUtilities;
import com.strobel.assembler.metadata.IMetadataResolver;
import com.strobel.assembler.metadata.ITypeLoader;
import com.strobel.assembler.metadata.JarTypeLoader;
import com.strobel.assembler.metadata.MetadataParser;
import com.strobel.assembler.metadata.MetadataSystem;
import com.strobel.assembler.metadata.TypeDefinition;
import com.strobel.assembler.metadata.TypeReference;
import com.strobel.core.StringUtilities;
import com.strobel.decompiler.AnsiTextOutput.ColorScheme;
import com.strobel.decompiler.LineNumberFormatter.LineNumberOption;
import com.strobel.decompiler.languages.BytecodeLanguage;
import com.strobel.decompiler.languages.LineNumberPosition;
import com.strobel.decompiler.languages.TypeDecompilationResults;
import com.strobel.io.PathHelper;

public class DecompilerDriver {

    public static void decompileJar(String jarFilePath, CommandLineOptions options, DecompilationOptions decompilationOptions)
            throws IOException {
        File jarFile = new File(jarFilePath);
        if (!jarFile.exists()) {
            throw new FileNotFoundException("File not found: " + jarFilePath);
        } else {
            DecompilerSettings settings = decompilationOptions.getSettings();
            JarFile jar = new JarFile(jarFile);
            Enumeration entries = jar.entries();
            boolean oldShowSyntheticMembers = settings.getShowSyntheticMembers();
            ITypeLoader oldTypeLoader = settings.getTypeLoader();
            settings.setShowSyntheticMembers(false);
            settings.setTypeLoader(new CompositeTypeLoader(new JarTypeLoader(jar), settings.getTypeLoader()));

            try {
                NoRetryMetadataSystem metadataSystem = new NoRetryMetadataSystem(settings.getTypeLoader());
                metadataSystem.setEagerMethodLoadingEnabled(options.isEagerMethodLoadingEnabled());
                int classesDecompiled = 0;

                while (entries.hasMoreElements()) {
                    JarEntry entry = (JarEntry) entries.nextElement();
                    String name = entry.getName();
                    if (name.endsWith(".class")) {
                        String internalName = StringUtilities.removeRight(name, ".class");

                        try {
                            decompileType(metadataSystem, internalName, options, decompilationOptions, false);
                            ++classesDecompiled;
                            if (classesDecompiled % 100 == 0) {
                                metadataSystem = new NoRetryMetadataSystem(settings.getTypeLoader());
                            }
                        } catch (Throwable var18) {
                            var18.printStackTrace();
                        }
                    }
                }
            } finally {
                settings.setShowSyntheticMembers(oldShowSyntheticMembers);
                settings.setTypeLoader(oldTypeLoader);
            }

        }
    }

    private static void decompileType(MetadataSystem metadataSystem, String typeName, CommandLineOptions commandLineOptions,
            DecompilationOptions options, boolean includeNested) throws IOException {
        DecompilerSettings settings = options.getSettings();
        Object type;
        if (typeName.length() == 1) {
            MetadataParser resolvedType = new MetadataParser(IMetadataResolver.EMPTY);
            TypeReference writer = resolvedType.parseTypeDescriptor(typeName);
            type = metadataSystem.resolve(writer);
        } else {
            type = metadataSystem.lookupType(typeName);
        }

        TypeDefinition resolvedType1;
        if (type != null && (resolvedType1 = ((TypeReference) type).resolve()) != null) {
            DeobfuscationUtilities.processType(resolvedType1);
            if (includeNested || !resolvedType1.isNested() && !resolvedType1.isAnonymous() && !resolvedType1.isSynthetic()) {
                Writer writer1 = createWriter(resolvedType1, settings);
                boolean writeToFile = writer1 instanceof FileOutputWriter;
                Object output;
                if (writeToFile) {
                    output = new PlainTextOutput(writer1);
                } else {
                    output = new AnsiTextOutput(writer1, commandLineOptions.getUseLightColorScheme() ? ColorScheme.LIGHT
                            : ColorScheme.DARK);
                }

                ((PlainTextOutput) output).setUnicodeOutputEnabled(settings.isUnicodeOutputEnabled());
                if (settings.getLanguage() instanceof BytecodeLanguage) {
                    ((PlainTextOutput) output).setIndentToken("  ");
                }

                TypeDecompilationResults results = settings.getLanguage().decompileType(resolvedType1, (ITextOutput) output,
                        options);
                writer1.flush();
                if (writeToFile) {
                    writer1.close();
                }

                final List<LineNumberPosition> lineNumberPositions = results.getLineNumberPositions();
                if ((commandLineOptions.getIncludeLineNumbers() || commandLineOptions.getStretchLines())
                        && writer1 instanceof FileOutputWriter) {
                    EnumSet<LineNumberOption> lineNumberOptions = EnumSet.noneOf(LineNumberOption.class);
                    if (commandLineOptions.getIncludeLineNumbers()) {
                        lineNumberOptions.add(LineNumberOption.LEADING_COMMENTS);
                    }

                    if (commandLineOptions.getStretchLines()) {
                        lineNumberOptions.add(LineNumberOption.STRETCHED);
                    }

                    LineNumberFormatter lineFormatter = new LineNumberFormatter(((FileOutputWriter) writer1).getFile(),
                            lineNumberPositions, lineNumberOptions);
                    lineFormatter.reformatFile();
                }

            }
        } else {
            System.err.printf("!!! ERROR: Failed to load class %s.\n", typeName);
        }
    }

    private static Writer createWriter(TypeDefinition type, DecompilerSettings settings) throws IOException {
        String outputDirectory = settings.getOutputDirectory();
        if (StringUtilities.isNullOrWhitespace(outputDirectory)) {
            return new OutputStreamWriter(System.out, settings.isUnicodeOutputEnabled() ? Charset.forName("UTF-8")
                    : Charset.defaultCharset());
        } else {
            String fileName = type.getName() + settings.getLanguage().getFileExtension();
            String packageName = type.getPackageName();
            String outputPath;
            if (StringUtilities.isNullOrWhitespace(packageName)) {
                outputPath = PathHelper.combine(outputDirectory, fileName);
            } else {
                outputPath = PathHelper.combine(outputDirectory, packageName.replace('.', PathHelper.DirectorySeparator),
                        fileName);
            }

            File outputFile = new File(outputPath);
            File parentFile = outputFile.getParentFile();
            if (parentFile != null && !parentFile.exists() && !parentFile.mkdirs()) {
                throw new IllegalStateException(String.format("Could not create output directory for file \"%s\".",
                        new Object[] { outputPath }));
            } else if (!outputFile.exists() && !outputFile.createNewFile()) {
                throw new IllegalStateException(
                        String.format("Could not create output file \"%s\".", new Object[] { outputPath }));
            } else {
                return new FileOutputWriter(outputFile, settings);
            }
        }
    }

}
