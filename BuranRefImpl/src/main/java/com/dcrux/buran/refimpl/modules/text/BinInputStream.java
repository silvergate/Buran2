package com.dcrux.buran.refimpl.modules.text;

import com.dcrux.buran.refimpl.modules.BaseModule;
import com.dcrux.buran.refimpl.modules.fields.fieldPerformer.binary.BinaryUtil;
import com.dcrux.buran.refimpl.modules.nodeWrapper.CommonNode;
import com.dcrux.buran.refimpl.modules.nodeWrapper.FieldIndexAndClassId;
import com.orientechnologies.orient.core.db.ODatabaseRecordThreadLocal;
import com.orientechnologies.orient.core.db.document.ODatabaseDocument;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Buran.
 *
 * @author: ${USER} Date: 11.08.13 Time: 23:37
 */
public class BinInputStream extends InputStream {

    private final CommonNode node;
    private final BaseModule baseModule;
    private final FieldIndexAndClassId fieldIndexAndClassId;
    private long index;
    private ODatabaseDocument db;

    public static InputStream buffered(CommonNode node, BaseModule baseModule,
            FieldIndexAndClassId fieldIndexAndClassId) {
        final BinInputStream bis = new BinInputStream(node, baseModule, fieldIndexAndClassId);
        final BufferedInputStream bufIs = new BufferedInputStream(bis, BinaryUtil.CHUNK_SIZE);
        return bufIs;
    }

    public BinInputStream(CommonNode node, BaseModule baseModule,
            FieldIndexAndClassId fieldIndexAndClassId) {
        this.node = node;
        this.baseModule = baseModule;
        this.fieldIndexAndClassId = fieldIndexAndClassId;
        this.db = this.baseModule.getDb();
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        if (off < 0) {
            throw new IndexOutOfBoundsException("off<0");
        }
        if (len < 0) {
            throw new IndexOutOfBoundsException("len<0");
        }
        if (len > b.length - off) {
            throw new IndexOutOfBoundsException("len>b.length-off");
        }
        ODatabaseRecordThreadLocal.INSTANCE.set(this.db);
        final Long size = BinaryUtil.getSize(this.node, this.fieldIndexAndClassId);
        if (size == null) {
            return -1;
        }
        if (this.index >= size) {
            return -1;
        }
        long adjustedLen;
        if (this.index + len > size) {
            adjustedLen = size - this.index;
        } else {
            adjustedLen = len;
        }

        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BinaryUtil.read(this.baseModule, this.node, this.fieldIndexAndClassId, this.index,
                adjustedLen, byteArrayOutputStream);
        final int actuallyRead = byteArrayOutputStream.size();
        this.index += actuallyRead;
        final byte[] bytes = byteArrayOutputStream.toByteArray();
        System.arraycopy(bytes, 0, b, off, actuallyRead);

        return actuallyRead;
    }

    @Override
    public int read() throws IOException {
        final byte[] buf = new byte[1];
        final int actuallRead = read(buf, 0, 1);
        if (actuallRead <= 0) {
            return -1;
        } else {
            return buf[0] & 0xff;
        }

        /*
        ODatabaseRecordThreadLocal.INSTANCE.set(this.db);
        final Long size = BinaryUtil.getSize(this.node, this.fieldIndexAndClassId);
        if (size == null) {
            return -1;
        }
        if (this.index >= size) {
            return -1;
        }
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BinaryUtil.read(this.baseModule, this.node, this.fieldIndexAndClassId, this.index, 1,
                byteArrayOutputStream);
        this.index++;

        final byte[] bytes = byteArrayOutputStream.toByteArray();

        if (bytes.length > 0) {
            return bytes[0] & 0xff;
        } else {
            return -1;
        } */
    }
}
