package com.dcrux.buran.refimpl.baseModules.newIndexing;

import com.dcrux.buran.common.UserId;
import com.dcrux.buran.query.queries.QueryTarget;

import java.text.MessageFormat;

/**
 * Buran.
 *
 * @author: ${USER} Date: 08.08.13 Time: 20:19
 */
public class FieldBuilder implements IFieldBuilder {

    public static final FieldBuilder SINGLETON = new FieldBuilder();

    public static String toHex(long value) {
        return Long.toHexString(value);
    }

    public static String getIndexStatic(UserId userId) {
        return toHex(userId.getId());
    }

    public static String getTypeStatic() {
        return "default";
    }

    @Override
    public String getField(UserId receiver, QueryTarget target) {
        final String classStr = toHex(target.getClassId().getId());
        final String indexStr = Short.toString(target.getIndexId().getId());
        final String indexIdStr = Short.toString(target.getIndexedFieldId().getId());
        return MessageFormat.format("{0}-{1}-{2}", classStr, indexStr, indexIdStr);
    }

    @Override
    public String getIndex(UserId userId) {
        return getIndexStatic(userId);
    }

    @Override
    public String getType() {
        return getTypeStatic();
    }
}
