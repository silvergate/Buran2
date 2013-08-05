package com.dcrux.buran.refimpl.baseModules.fields;

import com.dcrux.buran.refimpl.baseModules.fields.fieldPerformer.BinaryFieldPerformer;
import com.dcrux.buran.refimpl.baseModules.fields.fieldPerformer.IntegerFieldPerformer;
import com.dcrux.buran.refimpl.baseModules.fields.fieldPerformer.LinkFieldPerformer;
import com.dcrux.buran.refimpl.baseModules.fields.fieldPerformer.StringFieldPerformer;

/**
 * Buran.
 *
 * @author: ${USER} Date: 03.07.13 Time: 19:13
 */
public class FieldPerformerRegistryUtil {
    public static void register(FieldPerformerRegistry registry) {
        registry.register(StringFieldPerformer.SINGLETON);
        registry.register(IntegerFieldPerformer.SINGLETON);
        registry.register(BinaryFieldPerformer.SINGLETON);
        registry.register(LinkFieldPerformer.SINGLETON);
    }
}
