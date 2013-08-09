package com.dcrux.buran.refimpl.baseModules.newRelations;

import com.dcrux.buran.common.Nid;
import com.dcrux.buran.common.NidVer;
import com.dcrux.buran.common.classes.ClassId;
import com.dcrux.buran.common.exceptions.NodeNotFoundException;
import com.dcrux.buran.common.fields.FieldIndex;
import com.dcrux.buran.common.inRelations.InRealtionGetter;
import com.dcrux.buran.common.inRelations.InRelationResult;
import com.dcrux.buran.common.inRelations.selector.InRelSelCount;
import com.dcrux.buran.common.inRelations.selector.InRelSelTarget;
import com.dcrux.buran.common.inRelations.where.InRelWhereClassId;
import com.dcrux.buran.common.inRelations.where.InRelWhereVersioned;
import com.dcrux.buran.refimpl.baseModules.BaseModule;
import com.dcrux.buran.refimpl.baseModules.common.Module;
import com.dcrux.buran.refimpl.baseModules.common.ONidVer;
import com.dcrux.buran.refimpl.baseModules.nodeWrapper.FieldIndexAndClassId;
import com.orientechnologies.orient.core.db.record.OIdentifiable;
import com.orientechnologies.orient.core.id.ORID;
import com.orientechnologies.orient.core.index.OIndex;
import com.orientechnologies.orient.core.record.impl.ODocument;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Buran.
 *
 * @author: ${USER} Date: 04.08.13 Time: 20:58
 */
public class NewRelationsModule extends Module<BaseModule> {
    public NewRelationsModule(BaseModule baseModule) {
        super(baseModule);
    }

    public void setupDb() {
        NewRelationsWrapper.setupDb(getBase());
    }

    private Collection<OIdentifiable> getIndexByNidAndFieldIndex(ORID nid, ClassId classId,
            FieldIndex start, FieldIndex end) {
        final OIndex index = getBase().getDbUtils().getIndex(NewRelationsWrapper.CLASS_NAME,
                NewRelationsWrapper.IDX_SOURCE_NID_AND_CLASSID_AND_FIELD_INDEX);
        final Object valueStart =
                index.getDefinition().createValue(nid, classId.getId(), (int) start.getIndex());
        final Object valueEnd =
                index.getDefinition().createValue(nid, classId.getId(), (int) end.getIndex());
        final Collection<OIdentifiable> foundSet = index.getValuesBetween(valueStart, valueEnd);
        return foundSet;
    }

    private Collection<OIdentifiable> getIndexByNid(ORID nid) {
        final OIndex index = getBase().getDbUtils().getIndex(NewRelationsWrapper.CLASS_NAME,
                NewRelationsWrapper.IDX_SOURCE_NID_AND_CLASSID_AND_FIELD_INDEX);
        final Object valueStart = index.getDefinition().createValue(nid);
        final Object valueEnd = index.getDefinition().createValue(nid);
        final Collection<OIdentifiable> foundSet = index.getValuesBetween(valueStart, valueEnd);
        return foundSet;
    }

    public void removeSingleRelation(ORID nid, FieldIndexAndClassId fieldIndex) {
        System.out.println("Remove single relation");
        for (final OIdentifiable oIdentifiable : getIndexByNidAndFieldIndex(nid,
                fieldIndex.getClassId(), fieldIndex.getIndex(), fieldIndex.getIndex())) {
            getBase().getDb().delete((ORID) oIdentifiable);
        }
    }

    public void removeAllRelations(ORID nid) {
        System.out.println("Remove all relations");
        for (final OIdentifiable oIdentifiable : getIndexByNid(nid)) {
            getBase().getDb().delete((ORID) oIdentifiable);
        }
    }

    public void addRelation(NewRelationsWrapper relationWrapper) {
        System.out.println("Add single relation: " + relationWrapper);
        relationWrapper.getDocument().save();
    }


