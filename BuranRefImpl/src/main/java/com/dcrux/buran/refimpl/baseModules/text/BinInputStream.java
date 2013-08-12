package com.dcrux.buran.refimpl.baseModules.text;

import com.dcrux.buran.refimpl.baseModules.BaseModule;
import com.dcrux.buran.refimpl.baseModules.fields.fieldPerformer.binary.BinaryUtil;
import com.dcrux.buran.refimpl.baseModules.nodeWrapper.CommonNode;
import com.dcrux.buran.refimpl.baseModules.nodeWrapper.FieldIndexAndClassId;
import com.orientechnologies.orient.core.db.ODatabaseRecordThreadLocal;
import com.orientechnologies.orient.core.db.document.ODatabaseDocument;

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

    public BinInputStream(CommonNode node, BaseModule baseModule,
            FieldIndexAndClassId fieldIndexAndClassId) {
        this.node = node;
        this.baseModule = baseModule;
        this.fieldIndexAndClassId = fieldIndexAndClassId;
        this.db = this.baseModule.getDb();
    }

    @Override
    public int read() throws IOException {
        ODatabaseRecordThreadLocal.INSTANCE.set(this.db);
        final Long size = BinaryUtil.getSize(this.node, this.fieldIndexAndClassId);
        if (size == null) {
            //System.out.println("Size == null");
            return -1;
        }
        if (this.index >= size) {
            //System.out.println("this.index>=size");
            return -1;
        }
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BinaryUtil.read(this.baseModule, this.node, this.fieldIndexAndClassId, this.index, 1,
                byteArrayOutputStream);
        this.index++;

        final byte[] bytes = byteArrayOutputStream.toByteArray();
        //System.out.println("BinInputStream: " + this.index + Arrays.toString(bytes));

        if (bytes.length > 0) {
            return bytes[0] & 0xff;
        } else {
            return -1;
        }
    }
}
