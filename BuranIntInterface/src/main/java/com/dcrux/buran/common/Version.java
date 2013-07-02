package com.dcrux.buran.common;

import java.io.Serializable;

/**
 * Buran.
 *
 * @author: ${USER} Date: 29.06.13 Time: 16:14
 */
public class Version implements Serializable {
    private final long version;

    public static final Version INITIAL = new Version(0);

    public Version(long version) {
        this.version = version;
    }

    public long getVersion() {
        return version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Version version1 = (Version) o;

        if (version != version1.version) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (version ^ (version >>> 32));
    }

    @Override
    public String toString() {
        return "Version{" +
                "version=" + version +
                '}';
    }
}
