package com.dcrux.buran.scripting.iface;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Buran.
 *
 * @author: ${USER} Date: 05.07.13 Time: 07:34
 */
public class Code implements Serializable {

    public static Code c() {
        return new Code();
    }

    private List<IFunctionDeclaration<?>> functions = new ArrayList<IFunctionDeclaration<?>>();

    public LineNum addLn(IFunctionDeclaration<?> functionDeclaration) {
        functions.add(functionDeclaration);
        return new LineNum(this.functions.size() - 1);
    }

    public LazyLineNum addLz(IFunctionDeclaration<?> functionDeclaration) {
        final LazyLineNum lazyLineNum = new LazyLineNum();
        functions.add(functionDeclaration);
        lazyLineNum.setLineNum(new LineNum(this.functions.size() - 1));
        return lazyLineNum;
    }

    public Code add(IFunctionDeclaration<?> functionDeclaration) {
        functions.add(functionDeclaration);
        return this;
    }

    public IFunctionDeclaration<?> getLine(int line) {
        return this.functions.get(line);
    }

    public int getNumberOfLines() {
        return this.functions.size();
    }
}
