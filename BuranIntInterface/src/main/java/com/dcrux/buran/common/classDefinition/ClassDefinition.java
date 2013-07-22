package com.dcrux.buran.common.classDefinition;

import java.io.Serializable;

/**
 * Buran.
 *
 * @author: ${USER} Date: 02.07.13 Time: 19:47
 */
public class ClassDefinition implements Serializable {

    public static final int SHORT_MIN = 6;
    public static final int SHORT_MAX = 32;
    public static final int LONG_MIN = 32;
    public static final int LONG_MAX = 512;

    private String shortDesc;
    private String longDesc;

    public ClassDefinition(String shortDesc, String longDesc) {
        if ((shortDesc.length() < SHORT_MIN) || (shortDesc.length() > SHORT_MAX)) {
            throw new IllegalArgumentException(
                    "(shortDesc.length()<SHORT_MIN) || (shortDesc" + ".length()>SHORT_MAX)");
        }
        if ((longDesc.length() < LONG_MIN) || (longDesc.length() > LONG_MAX)) {
            throw new IllegalArgumentException(
                    "(longDesc.length()<LONG_MIN) || (longDesc" + ".length()>LONG_MAX)");
        }
        this.shortDesc = shortDesc;
        this.longDesc = longDesc;
        this.fields = new ClassFieldsDefinition();
        this.indexes = new ClassIndexDefinition();
    }

    private ClassFieldsDefinition fields;
    private ClassIndexDefinition indexes;

    private ClassDefinition() {
    }

    public ClassFieldsDefinition getFields() {
        return fields;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public String getLongDesc() {
        return longDesc;
    }

    public ClassIndexDefinition getIndexes() {
        return indexes;
    }

    @Override
    public String toString() {
        return "ClassDefinition{" +
                "shortDesc='" + shortDesc + '\'' +
                ", longDesc='" + longDesc + '\'' +
                ", fields=" + fields +
                ", indexes=" + indexes +
                '}';
    }
}
