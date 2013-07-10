package com.dcrux.buran.refimpl.baseModules.commit;

import com.dcrux.buran.common.IIncNid;
import com.dcrux.buran.common.NidVer;
import com.dcrux.buran.common.classes.ClassId;
import com.orientechnologies.orient.core.id.ORID;

import java.util.Collection;
import java.util.Map;

/**
 * Buran.
 *
 * @author: ${USER} Date: 08.07.13 Time: 22:59
 */
public class CommitResult {

    public static class IndexResult {
        private final ClassId classId;
        private final ORID versionsRecord;

        public IndexResult(ClassId classId, ORID versionsRecord) {
            this.classId = classId;
            this.versionsRecord = versionsRecord;
        }

        public ClassId getClassId() {
            return classId;
        }

        public ORID getVersionsRecord() {
            return versionsRecord;
        }
    }

    private final Map<IIncNid, NidVer> nidVersMap;

    public CommitResult(Map<IIncNid, NidVer> nidVersMap,
            Collection<IndexResult> removeFromIndexesCauseRemoved,
            Collection<IndexResult> removeFromIndexesCauseUpdated,
            Collection<IndexResult> addToIndexes) {
        this.nidVersMap = nidVersMap;
        this.removeFromIndexesCauseRemoved = removeFromIndexesCauseRemoved;
        this.removeFromIndexesCauseUpdated = removeFromIndexesCauseUpdated;
        this.addToIndexes = addToIndexes;
    }

    private final Collection<IndexResult> removeFromIndexesCauseRemoved;
    private final Collection<IndexResult> removeFromIndexesCauseUpdated;
    private final Collection<IndexResult> addToIndexes;

    public Map<IIncNid, NidVer> getNidVersMap() {
        return nidVersMap;
    }

    public Collection<IndexResult> getRemoveFromIndexesCauseRemoved() {
        return removeFromIndexesCauseRemoved;
    }

    public Collection<IndexResult> getRemoveFromIndexesCauseUpdated() {
        return removeFromIndexesCauseUpdated;
    }

    public Collection<IndexResult> getAddToIndexes() {
        return addToIndexes;
    }
}
