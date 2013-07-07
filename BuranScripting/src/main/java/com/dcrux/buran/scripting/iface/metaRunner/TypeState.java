package com.dcrux.buran.scripting.iface.metaRunner;

import com.dcrux.buran.scripting.iface.*;
import com.sun.istack.internal.Nullable;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Buran.
 *
 * @author: ${USER} Date: 05.07.13 Time: 08:16
 */
public class TypeState implements ITypeState {

    private final Map<String, IType<?>> typesMap = new HashMap<>();
    private final Set<String> finalfields = new HashSet<>();

    private IType retType;
    private LineNum jumpTo;
    private LineNum branchTo;

    private final ComplexityTracker compTracker;

    public TypeState(ComplexityTracker compTracker) {
        this.compTracker = compTracker;
    }

    public TypeState getStateForBranch() {
        final TypeState state = new TypeState((ComplexityTracker) this.compTracker.createBranch());
        state.typesMap.putAll(this.typesMap);
        state.finalfields.addAll(this.finalfields);
        return state;
    }

    public ComplexityTracker getComplexityTracker() {
        return this.compTracker;
    }

    @Override
    public IType getType(VarName name) {
        return typesMap.get(name.getName());
    }

    @Override
    public void setType(VarName name, IType type, boolean isFinal) throws ProgrammErrorException {
        if (finalfields.contains(name.getName())) {
            throw new ProgrammErrorException(MessageFormat
                    .format("Variable {0} is final and " + "cannot be reassigned.", name));
        }
        if (typesMap.containsKey(name.getName())) {
            removeType(name, false);
        }
        if (isFinal) {
            this.finalfields.add(name.getName());
        }
        this.typesMap.put(name.getName(), type);
        getCompTracker().allocateMemory(type.getMemoryMaxMemoryRequirement());
    }

    private void removeType(VarName name, boolean doThrow) throws ProgrammErrorException {
        if (finalfields.contains(name.getName())) {
            if (doThrow) {
                throw new ProgrammErrorException(MessageFormat
                        .format("Variable {0} is final and " + "cannot be removed.", name));
            }
        }
        final IType<?> removed = this.typesMap.remove(name.getName());
        if (removed == null) {
            throw new ProgrammErrorException(MessageFormat
                    .format("Could not remove variable {0} - " + "variable not assigned", name));
        }
        getCompTracker().freeMemory(removed.getMemoryMaxMemoryRequirement());
    }

    @Override
    public void removeType(VarName name) throws ProgrammErrorException {
        removeType(name, true);
    }

    @Nullable
    public IType getRetType() {
        return retType;
    }

    private void assureNoControlFlow() throws ProgrammErrorException {
        if (this.branchTo != null) {
            throw new ProgrammErrorException("A branch statement is already active. Only one " +
                    "control flow per line is allowed.");
        }
        if (this.retType != null) {
            throw new ProgrammErrorException("A return statement is already active. Only one " +
                    "control flow per line is allowed.");
        }
        if (this.jumpTo != null) {
            throw new ProgrammErrorException("A jump statement is already active. Only one " +
                    "control flow per line is allowed.");
        }
    }

    @Override
    public void ret(IType retType) throws ProgrammErrorException {
        assureNoControlFlow();
        this.retType = retType;
    }

    @Override
    public void jumpTo(ILineNumProvider jumpTo) throws ProgrammErrorException {
        assureNoControlFlow();
        this.jumpTo = jumpTo.getLineNum();
    }

    @Override
    public void branch(ILineNumProvider jumpTo) throws ProgrammErrorException {
        assureNoControlFlow();
        this.branchTo = jumpTo.getLineNum();
    }

    public LineNum getJumpTo() {
        return jumpTo;
    }

    public LineNum getBranchTo() {
        return branchTo;
    }

    public void continueAfterBranch() {
        this.branchTo = null;
    }

    @Override
    public IComplexityTracker getCompTracker() {
        return this.compTracker;
    }

    public void clearJumpTo() {
        this.jumpTo = null;
    }
}
