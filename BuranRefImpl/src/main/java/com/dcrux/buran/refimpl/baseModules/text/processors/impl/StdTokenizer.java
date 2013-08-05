package com.dcrux.buran.refimpl.baseModules.text.processors.impl;

import com.dcrux.buran.refimpl.baseModules.text.processors.ITokenizer;
import com.sun.istack.internal.Nullable;

/**
 * Buran.
 *
 * @author: ${USER} Date: 05.08.13 Time: 18:22
 */
public abstract class StdTokenizer implements ITokenizer {

    protected static class CDataBeginInfo {
        private final int typeInt;
        private final int cdataBeginPos;

        public CDataBeginInfo(int typeInt, int cdataBeginPos) {
            this.typeInt = typeInt;
            this.cdataBeginPos = cdataBeginPos;
        }

        public int getTypeInt() {
            return typeInt;
        }

        public int getCdataBeginPos() {
            return cdataBeginPos;
        }
    }

    public static final char CDATA_INIT_1 = '#';
    public static final char CDATA_INIT_2 = '[';
    public static final char CDATA_SEP = '|';
    public static final char CDATA_END_1 = ']';
    public static final char CDATA_END_2 = '#';
    public static final char ESCAPE = '\\';

    @Nullable
    protected CDataBeginInfo isCdataInit(String input, int pos) {
        /* Correct pattern: #[FFFFFFFF|??? */

        /* First character is ok? */
        if (input.charAt(pos) != CDATA_INIT_1) {
            return null;
        }
        /* Length is ok? */
        final int remainingLength = input.length() - pos;
        if (remainingLength < 14) {
            return null;
        }
        /* Escaped? */
        if ((pos > 0) && (input.charAt(pos - 1) == ESCAPE)) {
            return null;
        }
        /* Second character is ok? */
        if (input.charAt(pos + 1) != CDATA_INIT_2) {
            return null;
        }
        /* Separator ok? */
        final char separatorChar = input.charAt(pos + 10);
        if (separatorChar != CDATA_SEP) {
            return null;
        }
        /* Try to parse type number */
        final String typeNumStr = input.substring(pos + 2, pos + 10);
        try {
            final long typeNumLong = Long.parseLong(typeNumStr, 16);
            final int typeNumInt = (int) (typeNumLong - Integer.MAX_VALUE - 1);
            return new CDataBeginInfo(typeNumInt, pos + 11);
        } catch (NumberFormatException nfe) {
            return null;
        }
    }

    protected boolean isCdataEnd(String input, int pos) {
        /* Correct pattern: ?????]# */

        /* First character is ok? */
        if (input.charAt(pos) != CDATA_END_1) {
            return false;
        }
        /* Pos is ok? */
        if (pos < 5) {
            return false;
        }
        /* Escaped? */
        if ((input.charAt(pos - 1) == ESCAPE)) {
            return false;
        }
        /* Second character is ok? */
        if (input.charAt(pos + 1) != CDATA_END_2) {
            return false;
        }
        return true;
    }
}
