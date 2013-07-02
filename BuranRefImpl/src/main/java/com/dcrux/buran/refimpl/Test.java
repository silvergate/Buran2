package com.dcrux.buran.refimpl;

import com.dcrux.buran.commandBase.UncheckedException;
import com.dcrux.buran.commandBase.VoidType;
import com.dcrux.buran.commands.classes.ComClassHashIdById;
import com.dcrux.buran.commands.classes.ComClassIdByHash;
import com.dcrux.buran.commands.classes.ComDeclareClass;
import com.dcrux.buran.commands.dataFetch.ComFetch;
import com.dcrux.buran.commands.dataFetch.FetchResult;
import com.dcrux.buran.commands.dataMut.ComMutate;
import com.dcrux.buran.commands.incubation.ComCommit;
import com.dcrux.buran.commands.incubation.ComCreateNew;
import com.dcrux.buran.commands.incubation.ICommitResult;
import com.dcrux.buran.common.IIncNid;
import com.dcrux.buran.common.NidVer;
import com.dcrux.buran.common.UserId;
import com.dcrux.buran.common.classDefinition.ClassDefinition;
import com.dcrux.buran.common.classes.ClassHashId;
import com.dcrux.buran.common.classes.ClassId;
import com.dcrux.buran.common.fields.getter.GetStr;
import com.dcrux.buran.common.fields.getter.SingleGet;
import com.dcrux.buran.common.fields.setter.DataSetter;
import com.dcrux.buran.common.fields.setter.SetStr;
import com.dcrux.buran.refimpl.baseModules.BaseModule;
import com.dcrux.buran.refimpl.commandRunner.BuranCommandRunner;
import com.google.common.base.Optional;
import com.orientechnologies.orient.core.record.impl.ODocument;

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

        /*BaseModule ri = new BaseModule(thisAccount, sender);

        final ITransRet<IIncNid> t1 = ri.getIncubationModule().createInc(sender, new ClassId(212));
        IIncNid inid = ri.getDbUtils().run(t1);

        final ITransRet<IIncNid> createNode2 =
                ri.getIncubationModule().createInc(sender, new ClassId(212));
        IIncNid node2 = ri.getDbUtils().run(createNode2);

        SetLabel sl = new SetLabel(ClassLabelName.c(3));
        sl.add(LabelIndex.c(0), LabelTargetInc.unversioned(node2));
        sl.add(LabelIndex.c(32323), LabelTargetInc.unversioned(node2));

        final ITransaction t2 = ri.getDataMutModule()
                .setData(sender, inid, DataSetter.c(FieldIndex.c(0), SetStr.c("Hallo Welt")),
                        Collections.<ILabelSet>singleton(sl));
        ri.getDbUtils().run(t2);

        final ITransRet<Map<IIncNid, NidVer>> t3 =
                ri.getCommitModule().commitOld(sender, Arrays.asList(inid, node2));
        final Map<IIncNid, NidVer> nidVerR = ri.getDbUtils().run(t3);
        System.out.println(nidVerR);
        final NidVer nidVer = nidVerR.get(inid);


        final ITransRet<BatchGetResult> t4 =
                ri.getDataFetchModule().getData(nidVer, BatchGet.c(FieldIndex.c(0),
                GetStr.SINGLETON));
        final BatchGetResult data = ri.getDbUtils().run(t4);

        System.out.println("Data = " + data.getValues());

        GetLabel gl = new GetLabel(ClassLabelName.c(3), LabelIndex.c(Long.MIN_VALUE),
                LabelIndex.c(Long.MAX_VALUE));
        GetLabelResult labels = ri.getDbUtils().run(ri.getDataFetchModule().performLabelGet
        (nidVer, gl));
        System.out.println("Labels at node 1: " + labels);


        ri.close();*/


        BuranCommandRunner bcr = new BuranCommandRunner(false);
        try {
            ClassDefinition classDef = new ClassDefinition("Hallo Welt",
                    "kfk lkamdlkmalksml kmalksmld kmalsm sdf asdf sd fsdf sdf sdf a.");
            ClassId classId = bcr.synchronous(thisAccount, sender, new ComDeclareClass(classDef));

            final IIncNid incNid1 = bcr.synchronous(thisAccount, sender, ComCreateNew.c(classId));

            bcr.synchronous(thisAccount, sender,
                    ComMutate.field(incNid1, DataSetter.c(0, SetStr.c("Hallo Welt"))));

            final ICommitResult comResult =
                    bcr.synchronous(thisAccount, sender, ComCommit.c(incNid1));

            final NidVer comNode = comResult.getNid(incNid1);

            final FetchResult<String, VoidType> value = bcr.synchronous(thisAccount, sender,
                    ComFetch.field(comNode, SingleGet.c(0, GetStr.SINGLETON)));

            System.out.println("Data at index 0: " + value.getFieldResult().get());

            final ClassHashId classHashId =
                    bcr.synchronous(thisAccount, sender, ComClassHashIdById.c(classId));

            System.out.println("ClassID = " + classId + ", hash = " + classHashId);

            final Optional<ClassId> classIdRead =
                    bcr.synchronous(thisAccount, sender, ComClassIdByHash.c(classHashId));

            System.out.println("ClassID from Hash = " + classIdRead);


        } catch (UncheckedException uce) {
            uce.getWrapped().printStackTrace();
        }

        bcr.shutdown();
    }
}
