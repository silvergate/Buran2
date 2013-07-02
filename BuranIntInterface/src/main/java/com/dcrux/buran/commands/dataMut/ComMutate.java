package com.dcrux.buran.commands.dataMut;

import com.dcrux.buran.commandBase.VoidType;
import com.dcrux.buran.commands.Command;
import com.dcrux.buran.common.IIncNid;
import com.dcrux.buran.common.fields.IDataSetter;
import com.dcrux.buran.common.labels.ILabelSet;
import com.google.common.base.Optional;

import java.util.Set;

/**
 * Buran.
 *
 * @author: ${USER} Date: 02.07.13 Time: 17:39
 */
public class ComMutate extends Command<VoidType> {

    public static final Set<Class<? extends Exception>> EXCEPTIONS = exceptions();

    private final IIncNid incNid;
    private Optional<IDataSetter> dataSetter;
    private Optional<ILabelSet> labelSetter;

    private ComMutate(IIncNid incNid, Optional<IDataSetter> dataSetter,
            Optional<ILabelSet> labelSetter) {
        super(EXCEPTIONS);
        this.incNid = incNid;
        this.dataSetter = dataSetter;
        this.labelSetter = labelSetter;
    }

    public static ComMutate field(IIncNid incNid, IDataSetter dataSetter) {
        return new ComMutate(incNid, Optional.of(dataSetter), Optional.<ILabelSet>absent());
    }

    public static ComMutate label(IIncNid incNid, ILabelSet labelSetter) {
        return new ComMutate(incNid, Optional.<IDataSetter>absent(), Optional.of(labelSetter));
    }

    public ComMutate field(IDataSetter dataSetter) {
        this.dataSetter = Optional.of(dataSetter);
        return this;
    }

    public ComMutate label(ILabelSet labelSetter) {
        this.labelSetter = Optional.of(labelSetter);
        return this;
    }

    public IIncNid getIncNid() {
        return incNid;
    }

    public Optional<IDataSetter> getDataSetter() {
        return dataSetter;
    }

    public Optional<ILabelSet> getLabelSetter() {
        return labelSetter;
    }
}
