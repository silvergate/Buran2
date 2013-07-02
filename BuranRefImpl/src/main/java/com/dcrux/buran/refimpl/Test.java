package com.dcrux.buran.refimpl;

import com.dcrux.buran.ClassId;
import com.dcrux.buran.IIncNid;
import com.dcrux.buran.NidVer;
import com.dcrux.buran.UserId;
import com.dcrux.buran.fields.BatchGet;
import com.dcrux.buran.fields.BatchGetResult;
import com.dcrux.buran.fields.BatchSet;
import com.dcrux.buran.fields.FieldIndex;
import com.dcrux.buran.fields.getter.GetStr;
import com.dcrux.buran.fields.setter.SetterString;
import com.dcrux.buran.labels.ClassLabelName;
import com.dcrux.buran.labels.ILabelSet;
import com.dcrux.buran.labels.LabelIndex;
import com.dcrux.buran.labels.LabelTargetInc;
import com.dcrux.buran.labels.getter.GetLabel;
import com.dcrux.buran.labels.getter.GetLabelResult;
import com.dcrux.buran.labels.setter.SetLabel;
import com.dcrux.buran.refimpl.baseModules.BaseModule;
import com.dcrux.buran.refimpl.dao.ITransRet;
import com.dcrux.buran.refimpl.dao.ITransaction;
import com.orientechnologies.orient.core.record.impl.ODocument;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

/**
 * Buran.
 *
 * @author: ${USER} Date: 29.06.13 Time: 14:34
 */
public class Test {
    public static void main(String[] args) throws Throwable {

        ODocument od = new ODocument();
        // od.fromString("key:[true,#10:0],binary:false");
        //od.fromString("key:[#10:0,0s],binary:false");
        //od.field("key", Arrays.asList((long)0, new ORecordId("#10:0")), OType.EMBEDDEDLIST);
        //System.out.println(new String(od.toStream()));

        if (false) {
            return;
        }

        BaseModule.createNew(new UserId(0), true);
        final UserId thisAccount = new UserId(0);
        final UserId sender = new UserId(332);

        BaseModule ri = new BaseModule(thisAccount);

        final ITransRet<IIncNid> t1 = ri.getIncubationSrv().createInc(sender, new ClassId(212));
        IIncNid inid = ri.getDbUtils().run(t1);

        final ITransRet<IIncNid> createNode2 =
                ri.getIncubationSrv().createInc(sender, new ClassId(212));
        IIncNid node2 = ri.getDbUtils().run(createNode2);

                /* Verbindung von node 1 nach node 2 */
        SetLabel sl = new SetLabel(ClassLabelName.c(3));
        sl.add(LabelIndex.c(0), LabelTargetInc.unversioned(node2));
        sl.add(LabelIndex.c(32323), LabelTargetInc.unversioned(node2));

        final ITransaction t2 = ri.getIncubationSrv()
                .setData(sender, inid, BatchSet.c(FieldIndex.c(0), SetterString.c("Hallo Welt")),
                        Collections.<ILabelSet>singleton(sl));
        ri.getDbUtils().run(t2);

        final ITransRet<Map<IIncNid, NidVer>> t3 =
                ri.getIncubationSrv().commit(sender, Arrays.asList(inid, node2));
        final Map<IIncNid, NidVer> nidVerR = ri.getDbUtils().run(t3);
        System.out.println(nidVerR);
        final NidVer nidVer = nidVerR.get(inid);


        final ITransRet<BatchGetResult> t4 =
                ri.getDataSrv().getData(nidVer, BatchGet.c(FieldIndex.c(0), GetStr.SINGLETON));
        final BatchGetResult data = ri.getDbUtils().run(t4);

        System.out.println("Data = " + data.getValues());

        GetLabel gl = new GetLabel(ClassLabelName.c(3), LabelIndex.c(Long.MIN_VALUE),
                LabelIndex.c(Long.MAX_VALUE));
        GetLabelResult labels = ri.getDbUtils().run(ri.getDataSrv().performLabelGet(nidVer, gl));
        System.out.println("Labels at node 1: " + labels);


        ri.close();
    }
}
