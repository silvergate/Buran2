package com.dcrux.buran.scripting.iface;

import java.util.ArrayList;
import java.util.List;

/**
 * Buran.
 *
 * @author: ${USER} Date: 05.07.13 Time: 07:34
 */
public class Block {

    public static Block c() {
        return new Block();
    }

    private final List<IFunctionDeclaration<?>> functions = new ArrayList<>();

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

    public Block add(IFunctionDeclaration<?> functionDeclaration) {
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
