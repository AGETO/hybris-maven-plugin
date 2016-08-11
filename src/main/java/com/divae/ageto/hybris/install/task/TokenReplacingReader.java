package com.divae.ageto.hybris.install.task;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;
import java.nio.CharBuffer;
import java.util.Map;

/**
 * @author Klaus Hauschild
 */
class TokenReplacingReader extends Reader {

    private final PushbackReader      pushbackReader;
    private final Map<String, String> token;
    private final StringBuilder       tokenNameBuffer = new StringBuilder();

    private String                    tokenValue;
    private int                       tokenValueIndex;

    TokenReplacingReader(final Reader source, final Map<String, String> token) {
        this.pushbackReader = new PushbackReader(source, 2);
        this.token = token;
    }

    public int read(CharBuffer target) throws IOException {
        throw new RuntimeException("Operation Not Supported");
    }

    public int read() throws IOException {
        if (tokenValue != null) {
            if (tokenValueIndex < tokenValue.length()) {
                return tokenValue.charAt(tokenValueIndex++);
            }
            if (tokenValueIndex == tokenValue.length()) {
                tokenValue = null;
                tokenValueIndex = 0;
            }
        }

        int data = pushbackReader.read();
        if (data != '$')
            return data;

        data = pushbackReader.read();
        if (data != '{') {
            pushbackReader.unread(data);
            return '$';
        }
        tokenNameBuffer.delete(0, tokenNameBuffer.length());

        data = pushbackReader.read();
        while (data != '}') {
            tokenNameBuffer.append((char) data);
            data = pushbackReader.read();
        }

        tokenValue = token.get(tokenNameBuffer.toString());

        if (tokenValue == null) {
            tokenValue = "${" + tokenNameBuffer.toString() + "}";
        }
        if (tokenValue.length() == 0) {
            return read();
        }
        return tokenValue.charAt(tokenValueIndex++);

    }

    public int read(char cbuf[]) throws IOException {
        return read(cbuf, 0, cbuf.length);
    }

    public int read(final char cbuf[], final int off, final int len) throws IOException {
        int charsRead = 0;
        for (int i = 0; i < len; i++) {
            final int nextChar = read();
            if (nextChar == -1) {
                if (charsRead == 0) {
                    charsRead = -1;
                }
                break;
            }
            charsRead = i + 1;
            cbuf[off + i] = (char) nextChar;
        }
        return charsRead;
    }

    public void close() throws IOException {
        pushbackReader.close();
    }

    public long skip(final long n) throws IOException {
        throw new RuntimeException("Operation Not Supported");
    }

    public boolean ready() throws IOException {
        return pushbackReader.ready();
    }

    public boolean markSupported() {
        return false;
    }

    public void mark(int readAheadLimit) throws IOException {
        throw new RuntimeException("Operation Not Supported");
    }

    public void reset() throws IOException {
        throw new RuntimeException("Operation Not Supported");
    }

}
