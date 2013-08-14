package com.dcrux.buran.refimpl.modules.text;

import com.dcrux.buran.commands.text.ITransferConfig;
import com.dcrux.buran.commands.text.TextExtract;
import com.dcrux.buran.commands.text.TextTransfer;
import com.dcrux.buran.commands.text.TransferEntry;
import com.dcrux.buran.common.UserId;
import com.dcrux.buran.common.exceptions.IncNodeNotFound;
import com.dcrux.buran.common.exceptions.NodeClassNotFoundException;
import com.dcrux.buran.common.exceptions.NodeNotFoundException;
import com.dcrux.buran.common.fields.FieldTarget;
import com.dcrux.buran.common.fields.setter.FieldAppendStr;
import com.dcrux.buran.common.fields.setter.FieldSetStr;
import com.dcrux.buran.common.fields.setter.FieldSetter;
import com.dcrux.buran.common.fields.setter.IUnfieldedDataSetter;
import com.dcrux.buran.refimpl.modules.BaseModule;
import com.dcrux.buran.refimpl.modules.common.Module;
import com.dcrux.buran.refimpl.modules.fields.FieldConstraintViolationInt;
import com.dcrux.buran.refimpl.modules.nodeWrapper.CommonNode;
import com.dcrux.buran.refimpl.modules.nodeWrapper.FieldIndexAndClassId;
import com.dcrux.buran.refimpl.modules.textExtractor.ParseResult;
import com.dcrux.buran.refimpl.modules.textExtractor.TextUtils;
import com.orientechnologies.orient.core.db.record.OIdentifiable;
import org.apache.tika.exception.TikaException;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.HashSet;
import java.util.Set;

/**
 * Buran.
 *
 * @author: ${USER} Date: 05.08.13 Time: 12:27
 */
public class TextModule extends Module<BaseModule> {

    final TextUtils textUtils = new TextUtils();

    public TextModule(BaseModule baseModule) {
        super(baseModule);
    }

    public void performSet(UserId sender, CommonNode node, TextExtract setter)
            throws IOException, FieldConstraintViolationInt, NodeNotFoundException,
            NodeClassNotFoundException, IncNodeNotFound, TikaException, SAXException {
        final FieldTarget fieldTarget = setter.getBinaryField();
        InputStream binInputStream = BinInputStream.buffered(node, getBase(),
                new FieldIndexAndClassId(fieldTarget.getFieldIndex(), fieldTarget.getClassId(),
                        node.getPrimaryClassId().equals(fieldTarget.getClassId())));
        final ParseResult parseResult = this.textUtils.parse(binInputStream);

        for (final TransferEntry transferEntry : setter.getTransferEntries()) {
            final FieldTarget targetField = transferEntry.getTargetField();
            String stringVal = null;
            Reader readerVal = null;
            switch (transferEntry.getField()) {
                case content:
                    readerVal = parseResult.getReader();
                    //stringVal = org.apache.commons.io.IOUtils.toString(parseResult.getReader());
                    //IOUtils.closeQuietly(parseResult.getReader());
                    break;
                default:
                    throw new UnsupportedOperationException("Not yet implemented");
            }
            final ITransferConfig transfer = transferEntry.getTransferConfig();
            if (readerVal != null) {
                if (transfer instanceof TextTransfer) {
                    final TextTransfer tt = (TextTransfer) transfer;
                    final char[] cBuffer = new char[tt.getLimitChars()];
                    final int numRead = readerVal.read(cBuffer, 0, cBuffer.length);
                    final String str;
                    if (numRead < 1) str = "";
                    else str = new String(cBuffer, 0, numRead);
                    IUnfieldedDataSetter unfieldedSetter;
                    if (tt.isAppend()) {
                        unfieldedSetter = FieldAppendStr.c(str);
                    } else {
                        unfieldedSetter = FieldSetStr.c(str);
                    }
                    //System.out.println("Content: " + str + ", " + str.length());
                    final Set<OIdentifiable> commitableRelations = new HashSet<OIdentifiable>();
                    getBase().getDataMutModule().setDataDirect(sender, node,
                            FieldSetter.c(targetField, unfieldedSetter), commitableRelations);
                } else {
                    throw new IllegalArgumentException("Unknown transfer config");
                }
            }
        }

    }

}
