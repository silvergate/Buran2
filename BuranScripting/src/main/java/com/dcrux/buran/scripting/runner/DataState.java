package com.dcrux.buran.scripting.runner;

import com.dcrux.buran.scripting.iface.IDataState;
import com.dcrux.buran.scripting.iface.VarName;

import java.util.HashMap;
import java.util.Map;

/**
 * Buran.
 *
 * @author: ${USER} Date: 05.07.13 Time: 12:34
 */
public class DataState implements IDataState {

    private final Map<String, Object> varValues = new HashMap<>();
    private Integer jumpTo;
    private Object ret;
    private boolean isRet;

    @Override
    public Object getValue(VarName name) {
        return this.varValues.get(name.getName());
    }

    @Override
    public void setValue(VarName name, Object value) {
        System.out.println("Setting " + name + " to " + value);
        this.varValues.put(name.getName(), value);
    }

    @Override
    public void removeValue(VarName name) {
        this.varValues.remove(name.getName());
    }

    @Override
    public void jumpTo(int lineNum) {
        this.jumpTo = lineNum;
    }

    public void clearJumpTo() {
        this.jumpTo = null;
    }

    public boolean isRet() {
        return isRet;
    }

    public Object getRet() {
        return ret;
    }

    public Integer getJumpTo() {
        return jumpTo;
    }

    @Override
    public void ret(Object value) {
        this.ret = value;
        this.isRet = true;
    }
}
