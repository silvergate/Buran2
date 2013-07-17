package com.dcrux.buran.commands.dataMut;

import com.dcrux.buran.commandBase.VoidType;
import com.dcrux.buran.commands.Command;
import com.dcrux.buran.common.IncNid;
import com.dcrux.buran.common.getterSetter.IDataSetter;

import java.util.Set;

/**
 * Buran.
 *
 * @author: ${USER} Date: 02.07.13 Time: 17:39
 */
public class ComMutate extends Command<VoidType> {

    public static final Set<Class<? extends Exception>> EXCEPTIONS = exceptions();

    private IncNid incNid;
    private IDataSetter setter;

    private ComMutate(IncNid incNid, IDataSetter dataSetter) {
        super(EXCEPTIONS);
        this.incNid = incNid;
        this.setter = dataSetter;
    }

    private ComMutate() {
    }

    public static ComMutate c(IncNid incNid, IDataSetter dataSetter) {
        return new ComMutate(incNid, dataSetter);
    }

    public IncNid getIncNid() {
        return incNid;
    }

    public IDataSetter getSetter() {
        return setter;
    }
}
