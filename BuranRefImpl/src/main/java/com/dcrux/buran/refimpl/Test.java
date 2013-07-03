package com.dcrux.buran.refimpl;

import com.dcrux.buran.commandBase.UncheckedException;
import com.dcrux.buran.commands.classes.ComClassHashIdById;
import com.dcrux.buran.commands.classes.ComClassIdByHash;
import com.dcrux.buran.commands.classes.ComDeclareClass;
import com.dcrux.buran.commands.dataFetch.ComFetch;
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
import com.dcrux.buran.common.fields.setter.FieldSetter;
import com.dcrux.buran.common.fields.setter.SetStr;
import com.dcrux.buran.common.getterSetter.BulkSet;
import com.dcrux.buran.common.labels.ClassLabelName;
import com.dcrux.buran.common.labels.LabelIndex;
import com.dcrux.buran.common.labels.getter.GetLabel;
import com.dcrux.buran.common.labels.getter.GetLabelResult;
import com.dcrux.buran.common.labels.setter.SetLabel;
import com.dcrux.buran.common.labels.targets.LabelTargetInc;
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

        BuranCommandRunner bcr = new BuranCommandRunner(false);
        try {
            ClassDefinition classDef = new ClassDefinition("Hallo Welt",
                    "kfk lkamdlkmalksml kmalksmld kmalsm sdf asdf sd fsdf sdf sdf a.");
            ClassId classId = bcr.sync(thisAccount, sender, new ComDeclareClass(classDef));

            final IIncNid incNid1 = bcr.sync(thisAccount, sender, ComCreateNew.c(classId));
            final IIncNid incNid2 = bcr.sync(thisAccount, sender, ComCreateNew.c(classId));
            final IIncNid incNid3 = bcr.sync(thisAccount, sender, ComCreateNew.c(classId));

            final SetLabel setLabel1 = SetLabel.c(ClassLabelName.c(0))
                    .add(LabelIndex.c(0l), LabelTargetInc.unversioned(incNid3))
                    .add(LabelIndex.c(1), LabelTargetInc.versioned(incNid2));

            bcr.sync(thisAccount, sender, ComMutate.c(incNid1,
                    BulkSet.c(FieldSetter.c(0, SetStr.c("Hallo Welt"))).add(setLabel1)));

            final ICommitResult comResult =
                    bcr.sync(thisAccount, sender, ComCommit.c(incNid1, incNid2, incNid3));

            final NidVer comNode = comResult.getNid(incNid1);

            final String value = bcr.sync(thisAccount, sender,
                    ComFetch.c(comNode, SingleGet.c(0, GetStr.SINGLETON)));

            System.out.println("Data at index 0: " + value);

            final ClassHashId classHashId =
                    bcr.sync(thisAccount, sender, ComClassHashIdById.c(classId));

            System.out.println("ClassID = " + classId + ", hash = " + classHashId);

            final Optional<ClassId> classIdRead =
                    bcr.sync(thisAccount, sender, ComClassIdByHash.c(classHashId));

            System.out.println("ClassID from Hash = " + classIdRead);

            /* Read label targets from node 1*/
            GetLabel labelGet = GetLabel.c(ClassLabelName.c(0), LabelIndex.MIN, LabelIndex.MAX);
            final GetLabelResult result =
                    bcr.sync(thisAccount, sender, ComFetch.c(comNode, labelGet));
            System.out.println("Labels Node1 out (name 0): " + result);


        } catch (UncheckedException uce) {
            uce.getWrapped().printStackTrace();
        }

        bcr.shutdown();
    }
}
