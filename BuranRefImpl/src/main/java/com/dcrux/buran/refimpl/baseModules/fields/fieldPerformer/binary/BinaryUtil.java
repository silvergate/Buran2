package com.dcrux.buran.refimpl.baseModules.fields.fieldPerformer.binary;

import com.dcrux.buran.common.fields.FieldIndex;
import com.dcrux.buran.refimpl.baseModules.BaseModule;
import com.dcrux.buran.refimpl.baseModules.nodeWrapper.CommonNode;
import com.orientechnologies.orient.core.db.record.OIdentifiable;
import com.orientechnologies.orient.core.metadata.schema.OType;
import com.orientechnologies.orient.core.record.impl.ORecordBytes;
import com.sun.istack.internal.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Buran.
 *
 * @author: ${USER} Date: 12.07.13 Time: 12:15
 */
public class BinaryUtil {

    private static final int CHUNK_SIZE = 8192 * 2;
    private static final String APPEND_SIZE = "as";

    @Nullable
    public static List<OIdentifiable> getChunks(CommonNode node, FieldIndex fieldIndex,
            boolean createIfNonExistant) {
        final Object chunks = node.getFieldValue(fieldIndex, OType.LINKLIST);
        if (chunks == null) {
            if (createIfNonExistant) {
                final List<OIdentifiable> createdList = new ArrayList<>();
                node.setFieldValue(fieldIndex, createdList, OType.LINKLIST);
                node.setFieldValue(fieldIndex, APPEND_SIZE, 0, OType.LONG);
                return createdList;
            } else {
                return null;
            }
        } else {
            return (List<OIdentifiable>) chunks;
        }
    }

    public static void empty(BaseModule baseModule, CommonNode node, FieldIndex fieldIndex) {
        final List<OIdentifiable> chunks = getChunks(node, fieldIndex, false);
        if ((chunks == null) || (chunks.isEmpty())) {
            return;
        }
        for (final OIdentifiable chunk : chunks) {
            baseModule.getDb().delete(chunk.getIdentity());
        }
        node.setFieldValue(fieldIndex, Collections.emptyList(), OType.LINKLIST);
        node.setFieldValue(fieldIndex, APPEND_SIZE, 0, OType.LONG);

    }

    public static void remove(BaseModule baseModule, CommonNode node, FieldIndex fieldIndex) {
        empty(baseModule, node, fieldIndex);
        node.removeFieldValue(fieldIndex);
        node.removeFieldValue(fieldIndex, APPEND_SIZE);
    }

    @Nullable
    public static Long getSize(CommonNode node, FieldIndex fieldIndex) {
        return (Long) node.getFieldValue(fieldIndex, APPEND_SIZE, OType.LONG);
    }

    public static boolean exists(CommonNode node, FieldIndex fieldIndex) {
        return getSize(node, fieldIndex) != null;
    }

    public static void createOfNonExistent(CommonNode node, FieldIndex fieldIndex) {
        if (!exists(node, fieldIndex)) {
            node.setFieldValue(fieldIndex, Collections.emptyList(), OType.LINKLIST);
            node.setFieldValue(fieldIndex, APPEND_SIZE, 0, OType.LONG);
        }
    }

    public static void append(BaseModule baseModule, CommonNode node, FieldIndex fieldIndex,
            InputStream inputStream) throws IOException {
        try {
            final List<OIdentifiable> chunksIn = getChunks(node, fieldIndex, true);
            final List<OIdentifiable> chunks = new ArrayList<>();
            chunks.addAll(chunksIn);
            long currentSize = getSize(node, fieldIndex);

        /* Fill existing chunk? */
            final int modulo = (int) (currentSize % CHUNK_SIZE);
            if (modulo != 0) {
                final int remainingSpace = CHUNK_SIZE - modulo;
                final int blockSize = CHUNK_SIZE - remainingSpace;
                final int block = (int) (currentSize / CHUNK_SIZE);
                final byte[] partialBuf = new byte[remainingSpace];
                final int actuallyRead = inputStream.read(partialBuf, 0, remainingSpace);

                final OIdentifiable chunkOrid = chunks.get(block);
                final ORecordBytes chunkBytes = baseModule.getDb().load(chunkOrid.getIdentity());

                final byte[] newBuffer = new byte[actuallyRead + blockSize];
                System.arraycopy(chunkBytes.toStream(), 0, newBuffer, 0, blockSize);
                System.arraycopy(partialBuf, 0, newBuffer, blockSize, actuallyRead);

                chunkBytes.fromStream(newBuffer);
                chunkBytes.save();

            /* Update size */
                final long prevSize = getSize(node, fieldIndex);
                node.setFieldValue(fieldIndex, APPEND_SIZE, prevSize + actuallyRead, OType.LONG);
            }

        /* Begin append at new block */
            boolean hasMore;
            long size = getSize(node, fieldIndex);
            do {
                final byte[] dataRead = new byte[CHUNK_SIZE];
                final int actuallyRead = inputStream.read(dataRead, 0, CHUNK_SIZE);
                if (actuallyRead != 0) {
                    ORecordBytes newBlock = new ORecordBytes();
                    newBlock.fromStream(dataRead);
                    newBlock.save();
                    chunks.add(newBlock.getIdentity());
                }

                size = size + actuallyRead;
                hasMore = actuallyRead == CHUNK_SIZE;
            } while (hasMore);
            node.setFieldValue(fieldIndex, chunks, OType.LINKLIST);
            node.setFieldValue(fieldIndex, APPEND_SIZE, size, OType.LONG);
        } finally {
            inputStream.close();
        }
    }

    public static void read(BaseModule baseModule, CommonNode node, FieldIndex fieldIndex,
            long skip, long limit, OutputStream outputStream) throws IOException {
        final List<OIdentifiable> chunks = getChunks(node, fieldIndex, false);
        final long size = getSize(node, fieldIndex);
        if (skip + limit > size) {
            throw new IllegalArgumentException("Out of bounds");
        }

        int blockNumber = (int) (skip / CHUNK_SIZE);
        int blockOffset = (int) (skip - (blockNumber * CHUNK_SIZE));
        long dataRead = 0;

        boolean done = false;
        do {
            final OIdentifiable chunkId = chunks.get(blockNumber);
            final ORecordBytes chunkBytes = baseModule.getDb().load(chunkId.getIdentity());

            long dataToReadFromBlock;
            long dataToRead = limit - dataRead;
            if (dataToRead < CHUNK_SIZE) {
                dataToReadFromBlock = dataToRead;
                done = false;
            } else {
                dataToReadFromBlock = CHUNK_SIZE;
            }

            /* Put data to stream */
            final byte[] data = chunkBytes.toStream();
            outputStream.write(data, blockOffset, (int) dataToReadFromBlock);

            dataRead = dataToRead + dataToReadFromBlock;

            /* Next block */
            if (!done) {
                blockNumber++;
                blockOffset = 0;
            }

        } while (done);

    }


}
