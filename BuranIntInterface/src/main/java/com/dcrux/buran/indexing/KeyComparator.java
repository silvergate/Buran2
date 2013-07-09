package com.dcrux.buran.indexing;

/**
 * Buran.
 *
 * @author: ${USER} Date: 09.07.13 Time: 16:55
 */
public class KeyComparator {
    public static int compareTo(byte[] b1, byte[] b2) {
        final int size = b1.length;
        for (int i = 0; i < size; ++i) {
            if (b1[i] > b2[i]) return 1;
            else if (b1[i] < b2[i]) return -1;
        }
        return 0;
    }
}
