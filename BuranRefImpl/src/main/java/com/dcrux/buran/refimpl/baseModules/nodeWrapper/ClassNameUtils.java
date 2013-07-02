package com.dcrux.buran.refimpl.baseModules.nodeWrapper;

import com.dcrux.buran.common.classes.ClassId;
import com.dcrux.buran.refimpl.baseModules.fields.DocFields;

/**
 * Buran.
 *
 * @author: ${USER} Date: 29.06.13 Time: 16:08
 */
public class ClassNameUtils {
    public static String generateNodeClasName(ClassId classId) {
        return DocFields.NODE_CLASS_PREFIX + classId.getId();
    }

    public static ClassId getNodeClassId(String oClassName) {
        final String numberStr;
        numberStr = oClassName.substring(DocFields.NODE_CLASS_PREFIX.length());
        return new ClassId(Long.parseLong(numberStr, 10));
    }
}
