package com.strobel.decompiler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

final class FileOutputWriter extends OutputStreamWriter {
    private final File file;

    FileOutputWriter(File file, DecompilerSettings settings) throws IOException {
        super(new FileOutputStream(file),
                settings.isUnicodeOutputEnabled() ? Charset.forName("UTF-8") : Charset.defaultCharset());
        this.file = file;
    }

    public File getFile() {
        return this.file;
    }
}
