package com.dcrux.buran.common.subscription;

import java.io.Serializable;

/**
 * Buran.
 *
 * @author: ${USER} Date: 17.07.13 Time: 01:12
 */
public class SubBlockId implements Serializable {

    public static final int MIN_LEN = 1;
    public static final int MAX_LEN = 32;

    private String id;

    public SubBlockId(String id) {
        if ((id.length() < MIN_LEN) || (id.length() > MAX_LEN)) {
            throw new IllegalArgumentException("(id.length()<MIN_LEN) || (id.length()>MAX_LEN)");
        }
        this.id = id;
    }

    private SubBlockId() {
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SubBlockId that = (SubBlockId) o;

        if (!id.equals(that.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "SubBlockId{" +
                "id='" + id + '\'' +
                '}';
    }
}
