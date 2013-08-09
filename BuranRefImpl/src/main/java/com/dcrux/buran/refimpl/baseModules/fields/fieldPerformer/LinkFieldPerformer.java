package com.dcrux.buran.refimpl.baseModules.fields.fieldPerformer;

import com.dcrux.buran.common.*;
import com.dcrux.buran.common.classDefinition.ClassDefinition;
import com.dcrux.buran.common.classDefinition.ClassFieldsDefinition;
import com.dcrux.buran.common.classes.ClassId;
import com.dcrux.buran.common.exceptions.NodeNotFoundException;
import com.dcrux.buran.common.fields.getter.FieldGetLinkClassId;
import com.dcrux.buran.common.fields.getter.FieldGetLinkTarget;
import com.dcrux.buran.common.fields.getter.IUnfieldedDataGetter;
import com.dcrux.buran.common.fields.setter.FieldRemove;
import com.dcrux.buran.common.fields.setter.FieldSetLink;
import com.dcrux.buran.common.fields.setter.IUnfieldedDataSetter;
import com.dcrux.buran.common.fields.types.LinkType;
import com.dcrux.buran.common.link.*;
import com.dcrux.buran.refimpl.baseModules.BaseModule;
import com.dcrux.buran.refimpl.baseModules.fields.FieldConstraintViolationInt;
import com.dcrux.buran.refimpl.baseModules.fields.FieldPerformer;
import com.dcrux.buran.refimpl.baseModules.fields.ICommitInfo;
import com.dcrux.buran.refimpl.baseModules.newRelations.NewRelationsWrapper;
import com.dcrux.buran.refimpl.baseModules.nodeWrapper.CommonNode;
import com.dcrux.buran.refimpl.baseModules.nodeWrapper.FieldIndexAndClassId;
import com.dcrux.buran.refimpl.baseModules.nodeWrapper.LiveNode;
import com.orientechnologies.orient.core.id.ORID;
import com.orientechnologies.orient.core.id.ORecordId;
import com.orientechnologies.orient.core.metadata.schema.OType;
import com.sun.istack.internal.Nullable;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Set;

/**
 * Buran.
 *
 * @author: ${USER} Date: 03.07.13 Time: 19:15
 */
public class LinkFieldPerformer extends FieldPerformer<LinkType> {

    public static enum TargetType {
        incUnversioned(0),
        incVersioned(1),
        nid(2),
        nidVer(3),
        extNid(4),
        extNidVer(5);
        int type;

        private TargetType(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }

        public static TargetType fromInt(int type) {
            switch (type) {
                case 0:
                    return incUnversioned;
                case 1:
                    return incVersioned;
                case 2:
                    return nid;
                case 3:
                    return nidVer;
                case 4:
                    return extNid;
                case 5:
                    return extNidVer;
                default:
                    throw new IllegalArgumentException("Unknown target int");
            }
        }
    }

    private final static Set<Class<? extends IUnfieldedDataGetter>> GETTERS =
            getters(FieldGetLinkTarget.class, FieldGetLinkClassId.class);

    private final static Set<Class<? extends IUnfieldedDataSetter>> SETTERS = setters(FieldSetLink
            .class, FieldRemove.class);

    public static final LinkFieldPerformer SINGLETON = new LinkFieldPerformer();

    public static final String APPEND_NID = "";
    public static final String APPEND_TYPE = "type";
    public static final String APPEND_ACCOUNT = "acc";
    public static final String APPEND_CLASSID = "cid";

