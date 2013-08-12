package com.dcrux.buran.refimpl.baseModules.textExtractor;

import org.apache.tika.io.IOUtils;

import java.io.IOException;
import java.io.Reader;

/**
 * Buran.
 *
 * @author: ${USER} Date: 09.08.13 Time: 18:30
 */
public class ParseResult {
    private final Reader reader;
    private final org.apache.tika.metadata.Metadata metadata;
    private Metadata meta;
    private boolean readerMarkedAsRead;

    public ParseResult(Reader reader, org.apache.tika.metadata.Metadata metadata) {
        this.reader = reader;
        this.metadata = metadata;
    }

    public Reader getReader() throws IOException {
        if (this.readerMarkedAsRead) {
            throw new IllegalStateException("Reader is already read");
        }
        return reader;
    }

    public void markReaderAsRead() {
        this.readerMarkedAsRead = true;
    }

    public Metadata getMetadata() throws IOException {
        if (!this.readerMarkedAsRead) {
            /* Read reader */
            IOUtils.toByteArray(getReader());
        }
        if (this.meta == null) {
            this.meta = new Metadata();
            TextUtils.toMeta(this.metadata, this.meta);
        }
        return this.meta;
    }

    public boolean isSupported() {
        return this.reader != null;
    }
}
