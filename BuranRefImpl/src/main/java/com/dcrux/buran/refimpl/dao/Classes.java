package com.dcrux.buran.refimpl.dao;

import com.dcrux.buran.ClassId;
import com.dcrux.buran.refimpl.baseModules.BaseModule;

/**
 * Buran.
 *
 * @author: ${USER} Date: 29.06.13 Time: 16:05
 */
public class Classes {
    private final BaseModule impl;

    public Classes(BaseModule impl) {
        this.impl = impl;
    }

    public boolean assureIncClass(ClassId classId) {
        final String className = ClassNameUtils.generateNodeClasName(classId);
        boolean exists = exists(classId);
        if (!exists) {
            impl.getDb().getMetadata().getSchema().createClass(className);
        }
        return !exists;
    }

    public boolean exists(ClassId classId) {
        final String className = ClassNameUtils.generateNodeClasName(classId);
        boolean exists = impl.getDb().getMetadata().getSchema().existsClass(className);
        return exists;
    }
}
