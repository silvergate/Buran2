package com.dcrux.buran.refimpl.baseModules.deltaRecorder;

import com.dcrux.buran.common.getterSetter.IDataSetter;
import com.dcrux.buran.refimpl.baseModules.common.ONid;

/**
 * Buran.
 *
 * @author: ${USER} Date: 03.07.13 Time: 14:26
 */
public interface IRecordPlayer {
    void entry(ONid onid, IDataSetter setter) throws Exception;
}
