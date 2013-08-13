package com.dcrux.buran.refimpl.baseModules.nodeWrapper;

import com.dcrux.buran.common.UserId;
import com.dcrux.buran.common.classes.ClassId;
import com.dcrux.buran.common.domain.DomainId;
import com.dcrux.buran.common.fields.FieldIndex;
import com.dcrux.buran.refimpl.baseModules.common.DocumentWrapper;
import com.dcrux.buran.refimpl.baseModules.common.OIncNid;
import com.orientechnologies.orient.core.metadata.schema.OType;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.sun.istack.internal.Nullable;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Buran.
 *
 * @author: ${USER} Date: 01.07.13 Time: 01:39
 */
public class CommonNode extends DocumentWrapper {

    /* Class prefixes */
    public static final String NODE_CLASS_PREFIX = "node";

    /* Common */
    public static final String FIELD_COMMON_LIVE = "live";
    public static final String INC_FIELD_SENDER = "sender";
    public static final String FIELD_MARKED_FOR_DELETION = "markDel";
    public static final String FIELD_CLASSES = "c";
    public static final String FIELD_DOMAIN_IDS = "domIds";

    public CommonNode(ODocument document) {
        super(document);
    }

    public boolean addSecondaryClassId(ClassId classId) {
        Set classes = getDocument().field(FIELD_CLASSES, OType.EMBEDDEDSET);
        if (classes == null) {
            classes = new HashSet<Long>();
        }

        final boolean notAdded = (classes.contains(classId.getId()));
        classes.add(classId.getId());

        getDocument().field(FIELD_CLASSES, classes, OType.EMBEDDEDSET);
        return !notAdded;
    }

    public boolean removeSecondaryClassId(ClassId classId) {
        Set classes = getDocument().field(FIELD_CLASSES, OType.EMBEDDEDSET);
        if (classes == null) {
            return false;
        }
        boolean removed = classes.remove(classId.getId());
        if (classes.isEmpty()) {
            getDocument().removeField(FIELD_CLASSES);
        } else {
            getDocument().field(FIELD_CLASSES, classes, OType.EMBEDDEDSET);
        }
        return removed;
    }

    public Set<ClassId> getSecondaryClassIds() {
        final Set classes = getDocument().field(FIELD_CLASSES, OType.EMBEDDEDSET);
        if (classes == null) {
            return Collections.emptySet();
        } else {
            final Set<ClassId> classIds = new HashSet<>();
            for (final Object single : classes) {
                final Long id = (Long) single;
                classIds.add(new ClassId(id));
            }
            return classIds;
        }
    }

    public boolean hasSecondaryClasses() {
        return getDocument().containsField(FIELD_CLASSES);
    }

    public boolean isTypeOf(ClassId classId) {
        if (getPrimaryClassId().equals(classId)) {
            return true;
        }
        /* Secondary? */
        return (getSecondaryClassIds().contains(classId));
    }

    public boolean isLive() {
        final Boolean live = getDocument().field(FIELD_COMMON_LIVE, OType.BOOLEAN);
        return (live == null) || (live);
    }

    public void setSender(UserId userId) {
        getDocument().field(INC_FIELD_SENDER, userId.getId(), OType.LONG);
    }

    public UserId getSender() {
        final long senderId = getDocument().field(INC_FIELD_SENDER, OType.LONG);
        return new UserId(senderId);
    }

    public ClassId getPrimaryClassId() {
        return ClassNameUtils.getNodeClassId(getDocument().getClassName());
    }

    public Set<ClassId> getAllClassIds() {
        if (!hasSecondaryClasses()) {
            return Collections.singleton(getPrimaryClassId());
        } else {
            final Set<ClassId> classes = new HashSet<>();
            classes.add(getPrimaryClassId());
            classes.addAll(getSecondaryClassIds());
            return classes;
        }
    }

    @Deprecated
    public static String fieldName(FieldIndex index) {
        return "f" + index.getIndex();
    }

    @Deprecated
    public static String fieldName(FieldIndex index, String append) {
        return "f" + append + index.getIndex();
    }

    private static String fieldNameInt(FieldIndexAndClassId indexAndClassId,
            @Nullable String append) {
        final String appendStr;
        if (append != null) {
            appendStr = append;
        } else {
            appendStr = "";
        }
        if (indexAndClassId.isPrimaryClass()) {
            return "f" + appendStr + indexAndClassId.getIndex().getIndex();
        } else {
            final String classStr = Long.toHexString(indexAndClassId.getClassId().getId());
            return "f_" + classStr + appendStr + indexAndClassId.getIndex().getIndex();
        }
    }

    public static String fieldName(FieldIndexAndClassId indexAndClassId) {
        return fieldNameInt(indexAndClassId, null);
    }

    public static String fieldName(FieldIndexAndClassId indexAndClassId, String append) {
        return fieldNameInt(indexAndClassId, append);
    }

    @Deprecated
    public boolean hasField(FieldIndex index) {
        return getDocument().containsField(fieldName(index));
    }

    public Object getFieldValue(FieldIndexAndClassId index, OType type) {
        return getDocument().field(fieldName(index), type);
    }

    public void setFieldValue(FieldIndexAndClassId index, Object value, OType type) {
        getDocument().field(fieldName(index), value, type);
    }

    public void removeFieldValue(FieldIndexAndClassId index) {
        getDocument().removeField(fieldName(index));
    }

    public boolean hasField(FieldIndexAndClassId index) {
        return getDocument().containsField(fieldName(index));
    }

    public void setFieldValue(FieldIndexAndClassId index, String append, Object value, OType type) {
        getDocument().field(fieldName(index, append), value, type);
    }

    public void removeFieldValue(FieldIndexAndClassId index, String append) {
        getDocument().removeField(fieldName(index, append));
    }

    public Object getFieldValue(FieldIndexAndClassId index, String append, OType type) {
        return getDocument().field(fieldName(index, append), type);
    }

    public boolean hasField(FieldIndexAndClassId index, String append) {
        return getDocument().containsField(fieldName(index, append));
    }

    public void markForDeletion() {
        getDocument().field(FIELD_MARKED_FOR_DELETION, true, OType.BOOLEAN);
    }

    public boolean isMarkedForDeletion() {
        final Boolean marked = getDocument().field(FIELD_MARKED_FOR_DELETION, OType.BOOLEAN);
        return (marked != null) && marked;
    }

    public void deleteNow() {
        getDocument().clear();
        markForDeletion();
    }

    public OIncNid getIncNid() {
        return new OIncNid(getDocument().getIdentity());
    }

    public Set<DomainId> getDomainIds() {
        final Set domIdsLong = getDocument().field(FIELD_DOMAIN_IDS, OType.EMBEDDEDSET);
        final Set<DomainId> domainIds = new HashSet<>();
        if (domIdsLong != null) {
            for (final Object obj : domIdsLong) {
                domainIds.add(new DomainId(((Number) obj).longValue()));
            }
        }

        return domainIds;
    }
}
