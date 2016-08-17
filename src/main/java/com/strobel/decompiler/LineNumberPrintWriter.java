package com.strobel.decompiler;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.Arrays;

class LineNumberPrintWriter extends PrintWriter {

    private final String    _emptyPrefix;
    private final String    _format;
    private boolean         _needsPrefix;
    private boolean         _suppressLineNumbers;

    LineNumberPrintWriter(int maxLineNo, Writer w) {
        super(w);
        String maxNumberString = String.format("%d", maxLineNo);
        int numberWidth = maxNumberString.length();
        this._format = "/*%" + numberWidth + "d*/";
        String samplePrefix = String.format(this._format, maxLineNo);
        char[] prefixChars = samplePrefix.toCharArray();
        Arrays.fill(prefixChars, ' ');
        this._emptyPrefix = new String(prefixChars);
        this._needsPrefix = true;
    }

    void suppressLineNumbers() {
        this._suppressLineNumbers = true;
    }

    public void print(String s) {
        this.print(-1, s);
    }

    public void println(String s) {
        this.println(-1, s);
    }

    void println(int lineNumber, String s) {
        this.doPrefix(lineNumber);
        super.println(s);
        this._needsPrefix = true;
    }

    void print(int lineNumber, String s) {
        this.doPrefix(lineNumber);
        super.print(s);
    }

    private void doPrefix(int lineNumber) {
        if (this._needsPrefix && !this._suppressLineNumbers) {
            if (lineNumber == -1) {
                super.print(this._emptyPrefix);
            } else {
                String prefix = String.format(this._format, lineNumber);
                super.print(prefix);
            }
        }

        this._needsPrefix = false;
    }
}