    public <TResult extends Serializable> TResult processGetter(ORID targetNid,
            InRealtionGetter<TResult> getter) throws NodeNotFoundException {

        /* Get target */
        ORID target;
        switch (getter.getVersioning()) {
            case unversioned:
                target = targetNid;
                break;
            case versioned:
                target = getBase().getDataFetchModule().toNidVer(new Nid(targetNid.toString()))
                        .getoIdentifiable();
                break;
            default:
                throw new IllegalArgumentException("Unknown versioning");
        }

        /* Get start end end values */

        final Object valueStart;
        final Object valueEnd;
        final OIndex index;
        if (getter.getWhere() instanceof InRelWhereVersioned) {
            index = getBase().getDbUtils().getIndex(NewRelationsWrapper.CLASS_NAME,
                    NewRelationsWrapper.IDX_TARGET_AND_SRC_CLASSID_AND_FIELD_INDEX);
            valueStart =
                    index.getDefinition().createValue(NewRelationsWrapper.FIELD_BUG_FIX, target);
            valueEnd = index.getDefinition().createValue(NewRelationsWrapper.FIELD_BUG_FIX, target);
        } else if (getter.getWhere() instanceof InRelWhereClassId) {
            final InRelWhereClassId inRelWhereClassId = (InRelWhereClassId) getter.getWhere();
            index = getBase().getDbUtils().getIndex(NewRelationsWrapper.CLASS_NAME,
                    NewRelationsWrapper.IDX_TARGET_AND_SRC_CLASSID_AND_FIELD_INDEX);
            final int start;
            if (inRelWhereClassId.getStartIndex().isPresent()) {
                start = inRelWhereClassId.getStartIndex().get().getIndex();
            } else {
                start = Integer.MIN_VALUE;
            }
            final int end;
            if (inRelWhereClassId.getEndIndex().isPresent()) {
                end = inRelWhereClassId.getEndIndex().get().getIndex();
            } else {
                end = Integer.MAX_VALUE;
            }
            valueStart = index.getDefinition()
                    .createValue(NewRelationsWrapper.FIELD_BUG_FIX, target,
                            inRelWhereClassId.getSourceClassId().getId(), start);
            valueEnd = index.getDefinition().createValue(NewRelationsWrapper.FIELD_BUG_FIX, target,
                    inRelWhereClassId.getSourceClassId().getId(), end);
        } else {
            throw new IllegalArgumentException("Unknown where clause");
        }

        /* Fetch (and agregate) results */

        long indexSize = index.getSize();
        if (indexSize == 0) {
            System.out.println("WARNING: Index size: " + indexSize + ", target: " + target);
            System.out.println("WARNING: Automatic: " + index.isAutomatic());
            System.out.println("WARNING: Rebuilding: " + index.isRebuiding());
        }

        final int skip = getter.getSkipLimit().getSkipCount();
        final int limit = getter.getSkipLimit().getLimit();

        final Collection values =
                index.getValuesBetween(valueStart, true, valueEnd, true, limit + 1);
        boolean limited;
        if (values.size() > limit) {
            limited = true;
            /* Activate limit */
            if (!getter.getSkipLimit().isReturnPartialResults()) {
                /* No results to return */
                return (TResult) new InRelationResult<Serializable>(true, null);
            }
        } else {
            limited = false;
        }

        /* Aggregator or fetch */
        final Serializable result;
        if (getter.getSelector() instanceof InRelSelCount) {
            int size = values.size();
            if (limited) {
                size--;
            }
            if (skip > 0) {
                size = size - skip;
            }
            if (size < 0) {
                result = null;
            } else {
                result = size;
            }
        } else if (getter.getSelector() instanceof InRelSelTarget) {
            ArrayList<NidVer> resultNids = new ArrayList<>();
            int numSkipped = 0;
            for (final Object oIdentObj : values) {
                if (skip > numSkipped) {
                    numSkipped++;
                } else {
                    final OIdentifiable oIdentifiable = (OIdentifiable) oIdentObj;
                    final ODocument newRelation =
                            getBase().getDb().load(oIdentifiable.getIdentity());
                    final NewRelationsWrapper newRelationsWrapper =
                            new NewRelationsWrapper(newRelation);
                    final Nid source = newRelationsWrapper.getSource();
                    ONidVer oNidVer = getBase().getDataFetchModule().toNidVer(source);
                    resultNids.add(new NidVer(oNidVer.getoIdentifiable().toString()));
                }
            }
            if ((limited) && (!resultNids.isEmpty())) {
                resultNids = (ArrayList<NidVer>) resultNids.subList(0, resultNids.size() - 1);
            }
            result = resultNids;
        } else {
            throw new IllegalArgumentException("Unknown selector type");
        }

        return (TResult) new InRelationResult<Serializable>(limited, result);
    }
}
