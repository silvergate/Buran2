package com.dcrux.buran.common.link;

import com.dcrux.buran.common.IExtNidOrNidVer;
import com.dcrux.buran.common.classes.ClassId;

import java.io.Serializable;

/**
 * Buran.
 *
 * @author: ${USER} Date: 05.08.13 Time: 01:10
 */
public class ExtNidLinkTarget implements Serializable {
    private IExtNidOrNidVer extNidOrNidVer;
    private ClassId targetClassId;

    public ExtNidLinkTarget(IExtNidOrNidVer extNidOrNidVer, ClassId targetClassId) {
        this.extNidOrNidVer = extNidOrNidVer;
        this.targetClassId = targetClassId;
    }

    private ExtNidLinkTarget() {
    }

    public IExtNidOrNidVer getExtNidOrNidVer() {
        return extNidOrNidVer;
    }

    public ClassId getTargetClassId() {
        return targetClassId;
    }
}
