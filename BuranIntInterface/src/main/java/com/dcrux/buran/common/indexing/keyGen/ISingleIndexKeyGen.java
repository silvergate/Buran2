package com.dcrux.buran.common.indexing.keyGen;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Buran.
 *
 * @author: ${USER} Date: 09.07.13 Time: 18:12
 */
public interface ISingleIndexKeyGen extends Serializable, IKeyGen {

    public static class Builder implements Serializable {
        final List<ISingleIndexKeyGen> elements = new ArrayList<ISingleIndexKeyGen>();
    }

    int getLength();

    void generateKey(ByteBuffer byteBuffer);
}
