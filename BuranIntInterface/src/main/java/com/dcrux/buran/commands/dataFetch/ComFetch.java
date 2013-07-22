package com.dcrux.buran.commands.dataFetch;

import com.dcrux.buran.commands.Command;
import com.dcrux.buran.common.INidOrNidVer;
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

    private INidOrNidVer nidVer;
    private IDataGetter<TRetVal> getter;

    private ComFetch() {
    }

    public static <TRetVal extends Serializable> ComFetch<TRetVal> c(INidOrNidVer nidVer,
            IDataGetter<TRetVal> dataGetter) {
        return new ComFetch<TRetVal>(nidVer, dataGetter);
    }

    public ComFetch(INidOrNidVer nidVer, IDataGetter<TRetVal> dataGetter) {
        super(EXCEPTIONS);
        this.nidVer = nidVer;
        this.getter = dataGetter;
    }

    public INidOrNidVer getNidVer() {
        return nidVer;
    }

    public IDataGetter<TRetVal> getGetter() {
        return getter;
    }
}
