package com.dcrux.buran.common.inRelations.selector;

import com.dcrux.buran.common.NidVer;

import java.util.ArrayList;

/**
 * Buran.
 *
 * @author: ${USER} Date: 04.08.13 Time: 22:59
 */
public class InRelSelTarget implements IInRelSelector<ArrayList<NidVer>> {
    public static final InRelSelTarget SINGLETON = new InRelSelTarget();
}
