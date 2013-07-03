package com.dcrux.buran.commands.dataMut;

import com.dcrux.buran.commandBase.VoidType;
import com.dcrux.buran.commands.Command;
import com.dcrux.buran.common.IIncNid;
import com.dcrux.buran.common.getterSetter.IDataSetter;

import java.util.Set;

/**
 * Buran.
 *
 * @author: ${USER} Date: 02.07.13 Time: 17:39
 */
public class ComMutate extends Command<VoidType> {

    public static final Set<Class<? extends Exception>> EXCEPTIONS = exceptions();

    private final IIncNid incNid;
    private IDataSetter setter;

    private ComMutate(IIncNid incNid, IDataSetter dataSetter) {
        super(EXCEPTIONS);
        this.incNid = incNid;
        this.setter = dataSetter;
    }

    public static ComMutate c(IIncNid incNid, IDataSetter dataSetter) {
        return new ComMutate(incNid, dataSetter);
    }

    public IIncNid getIncNid() {
        return incNid;
    }

    public IDataSetter getSetter() {
        return setter;
    }
}
