package com.dcrux.buran.refimpl.baseModules.index.keyGen;

import com.dcrux.buran.common.indexing.keyGen.IKeyGen;
import com.dcrux.buran.common.indexing.keyGen.ISingleIndexKeyGen;
import com.dcrux.buran.common.indexing.keyGen.RangeIndexKeyGen;
import com.dcrux.buran.refimpl.baseModules.common.Module;

import java.nio.ByteBuffer;

/**
 * Buran.
 *
 * @author: ${USER} Date: 09.07.13 Time: 22:58
 */
public class KeyGenModule extends Module<Void> {
    public KeyGenModule() {
        super(null);
    }

    private byte[] getKeyAsBinary(ISingleIndexKeyGen singleIndexKeyGen) {
        final int length = singleIndexKeyGen.getLength();
        final ByteBuffer byteBuffer = ByteBuffer.allocate(length);
        singleIndexKeyGen.generateKey(byteBuffer);
        return byteBuffer.array();
    }

    public MapKey generate(IKeyGen keyGen) {
        if (keyGen instanceof ISingleIndexKeyGen) {
            final ISingleIndexKeyGen singleIndexKeyGen = (ISingleIndexKeyGen) keyGen;
            final byte[] binary = getKeyAsBinary(singleIndexKeyGen);
            return new MapKey(true, binary, true, binary);
        } else if (keyGen instanceof RangeIndexKeyGen) {
            final RangeIndexKeyGen rangeIndexKeyGen = (RangeIndexKeyGen) keyGen;
            if ((rangeIndexKeyGen.getFrom() != null) && (rangeIndexKeyGen.getTo() != null)) {
                return new MapKey(rangeIndexKeyGen.isFromIncluded(),
                        getKeyAsBinary(rangeIndexKeyGen.getFrom()), rangeIndexKeyGen.isToIncluded(),
                        getKeyAsBinary(rangeIndexKeyGen.getTo()));
            } else if (rangeIndexKeyGen.getFrom() != null) {
                return new MapKey(rangeIndexKeyGen.isFromIncluded(),
                        getKeyAsBinary(rangeIndexKeyGen.getFrom()), false, null);
            } else if (rangeIndexKeyGen.getTo() != null) {
                return new MapKey(false, null, rangeIndexKeyGen.isToIncluded(),
                        getKeyAsBinary(rangeIndexKeyGen.getTo()));
            } else {
                throw new IllegalArgumentException("No begin and no end.");
            }
        } else {
            throw new IllegalArgumentException(
                    "Only Single Index Key Gen supported (TODO: " + "implement others).");
        }
    }

}
