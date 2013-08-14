package com.dcrux.buran.refimpl.modules.commit;

import com.dcrux.buran.common.IncNid;
import com.dcrux.buran.common.classes.ClassId;
import com.dcrux.buran.refimpl.modules.common.ONidVer;

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
        private final ONidVer versionsRecord;

        public IndexResult(ClassId classId, ONidVer versionsRecord) {
            this.classId = classId;
            this.versionsRecord = versionsRecord;
        }

        public ClassId getClassId() {
            return classId;
        }

        public ONidVer getVersionsRecord() {
            return versionsRecord;
        }
    }

    private final Map<IncNid, ONidVer> nidVersMap;

    public CommitResult(Map<IncNid, ONidVer> nidVersMap,
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

    public Map<IncNid, ONidVer> getNidVersMap() {
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
