package com.dcrux.buran.refimpl.dao;

import com.dcrux.buran.ClassId;
import com.dcrux.buran.refimpl.baseModules.fields.DocFields;

/**
 * Buran.
 *
 * @author: ${USER} Date: 29.06.13 Time: 16:08
 */
public class ClassNameUtils {
    public static String generateNodeClasName(ClassId classId) {
        Mode mode = Mode.live;
        switch (mode) {
            case incubation:
                return DocFields.INC_NODE_CLASS_PREFIX + classId.getId();
            case live:
                return DocFields.NODE_CLASS_PREFIX + classId.getId();
            default:
                throw new IllegalArgumentException();
        }
    }

    public static ClassId getNodeClassId(String oClassName) {
        Mode mode = Mode.live;
        final String numberStr;
        switch (mode) {
            case incubation:
                numberStr = oClassName.substring(DocFields.INC_NODE_CLASS_PREFIX.length());
                break;
            case live:
                numberStr = oClassName.substring(DocFields.NODE_CLASS_PREFIX.length());
                break;
            default:
                throw new IllegalArgumentException();
        }
        return new ClassId(Long.parseLong(numberStr, 10));
    }
}