    @Override
    public boolean performSetter(BaseModule baseModule, UserId sender, CommonNode node,
            ClassDefinition classDefinition, LinkType stringType, FieldIndexAndClassId fieldIndex,
            IUnfieldedDataSetter setter) throws FieldConstraintViolationInt {
        if (setter instanceof FieldSetLink) {
            final FieldSetLink fieldSetLink = ((FieldSetLink) setter);
            if (!stringType.isValid(fieldSetLink)) {
                throw new FieldConstraintViolationInt("Invalid field set link.");
            }

            /* Remove old relation, if any */
            final TargetType previousType = getType(node, fieldIndex);
            if (previousType != null) {
                switch (previousType) {
                    case nidVer:
                    case nid:
                        baseModule.getNewRelationsModule()
                                .removeSingleRelation(node.getOrid(), fieldIndex);
                }
            }

            final ORID target;
            final TargetType type;
            final Long classId;
            final Long accountId;
            final LinkTargetInc linkTargetInc = fieldSetLink.getLinkTargetInc();
            if (linkTargetInc.is(LinkTargetInc.TYPE_INC)) {
                IncLinkTarget incLinkTarget = linkTargetInc.get(LinkTargetInc.TYPE_INC);
                if (incLinkTarget.isVersioned()) {
                    type = TargetType.incVersioned;
                } else {
                    type = TargetType.incUnversioned;
                }
                classId = null;
                accountId = null;
                target = new ORecordId(incLinkTarget.getIncNid().getAsString());
            } else if (linkTargetInc.is(LinkTargetInc.TYPE_NID_VER)) {
                NidVerLinkTarget nidVerLinkTarget = linkTargetInc.get(LinkTargetInc.TYPE_NID_VER);
                if (nidVerLinkTarget.isVersioned()) {
                    type = TargetType.nidVer;
                    target = new ORecordId(nidVerLinkTarget.getNidVer().getAsString());
                } else {
                    type = TargetType.nid;
                    try {
                        target =
                                baseModule.getDataFetchModule().toOnid(nidVerLinkTarget.getNidVer())
                                        .getRecordId();
                    } catch (NodeNotFoundException e) {
                        throw new FieldConstraintViolationInt(MessageFormat
                                .format("Node {0} " + "(versioned) was not found in system.",
                                        nidVerLinkTarget.getNidVer()));
                    }
                }
                classId = null;
                accountId = null;
            } else if (linkTargetInc.is(LinkTargetInc.TYPE_NID)) {
                type = TargetType.nid;
                target = new ORecordId(linkTargetInc.get(LinkTargetInc.TYPE_NID).getAsString());
                classId = null;
                accountId = null;
            } else if (linkTargetInc.is(LinkTargetInc.TYPE_EXT)) {
                final ExtNidLinkTarget extNidLinkTarget = linkTargetInc.get(LinkTargetInc.TYPE_EXT);
                if (extNidLinkTarget.getExtNidOrNidVer().is(IExtNidOrNidVer.TYPE_EXT_NID)) {
                    final ExtNid extNid =
                            extNidLinkTarget.getExtNidOrNidVer().get(IExtNidOrNidVer.TYPE_EXT_NID);
                    type = TargetType.extNid;
                    target = new ORecordId(extNid.getNid().getAsString());
                    accountId = extNid.getAccount().getId();
                } else if (extNidLinkTarget.getExtNidOrNidVer()
                        .is(IExtNidOrNidVer.TYPE_EXT_NID_VER)) {
                    final ExtNidVer extNidVer = extNidLinkTarget.getExtNidOrNidVer()
                            .get(IExtNidOrNidVer.TYPE_EXT_NID_VER);
                    type = TargetType.extNidVer;
                    target = new ORecordId(extNidVer.getNidVer().getAsString());
                    accountId = extNidVer.getAccount().getId();

                } else {
                    throw new IllegalArgumentException("Unknown ext nid type");
                }
                classId = extNidLinkTarget.getTargetClassId().getId();
            } else {
                throw new IllegalArgumentException("Unknown target type");
            }
            node.setFieldValue(fieldIndex, APPEND_NID, target, OType.LINK);
            node.setFieldValue(fieldIndex, APPEND_TYPE, type.getType(), OType.INTEGER);
            if (accountId != null) {
                node.setFieldValue(fieldIndex, APPEND_ACCOUNT, accountId, OType.LONG);
            }
            if (classId != null) {
                node.setFieldValue(fieldIndex, APPEND_CLASSID, classId, OType.LONG);
            }

            /* Add new relation */
            switch (type) {
                case nidVer:
                case nid:

                    boolean versioned = type.equals(TargetType.nidVer);
                    final CommonNode targetNode;
                    if (versioned) {
                        try {
                            targetNode = baseModule.getDataFetchModule().getNode(target);
                        } catch (NodeNotFoundException e) {
                            throw new FieldConstraintViolationInt(MessageFormat.format("Given " +
                                    "target node {0} (versioned) was " +
                                    "not found", target));
                        }
                    } else {
                        try {
                            targetNode = baseModule.getDataFetchModule()
                                    .getNode(new Nid(target.getIdentity().toString()));
                        } catch (NodeNotFoundException e) {
                            throw new FieldConstraintViolationInt(MessageFormat.format("Given " +
                                    "target node {0} (not versioned) was " +
                                    "not found", target));
                        }
                    }
                    ClassId targetClassId = targetNode.getPrimaryClassId();
                    NewRelationsWrapper newRelationsWrapper = NewRelationsWrapper
                            .c(node.getOrid(), fieldIndex, targetClassId, target, versioned);
                    baseModule.getNewRelationsModule().addRelation(newRelationsWrapper);
            }


            return true;
        }

        if (setter instanceof FieldRemove) {
            final TargetType type = getType(node, fieldIndex);
            if (type != null) {
                switch (type) {
                    case nidVer:
                    case nid:
                        baseModule.getNewRelationsModule()
                                .removeSingleRelation(node.getOrid(), fieldIndex);
                }
            }
            node.removeFieldValue(fieldIndex, APPEND_NID);
            node.removeFieldValue(fieldIndex, APPEND_TYPE);
            node.removeFieldValue(fieldIndex, APPEND_ACCOUNT);
            node.removeFieldValue(fieldIndex, APPEND_CLASSID);
            return true;
        }
        throw new IllegalStateException("Unknown setter");
    }

