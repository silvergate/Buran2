package com.dcrux.buran.common.fields.setter;

import com.dcrux.buran.common.fields.typeDef.ITypeDef;
import com.dcrux.buran.common.fields.types.LinkType;
import com.dcrux.buran.common.link.LinkTargetInc;

/**
 * Buran.
 *
 * @author: ${USER} Date: 29.06.13 Time: 14:52
 */
public class FieldSetLink implements IUnfieldedDataSetter {

    private LinkTargetInc linkTargetInc;

    public FieldSetLink(LinkTargetInc linkTargetInc) {
        this.linkTargetInc = linkTargetInc;
    }

    private FieldSetLink() {
    }

    public static FieldSetLink c(LinkTargetInc linkTargetInc) {
        return new FieldSetLink(linkTargetInc);
    }

    @Override
    public boolean canApplyTo(ITypeDef dataType) {
        return dataType instanceof LinkType;
    }

    public LinkTargetInc getLinkTargetInc() {
        return linkTargetInc;
    }
}
