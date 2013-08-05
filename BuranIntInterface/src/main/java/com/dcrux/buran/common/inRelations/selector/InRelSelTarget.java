package com.dcrux.buran.common.inRelations.selector;

import com.dcrux.buran.common.Nid;

import java.util.ArrayList;

/**
 * Buran.
 *
 * @author: ${USER} Date: 04.08.13 Time: 22:59
 */
public class InRelSelTarget implements IInRelSelector<ArrayList<Nid>> {
    public static final InRelSelTarget SINGLETON = new InRelSelTarget();
}