    @Override
    public void validateAndCommit(BaseModule baseModule, UserId sender, CommonNode node,
            ClassDefinition classDefinition, LinkType stringType,
            ClassFieldsDefinition.FieldEntry fieldEntry, FieldIndexAndClassId fieldIndex,
            ICommitInfo commitInfo) throws FieldConstraintViolationInt {
        if (fieldEntry.isRequired()) {
            Integer value = (Integer) node.getFieldValue(fieldIndex, APPEND_TYPE, OType.INTEGER);
            if (value == null) {
                throw new FieldConstraintViolationInt("Field is missing.");
            }
        }

        /* Commit and add relation */
        final TargetType oldType = getType(node, fieldIndex);
        final TargetType newType;
        final ORID target;
        if (oldType != null) {
            /* Commit */
            switch (oldType) {
                case incVersioned:
                    final ORID incOrid = getOrid(node, fieldIndex);
                    final ORID newOrid = commitInfo.getNidVerByIncNid(incOrid);
                    if (newOrid == null) {
                        throw new FieldConstraintViolationInt(MessageFormat.format("Node with " +
                                "incubation ID {0} was not found in commit; this node is " +
                                "referenced. Every referenced " +
                                "node must be in commit.", incOrid));
                    }
                    newType = TargetType.nidVer;
                    node.setFieldValue(fieldIndex, APPEND_NID, newOrid, OType.LINK);
                    node.setFieldValue(fieldIndex, APPEND_TYPE, newType.getType(), OType.INTEGER);
                    target = newOrid;
                    break;
                case incUnversioned:
                    final ORID incOrid2 = getOrid(node, fieldIndex);
                    final ORID newOrid2 = commitInfo.getNidByIncNid(incOrid2);
                    if (newOrid2 == null) {
                        throw new FieldConstraintViolationInt(MessageFormat.format("Node with " +
                                "incubation ID {0} was not found in commit; this node is " +
                                "referenced. Every referenced " +
                                "node must be in commit.", incOrid2));
                    }
                    newType = TargetType.nid;
                    node.setFieldValue(fieldIndex, APPEND_NID, newOrid2, OType.LINK);
                    node.setFieldValue(fieldIndex, APPEND_TYPE, newType.getType(), OType.INTEGER);
                    target = newOrid2;
                    break;
                default:
                    newType = null;
                    target = null;
            }

            /* Add relation */
            if (newType != null) {
                switch (newType) {
                    case nidVer:
                    case nid:
                        boolean versioned = newType.equals(TargetType.nidVer);
                        final CommonNode targetNode;
                        if (versioned) {
                            try {
                                targetNode = baseModule.getDataFetchModule().getNode(target);
                            } catch (NodeNotFoundException e) {
                                throw new FieldConstraintViolationInt(
                                        MessageFormat.format("Given " +
                                                "target node {0} (versioned) was " +
                                                "not found", target));
                            }
                        } else {
                            try {
                                targetNode = baseModule.getDataFetchModule()
                                        .getNode(new Nid(target.getIdentity().toString()));
                            } catch (NodeNotFoundException e) {
                                throw new FieldConstraintViolationInt(
                                        MessageFormat.format("Given " +
                                                "target node {0} (not versioned) was " +
                                                "not found", target));
                            }
                        }
                        ClassId targetClassId = targetNode.getPrimaryClassId();
                        NewRelationsWrapper newRelationsWrapper = NewRelationsWrapper
                                .c(node.getOrid(), fieldIndex, targetClassId, target, versioned);
                        baseModule.getNewRelationsModule().addRelation(newRelationsWrapper);
                }
            }
        }
    }

