package com.dcrux.buran.commands.dataFetch;

import com.dcrux.buran.commands.Command;
import com.dcrux.buran.common.NidVer;
import com.dcrux.buran.common.getterSetter.IDataGetter;

import java.io.Serializable;
import java.util.Set;

/**
 * Buran.
 *
 * @author: ${USER} Date: 02.07.13 Time: 18:18
 */
public class ComFetch<TRetVal extends Serializable> extends Command<TRetVal> {

    public static final Set<Class<? extends Exception>> EXCEPTIONS = exceptions();

    private final NidVer nidVer;
    private final IDataGetter<TRetVal> getter;

    public static <TRetVal extends Serializable> ComFetch<TRetVal> c(NidVer nidVer,
            IDataGetter<TRetVal> dataGetter) {
        return new ComFetch<>(nidVer, dataGetter);
    }

    public ComFetch(NidVer nidVer, IDataGetter<TRetVal> dataGetter) {
        super(EXCEPTIONS);
        this.nidVer = nidVer;
        this.getter = dataGetter;
    }

    public NidVer getNidVer() {
        return nidVer;
    }

    public IDataGetter<TRetVal> getGetter() {
        return getter;
    }
}
