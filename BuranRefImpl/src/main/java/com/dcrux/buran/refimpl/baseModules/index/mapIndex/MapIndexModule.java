package com.dcrux.buran.refimpl.baseModules.index.mapIndex;

import com.dcrux.buran.common.classDefinition.ClassIndexDefinition;
import com.dcrux.buran.common.classDefinition.ClassIndexName;
import com.dcrux.buran.common.classes.ClassId;
import com.dcrux.buran.indexing.IndexDefinition;
import com.dcrux.buran.indexing.mapStore.IMapIndex;
import com.dcrux.buran.indexing.mapStore.MapIndex;
import com.dcrux.buran.indexing.mapStore.NullMapIndex;
import com.dcrux.buran.refimpl.baseModules.BaseModule;
import com.dcrux.buran.refimpl.baseModules.classes.ClassDefExt;
import com.dcrux.buran.refimpl.baseModules.common.Module;
import com.dcrux.buran.refimpl.baseModules.index.eval.EvaluatedMap;
import com.dcrux.buran.refimpl.baseModules.index.keyGen.MapKey;
import com.dcrux.buran.refimpl.baseModules.index.orientSerializer.BuranKeySerializer;
import com.dcrux.buran.refimpl.baseModules.index.orientSerializer.ComparableBinary;
import com.orientechnologies.orient.core.id.ORID;
import com.orientechnologies.orient.core.index.OIndex;
import com.orientechnologies.orient.core.index.ORuntimeKeyIndexDefinition;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.sun.istack.internal.Nullable;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * Buran.
 *
 * @author: ${USER} Date: 09.07.13 Time: 11:47
 */
public class MapIndexModule extends Module<BaseModule> {
    public MapIndexModule(BaseModule baseModule) {
        super(baseModule);
    }

    private String getOrientIndexName(ClassId classId, ClassIndexName classIndexName) {
        return MessageFormat.format("idx-{0}-{1}", ((Long) classId.getId()).toString(),
                classIndexName.getName());
    }

    public OIndex getIndex(ClassId classId, ClassIndexName classIndexName) {
        final String iName = getOrientIndexName(classId, classIndexName);
        final OIndex oIndex = getBase().getDb().getMetadata().getIndexManager().getIndex(iName);
        return oIndex;
    }

    private void createIndex(ClassId classId, ClassIndexName classIndexName, IMapIndex mapIndex) {
        if (mapIndex instanceof MapIndex) {
            final MapIndex mapIndexCast = (MapIndex) mapIndex;
            final String iName = getOrientIndexName(classId, classIndexName);
            if (!getBase().getDb().getMetadata().getIndexManager().existsIndex(iName)) {
                getBase().getDb().getMetadata().getIndexManager()
                        .createIndex(iName, OClass.INDEX_TYPE.NOTUNIQUE.name(),
                                new ORuntimeKeyIndexDefinition(BuranKeySerializer.ID), null, null);
            }
        } else if (mapIndex instanceof NullMapIndex) {

        } else {
            throw new IllegalArgumentException(
                    "Unknown map index, given Type: " + mapIndex.getClass());
        }
    }

    public void createIndexes(ClassId classId, ClassIndexDefinition classIndexDefinition) {
        for (final Map.Entry<ClassIndexName, IndexDefinition> entry : classIndexDefinition
                .getIndexDefinitionMap().entrySet()) {
            createIndex(classId, entry.getKey(), entry.getValue().getMapIndexType());
        }
    }

    public void removeFromMapStorage(ClassId classId, ClassDefExt cde, ORID versionsRecord) {
        final ClassIndexDefinition indexes = cde.getClassDefinition().getIndexes();
        for (final Map.Entry<ClassIndexName, IndexDefinition> index : indexes
                .getIndexDefinitionMap().entrySet()) {
            final IndexDefinition indexDefinition = index.getValue();
            final IMapIndex mapIndex = indexDefinition.getMapIndexType();
            if (mapIndex instanceof MapIndex) {
                final OIndex oIndex = getIndex(classId, index.getKey());
                oIndex.remove(versionsRecord);
            } else if (mapIndex instanceof NullMapIndex) {
            } else {
                throw new IllegalArgumentException(
                        "Unknown map index, given Type: " + mapIndex.getClass());
            }
        }
    }

    public void addToMapStorage(ClassId classId, ClassDefExt cde, ORID versionsRecord,
            Map<ClassIndexName, Collection<EvaluatedMap>> dataByClassIndex) {
        for (final Map.Entry<ClassIndexName, Collection<EvaluatedMap>> entry : dataByClassIndex
                .entrySet()) {
            final OIndex<?> index = getIndex(classId, entry.getKey());
            for (final EvaluatedMap evalMap : entry.getValue()) {
                index.put(new ComparableBinary(evalMap.getKey()), versionsRecord);
            }
        }
    }

    @Nullable
    public Collection<ORID> get(ClassId classId, ClassIndexName name, MapKey mapKey,
            long maxValuesToFetch) {
        final OIndex<?> index = getIndex(classId, name);

        final Object result;
        final long limit = maxValuesToFetch + 1;

        if ((mapKey.getBegin() != null) && (mapKey.getEnd() != null)) {
            if (mapKey.getBegin() == mapKey.getEnd()) {
                result = index.get(new ComparableBinary(mapKey.getBegin()));
            } else {
                result = index.getValuesBetween(new ComparableBinary(mapKey.getBegin()),
                        mapKey.isBeginIncluded(), new ComparableBinary(mapKey.getEnd()),
                        mapKey.isEndIncluded(), (int) limit);
            }
        } else {
            if (mapKey.getBegin() != null) {
                result = index.getValuesMajor(new ComparableBinary(mapKey.getBegin()),
                        mapKey.isBeginIncluded(), (int) limit);
            } else if (mapKey.getEnd() != null) {
                result = index.getValuesMinor(new ComparableBinary(mapKey.getEnd()),
                        mapKey.isEndIncluded(), (int) limit);
            } else {
                throw new IllegalArgumentException("End and Begin are both null");
            }
        }

        final Collection resCollection;
        if (result instanceof Collection) {
            resCollection = (Collection) result;
        } else {
            resCollection = Collections.singletonList(result);
        }

        if (resCollection.size() > maxValuesToFetch) {
            return null;
        }

        return resCollection;
    }
}
