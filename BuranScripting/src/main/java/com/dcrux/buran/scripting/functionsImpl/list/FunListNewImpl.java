package com.dcrux.buran.scripting.functionsImpl.list;

import com.dcrux.buran.scripting.functions.list.FunListNew;
import com.dcrux.buran.scripting.iface.IDataState;
import com.dcrux.buran.scripting.iface.IEvaluable;
import com.dcrux.buran.scripting.iface.IFunctionImplFactory;
import com.dcrux.buran.scripting.iface.IFunctionImplementation;
import com.dcrux.buran.scripting.iface.types.ListType;

/**
 * Buran.
 *
 * @author: ${USER} Date: 04.07.13 Time: 18:31
 */
public class FunListNewImpl implements IFunctionImplementation<Object[], ListType, FunListNew> {

    private final int numberOfInputs;

    public static IFunctionImplFactory<Object[], ListType, FunListNew> FACTORY =
            new IFunctionImplFactory<Object[], ListType, FunListNew>() {
                @Override
                public IFunctionImplementation<Object[], ListType, FunListNew> getImplementation(
                        FunListNew function) {
                    return new FunListNewImpl(function.getNumberOfInputs());
                }

                @Override
                public Class<FunListNew> getFuncDeclClass() {
                    return FunListNew.class;
                }
            };

    public FunListNewImpl(int numberOfInputs) {
        this.numberOfInputs = numberOfInputs;
    }

    @Override
    public Object run(IDataState state, IEvaluable... input) {
        final Object[] ret = new Object[this.numberOfInputs];
        for (int i = 0; i < this.numberOfInputs; i++) {
            ret[i] = input[i].eval(state);
        }

        return ret;
    }
}
