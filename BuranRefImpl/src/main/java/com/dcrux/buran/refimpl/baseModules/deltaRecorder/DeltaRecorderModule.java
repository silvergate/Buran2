package com.dcrux.buran.refimpl.baseModules.deltaRecorder;

import com.dcrux.buran.common.getterSetter.IDataSetter;
import com.dcrux.buran.refimpl.baseModules.BaseModule;
import com.dcrux.buran.refimpl.baseModules.common.Module;
import com.dcrux.buran.refimpl.baseModules.common.ONid;
import com.dcrux.buran.refimpl.baseModules.nodeWrapper.IncubationNode;
import com.orientechnologies.orient.core.db.record.OIdentifiable;
import com.orientechnologies.orient.core.id.ORID;
import com.orientechnologies.orient.core.index.OIndex;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.sun.istack.internal.Nullable;
import org.apache.commons.lang3.SerializationUtils;

import java.util.Collection;
import java.util.List;

/**
 * Buran.
 *
 * @author: ${USER} Date: 03.07.13 Time: 13:45
 */
public class DeltaRecorderModule extends Module<BaseModule> {
    public DeltaRecorderModule(BaseModule baseModule) {
        super(baseModule);
    }

    @Nullable
    private DeltaWrapper getDeltaWrapper(ONid oNid) {
        final OIndex index = getBase().getDbUtils()
                .getIndex(DeltaWrapper.CLASS_NAME, DeltaWrapper.INDEX_INC_NODE_ID);

        final Object value = index.getDefinition().createValue(oNid.getAsString());
        final Collection<OIdentifiable> found = index.getValuesBetween(value, value);
        if ((found == null) || (found.isEmpty())) {
            return null;
        }
        if (found.size() > 1) {
            throw new IllegalStateException("This should never happen");
        }
        final ODocument doc = getBase().getDb().load((ORID) found.iterator().next());
        return new DeltaWrapper(doc);
    }

    private DeltaWrapper getOrCreateDeltaWrapper(ONid oNid) {
        //TODO: This is not thread-safe!
        final DeltaWrapper deltaWrapper = getDeltaWrapper(oNid);
        if (deltaWrapper != null) {
            return deltaWrapper;
        }
        return DeltaWrapper.create(oNid);
    }

    public void record(IncubationNode node, IDataSetter setter) {
        final DeltaWrapper deltaWrapper = getOrCreateDeltaWrapper(node.getNid());
        final byte[] serSetter = SerializationUtils.serialize(setter);
        final DeltaEntryWrapper dew = DeltaEntryWrapper.create(serSetter);
        dew.getDocument().save();
        deltaWrapper.addEntry(dew);
        deltaWrapper.getDocument().save();
    }

    public void playRecords(ONid oNid, IRecordPlayer player) throws Exception {
        final DeltaWrapper wrapper = getDeltaWrapper(oNid);
        if (wrapper == null) {
            return;
        }
        final List<ODocument> entries = wrapper.getEntries();
        if ((entries != null) && (!entries.isEmpty())) {
            for (final ODocument entry : entries) {
                final DeltaEntryWrapper entryWrapper = new DeltaEntryWrapper(entry);
                final byte[] data = entryWrapper.getSerData();
                IDataSetter setter = (IDataSetter) SerializationUtils.deserialize(data);
                player.entry(oNid, setter);
            }
        }
    }

    public boolean deleteDeltaRecord(ONid oNid) {
        DeltaWrapper wrapper = getDeltaWrapper(oNid);
        if (wrapper == null) {
            return false;
        }
        final List<ODocument> entries = wrapper.getEntries();
        if (entries != null) {
            for (final ODocument entry : entries) {
                getBase().getDb().delete(entry);
            }
        }
        wrapper.getDocument().delete();
        return true;
    }

    public void setupDb() {
        DeltaWrapper.setupDb(getBase());
        DeltaEntryWrapper.setupDb(getBase());
    }
}
