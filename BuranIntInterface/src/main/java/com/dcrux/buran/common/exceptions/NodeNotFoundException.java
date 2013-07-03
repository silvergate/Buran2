package com.dcrux.buran.common.exceptions;

import com.dcrux.buran.common.ExpectableException;
import com.dcrux.buran.common.Version;
import com.google.common.base.Optional;

/**
 * Buran.
 *
 * @author: ${USER} Date: 30.06.13 Time: 23:06
 */
public class NodeNotFoundException extends ExpectableException {

    public static enum Reason {
        wrongVersion,
        deleted,
        doesNotExist
    }

    private final Reason reason;
    private final Optional<Version> currentVersion;

    public static NodeNotFoundException wrongVersion(Version currentVersion) {
        return new NodeNotFoundException(Reason.wrongVersion, Optional.of(currentVersion));
    }

    public static NodeNotFoundException deleted() {
        return new NodeNotFoundException(Reason.deleted, Optional.<Version>absent());
    }


    public static NodeNotFoundException doesNotExist() {
        return new NodeNotFoundException(Reason.doesNotExist, Optional.<Version>absent());
    }

    private NodeNotFoundException(Reason reason, Optional<Version> currentVersion) {
        super();
        this.reason = reason;
        this.currentVersion = currentVersion;
    }

    public Reason getReason() {
        return reason;
    }

    public Optional<Version> getCurrentVersion() {
        return currentVersion;
    }
}
