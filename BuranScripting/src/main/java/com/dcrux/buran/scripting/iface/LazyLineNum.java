package com.dcrux.buran.scripting.iface;

/**
 * Buran.
 *
 * @author: ${USER} Date: 07.07.13 Time: 23:56
 */
public class LazyLineNum implements ILineNumProvider {
    private LineNum lineNum;

    public static LazyLineNum c() {
        return new LazyLineNum();
    }

    public void setLineNum(LineNum lineNum) {
        if ((this.lineNum != null) && (!this.lineNum.equals(lineNum))) {
            throw new IllegalArgumentException("Another line number is already set.");
        }
        this.lineNum = lineNum;
    }


    @Override
    public LineNum getLineNum() {
        if (this.lineNum == null) {
            throw new IllegalStateException("No line num is set.");
        }
        return this.lineNum;
    }
}
