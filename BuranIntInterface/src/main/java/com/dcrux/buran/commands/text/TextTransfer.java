package com.dcrux.buran.commands.text;

import com.dcrux.buran.common.fields.types.StringType;

/**
 * Buran.
 *
 * @author: ${USER} Date: 11.08.13 Time: 22:42
 */
public class TextTransfer extends ITransferConfig {
    private int limitChars;
    private boolean append;

    public TextTransfer(int limitChars, boolean append) {
        if (limitChars > StringType.MAXLEN_LIMIT) {
            throw new IllegalArgumentException("limitChars> StringType.MAXLEN_LIMIT");
        }
        this.limitChars = limitChars;
        this.append = append;
    }

    private TextTransfer() {
    }

    public int getLimitChars() {
        return limitChars;
    }

    public boolean isAppend() {
        return append;
    }
}
