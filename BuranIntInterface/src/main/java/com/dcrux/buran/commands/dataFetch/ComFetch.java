package com.dcrux.buran.commands.dataFetch;

import com.dcrux.buran.commandBase.VoidType;
import com.dcrux.buran.commands.Command;
import com.dcrux.buran.common.NidVer;
import com.dcrux.buran.common.fields.IFieldGetter;
import com.dcrux.buran.common.labels.ILabelGet;
import com.google.common.base.Optional;

import java.io.Serializable;
import java.util.Set;

/**
 * Buran.
 *
 * @author: ${USER} Date: 02.07.13 Time: 18:18
 */
public class ComFetch<TFieldResult extends Serializable, TLabelResult extends Serializable>
        extends Command<FetchResult<TFieldResult, TLabelResult>> {

    public static final Set<Class<? extends Exception>> EXCEPTIONS = exceptions();

    private final NidVer nidVer;
    private Optional<IFieldGetter<TFieldResult>> fieldGetter;
    private Optional<ILabelGet<TLabelResult>> labelGetter;

    public ComFetch(NidVer nidVer, Optional<IFieldGetter<TFieldResult>> fieldGetter,
            Optional<ILabelGet<TLabelResult>> labelGetter) {
        super(EXCEPTIONS);
        this.nidVer = nidVer;
        this.fieldGetter = fieldGetter;
        this.labelGetter = labelGetter;
    }

    public static <TFieldResultLocal extends Serializable> ComFetch<TFieldResultLocal,
            VoidType> field(
            NidVer nidVer, IFieldGetter<TFieldResultLocal> fieldGetter) {
        return new ComFetch(nidVer, Optional.<IFieldGetter<TFieldResultLocal>>of(fieldGetter),
                Optional.<ILabelGet<VoidType>>absent());
    }

    public static <TLabelResultLocal extends Serializable> ComFetch<VoidType,
            TLabelResultLocal> label(
            NidVer nidVer, ILabelGet<TLabelResultLocal> fieldGetter) {
        return new ComFetch(nidVer, Optional.<ILabelGet<VoidType>>absent(),
                Optional.<ILabelGet<TLabelResultLocal>>of(fieldGetter));
    }

    public <TFieldResultLocal extends Serializable> ComFetch<TFieldResultLocal, TLabelResult> field(
            IFieldGetter<TFieldResultLocal> fieldGetter) {
        return new ComFetch<TFieldResultLocal, TLabelResult>(getNidVer(),
                (Optional<IFieldGetter<TFieldResultLocal>>) fieldGetter, this.labelGetter);
    }

    public <TLabelResultLocal extends Serializable> ComFetch<TFieldResult, TLabelResultLocal> label(
            ILabelGet<TLabelResultLocal> labelGetter) {
        return new ComFetch<TFieldResult, TLabelResultLocal>(getNidVer(), this.fieldGetter,
                (Optional<ILabelGet<TLabelResultLocal>>) labelGetter);
    }

    public NidVer getNidVer() {
        return nidVer;
    }

    public Optional<IFieldGetter<TFieldResult>> getFieldGetter() {
        return fieldGetter;
    }

    public Optional<ILabelGet<TLabelResult>> getLabelGetter() {
        return labelGetter;
    }
}
