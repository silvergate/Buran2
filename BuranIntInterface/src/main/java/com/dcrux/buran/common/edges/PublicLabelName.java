package com.dcrux.buran.common.edges;

/**
 * Buran.
 *
 * @author: ${USER} Date: 01.07.13 Time: 02:03
 */
public class PublicLabelName implements ILabelName {

    public static final int MIN_LEN = 1;
    public static final int MAX_LEN = 40;

    private String name;

    public PublicLabelName(String name) {
        if ((name.length() < MIN_LEN) || (name.length() > MAX_LEN)) {
            throw new IllegalArgumentException(
                    "((name.length()<MIN_LEN) || (name.length()" + ">MAX_LEN))");
        }
        this.name = name;
    }

    private PublicLabelName() {
    }

    public String getName() {
        return name;
    }
}
