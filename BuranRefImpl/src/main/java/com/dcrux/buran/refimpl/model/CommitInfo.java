package com.dcrux.buran.refimpl.model;

import com.orientechnologies.orient.core.id.ORID;
import com.sun.istack.internal.Nullable;

import java.util.*;

/**
 * Buran.
 *
 * @author: ${USER} Date: 01.07.13 Time: 09:46
 */
public class CommitInfo {
    public static class CommitEntry {
        private final IncubationNode incubationNode;
        private final LiveNode liveNode;

        public CommitEntry(IncubationNode incubationNode, LiveNode liveNode) {
            this.incubationNode = incubationNode;
            this.liveNode = liveNode;
        }

        public IncubationNode getIncubationNode() {
            return incubationNode;
        }

        public LiveNode getLiveNode() {
            return liveNode;
        }

        public boolean isUpdate() {
            return !getIncubationNode().getNid().getRecordId()
                    .equals(getLiveNode().getNid().getRecordId());
        }
    }

    private Set<CommitEntry> commitEntrySet = new HashSet<CommitEntry>();
    private Set<CommitEntry> commitEntrySetRo = Collections.unmodifiableSet(this.commitEntrySet);
    private Map<ORID, CommitEntry> incToLive = new HashMap<>();

    public void add(CommitEntry entry) {
        this.commitEntrySet.add(entry);
        this.incToLive.put(entry.getIncubationNode().getNid().getRecordId(), entry);
    }

    public Set<CommitEntry> getCommitEntrySet() {
        return commitEntrySetRo;
    }

    @Nullable
    public CommitEntry getByIncNid(ORID incNid) {
        return this.incToLive.get(incNid);
    }
}