    @Nullable
    private TargetType getType(CommonNode node, FieldIndexAndClassId fieldIndex) {
        Integer type = (Integer) node.getFieldValue(fieldIndex, APPEND_TYPE, OType.INTEGER);
        if (type == null) {
            return null;
        } else {
            return TargetType.fromInt(type);
        }
    }

    @Nullable
    private UserId getAccount(CommonNode node, FieldIndexAndClassId fieldIndex) {
        Long account = (Long) node.getFieldValue(fieldIndex, APPEND_ACCOUNT, OType.LONG);
        if (account == null) {
            return null;
        } else {
            return new UserId(account);
        }
    }

    @Nullable
    private ClassId getClassId(CommonNode node, FieldIndexAndClassId fieldIndex) {
        Long classId = (Long) node.getFieldValue(fieldIndex, APPEND_CLASSID, OType.LONG);
        if (classId == null) {
            return null;
        } else {
            return new ClassId(classId);
        }
    }

    @Nullable
    private ORID getOrid(CommonNode node, FieldIndexAndClassId fieldIndex) {
        ORID orid = (ORID) node.getFieldValue(fieldIndex, APPEND_NID, OType.LINK);
        if (orid == null) {
            return null;
        } else {
            return orid;
        }
    }

    @Override
    public Serializable performGetter(BaseModule baseModule, LiveNode node,
            ClassDefinition classDefinition, LinkType stringType, FieldIndexAndClassId fieldIndex,
            IUnfieldedDataGetter<?> dataGetter) {
        if (dataGetter instanceof FieldGetLinkTarget) {
            TargetType type = getType(node, fieldIndex);
            if (type == null) {
                return null;
            }
            switch (type) {
                case extNidVer:
                    return LinkTarget.extNidVer(new ExtNidVer(getAccount(node, fieldIndex),
                            new NidVer(getOrid(node, fieldIndex).toString())));
                case extNid:
                    return LinkTarget.extNid(new ExtNid(getAccount(node, fieldIndex),
                            new Nid(getOrid(node, fieldIndex).toString())));
                case nid:
                    return LinkTarget.nid(new Nid(getOrid(node, fieldIndex).toString()));
                case nidVer:
                    return LinkTarget.nidVer(new NidVer(getOrid(node, fieldIndex).toString()));
                default:
                    throw new IllegalArgumentException("Unknown target, has node been commited?");
            }
        } else if (dataGetter instanceof FieldGetLinkClassId) {
            ClassId classId = getClassId(node, fieldIndex);
            return classId;
        }
        throw new IllegalArgumentException("Unknown getter");
    }

    @Override
    public Set<Class<? extends IUnfieldedDataGetter>> supportedGetters() {
        return GETTERS;
    }

    @Override
    public Set<Class<? extends IUnfieldedDataSetter>> supportedSetters() {
        return SETTERS;
    }

    @Override
    public Class<LinkType> supports() {
        return LinkType.class;
    }
}
