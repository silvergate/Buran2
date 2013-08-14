package com.dcrux.buran.common.domain;

import java.io.Serializable;

/**
 * Buran.
 *
 * @author: ${USER} Date: 13.08.13 Time: 21:42
 */
@Deprecated
public class DomainAppend implements Serializable {
    public static final int MAX_LEN = 16;
    private String append;

    public static final DomainAppend DEFAULT = new DomainAppend("");

    public DomainAppend(String append) {
        if (append.length() > MAX_LEN) {
            throw new IllegalArgumentException("append.length()>MAX_LEN");
        }
        this.append = append;
    }

    private DomainAppend() {
    }

    public String getAppend() {
        return append;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DomainAppend that = (DomainAppend) o;

        if (!append.equals(that.append)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return append.hashCode();
    }

    @Override
    public String toString() {
        return "DomainAppend{" +
                "append='" + append + '\'' +
                '}';
    }
}
