package com.dcrux.buran.refimpl;

import com.dcrux.buran.callbacksBase.ICallbackCommand;
import com.dcrux.buran.commandBase.UncheckedException;
import com.dcrux.buran.commands.classes.ComClassHashIdById;
import com.dcrux.buran.commands.classes.ComClassIdByHash;
import com.dcrux.buran.commands.classes.ComDeclareClass;
import com.dcrux.buran.commands.dataFetch.ComFetch;
import com.dcrux.buran.commands.dataMut.ComMutate;
import com.dcrux.buran.commands.incubation.ComCommit;
import com.dcrux.buran.commands.incubation.ComCreateNew;
import com.dcrux.buran.commands.incubation.ComCreateUpdate;
import com.dcrux.buran.commands.incubation.CommitResult;
import com.dcrux.buran.commands.indexing.ComQuery;
import com.dcrux.buran.commands.subscription.ComAddSub;
import com.dcrux.buran.common.INidOrNidVer;
import com.dcrux.buran.common.IncNid;
import com.dcrux.buran.common.NidVer;
import com.dcrux.buran.common.UserId;
import com.dcrux.buran.common.classDefinition.ClassDefinition;
import com.dcrux.buran.common.classDefinition.ClassIndexName;
import com.dcrux.buran.common.classes.ClassHashId;
import com.dcrux.buran.common.classes.ClassId;
import com.dcrux.buran.common.edges.ClassLabelName;
import com.dcrux.buran.common.edges.IEdgeSetter;
import com.dcrux.buran.common.edges.LabelIndex;
import com.dcrux.buran.common.edges.getter.GetEdge;
import com.dcrux.buran.common.edges.getter.GetEdgeResult;
import com.dcrux.buran.common.edges.getter.GetInClassEdge;
import com.dcrux.buran.common.edges.getter.GetInClassEdgeResult;
import com.dcrux.buran.common.edges.setter.SetEdge;
import com.dcrux.buran.common.edges.targets.EdgeTargetInc;
import com.dcrux.buran.common.fields.FieldIndex;
import com.dcrux.buran.common.fields.getter.*;
import com.dcrux.buran.common.fields.setter.FieldSetBin;
import com.dcrux.buran.common.fields.setter.FieldSetInt;
import com.dcrux.buran.common.fields.setter.FieldSetStr;
import com.dcrux.buran.common.fields.setter.FieldSetter;
import com.dcrux.buran.common.fields.types.BinaryType;
import com.dcrux.buran.common.fields.types.IntegerType;
import com.dcrux.buran.common.fields.types.StringType;
import com.dcrux.buran.common.getterSetter.BulkSet;
import com.dcrux.buran.common.getterSetter.IDataSetter;
import com.dcrux.buran.common.indexing.IndexDefinition;
import com.dcrux.buran.common.indexing.keyGen.NumberKeyGen;
import com.dcrux.buran.common.indexing.keyGen.RangeIndexKeyGen;
import com.dcrux.buran.common.indexing.mapFunction.MapFunction;
import com.dcrux.buran.common.indexing.mapInput.FieldTarget;
import com.dcrux.buran.common.indexing.mapInput.NodeMapInput;
import com.dcrux.buran.common.indexing.mapStore.MapIndex;
import com.dcrux.buran.common.subscription.SubBlockId;
import com.dcrux.buran.common.subscription.SubDefinition;
import com.dcrux.buran.refimpl.baseModules.BaseModule;
import com.dcrux.buran.refimpl.commandRunner.BuranCommandRunner;
import com.dcrux.buran.scripting.functions.FunGet;
import com.dcrux.buran.scripting.functions.FunRet;
import com.dcrux.buran.scripting.functions.integer.FunIntLit;
import com.dcrux.buran.scripting.functions.integer.FunIntToBin;
import com.dcrux.buran.scripting.functions.list.FunListNew;
import com.dcrux.buran.scripting.iface.Block;
import com.dcrux.buran.scripting.iface.VarName;
import com.google.common.base.Optional;
import com.orientechnologies.orient.core.record.impl.ODocument;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Buran.
 *
 * @author: ${USER} Date: 29.06.13 Time: 14:34
 */
public class Test {

    public static final FieldIndex C1_I0 = FieldIndex.c(0);
    public static final FieldIndex C1_I1 = FieldIndex.c(1);
    public static final FieldIndex C1_I2 = FieldIndex.c(2);
    public static final ClassIndexName C1_I1_INDEX = new ClassIndexName("byI1");

    public static void registerSubListener(BuranCommandRunner bcr, UserId thisAccount) {
        final LinkedBlockingDeque<ICallbackCommand> cbq = new LinkedBlockingDeque<>();
        bcr.register(thisAccount, cbq);
        final Runnable queueRunner = new Runnable() {
            @Override
            public void run() {
                try {
                    do {
                        final ICallbackCommand element = cbq.take();
                        System.out.println("Got new Callack: " + element);
                    } while (true);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        final Thread nt = new Thread(queueRunner);
        nt.start();
    }

    public static ClassDefinition cdef1() {
        NodeMapInput nodeMapInput = new NodeMapInput();
        nodeMapInput.getFields().put(VarName.c("input"), FieldTarget.cRequired(C1_I1));
        Block block = new Block();
        block.add(FunRet.c(FunListNew.c().add(FunIntToBin.c(FunGet
                .c(VarName.c("input"), com.dcrux.buran.scripting.iface.types.IntegerType.class),
                com.dcrux.buran.scripting.iface.types.IntegerType.NumOfBits.int64))
                .add(FunIntLit.c(212)).get()));
        MapFunction mapFunction = MapFunction.single(block);
        IndexDefinition byField1 =
                new IndexDefinition(nodeMapInput, mapFunction, new MapIndex(true));

        ClassDefinition classDef = new ClassDefinition("Hallo Welt",
                "kfk lkamdlkmalksml kmalksmld kmalsm sdf asdf sd fsdf sdf sdf a.");
        classDef.getFields().add(C1_I0, new StringType(0, 200), false)
                .add(C1_I1, IntegerType.cInt32Range(), false)
                .add(C1_I2, BinaryType.c(100000000), false);
        classDef.getIndexes().add(C1_I1_INDEX, byField1);
        return classDef;
    }

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
        registerSubListener(bcr, thisAccount);
        try {
            ClassId classId = bcr.sync(thisAccount, sender, new ComDeclareClass(cdef1()));

            /* Add subscription */
            ComAddSub comAddSub = ComAddSub.c(new SubBlockId("daBlock"),
                    new SubDefinition(classId, C1_I1_INDEX,
                            RangeIndexKeyGen.from(NumberKeyGen.int64(Integer.MIN_VALUE))));
            bcr.sync(thisAccount, sender, comAddSub);

            final IncNid incNid1 = bcr.sync(thisAccount, sender, ComCreateNew.c(classId));
            final IncNid incNid2 = bcr.sync(thisAccount, sender, ComCreateNew.c(classId));
            final IncNid incNid3 = bcr.sync(thisAccount, sender, ComCreateNew.c(classId));

            final SetEdge setEdge1 = SetEdge.c(ClassLabelName.c(0))
                    .add(LabelIndex.c(0l), EdgeTargetInc.unversioned(incNid3))
                    .add(LabelIndex.c(1), EdgeTargetInc.unversioned(incNid2));

            byte[] binValue = new byte[80000];

            bcr.sync(thisAccount, sender, ComMutate.c(incNid1, BulkSet.c(
                    FieldSetter.c(0, FieldSetStr.c("Hallo Welt")).add(1, FieldSetInt.c(32)))
                    .add(setEdge1)));
            bcr.sync(thisAccount, sender, ComMutate.c(incNid2, BulkSet.c(
                    FieldSetter.c(0, FieldSetStr.c("Ich bin Node 1")).add(1, FieldSetInt.c(344333)))
                    .add(setEdge1)));
            bcr.sync(thisAccount, sender, ComMutate
                    .c(incNid2, BulkSet.c(FieldSetter.c(0, FieldSetStr.c("Noch ne welt")))));
            bcr.sync(thisAccount, sender, ComMutate.c(incNid1,
                    FieldSetter.c(1, FieldSetInt.c(32312)).add(2, FieldSetBin.c(binValue))));

            final CommitResult comResult =
                    bcr.sync(thisAccount, sender, ComCommit.c(incNid1, incNid2, incNid3));

            final NidVer comNode = comResult.getNid(incNid1);
            final NidVer node3 = comResult.getNid(incNid3);
            final NidVer node2 = comResult.getNid(incNid2);

            final String value = bcr.sync(thisAccount, sender,
                    ComFetch.c(comNode, SingleGet.c(0, FieldGetStr.SINGLETON)));

            System.out.println("Data at indexAndNotify 0: " + value);

            final ClassHashId classHashId =
                    bcr.sync(thisAccount, sender, ComClassHashIdById.c(classId));

            System.out.println("ClassID = " + classId + ", hash = " + classHashId);

            final Optional<ClassId> classIdRead =
                    bcr.sync(thisAccount, sender, ComClassIdByHash.c(classHashId));

            System.out.println("ClassID from Hash = " + classIdRead);

            /* Read edge targets from node 1*/
            GetEdge labelGet = GetEdge.c(ClassLabelName.c(0), LabelIndex.MIN, LabelIndex.MAX);
            final GetEdgeResult result =
                    bcr.sync(thisAccount, sender, ComFetch.c(comNode, labelGet));
            System.out.println("Labels Node1 out (name 0): " + result);

            /* Update comNode */
            ComCreateUpdate comCreateUpdate = ComCreateUpdate.c(comNode);

            IncNid changedNode = bcr.sync(thisAccount, sender, comCreateUpdate);

            final IDataSetter changeLabelSetter = BulkSet.c(
                    FieldSetter.c(0, FieldSetStr.c("Ich bin eine Änderung!"))
                            .add(C1_I1, FieldSetInt.c(666)));
            bcr.sync(thisAccount, sender, ComMutate.c(changedNode, changeLabelSetter));

            final IEdgeSetter newLabel = SetEdge.c(ClassLabelName.c(0))
                    .add(LabelIndex.c(33), EdgeTargetInc.unversioned(changedNode));
            bcr.sync(thisAccount, sender, ComMutate.c(changedNode, newLabel));

            NidVer changedNodeCommited =
                    bcr.sync(thisAccount, sender, ComCommit.c(changedNode)).getNid(changedNode);

            /* Änderung holen */
            final String value2 = bcr.sync(thisAccount, sender,
                    ComFetch.c(changedNodeCommited, SingleGet.c(0, FieldGetStr.SINGLETON)));

            System.out.println("Data at indexAndNotify 0 (After change): " + value2);

            GetEdge labelGet2 = GetEdge.c(ClassLabelName.c(0), LabelIndex.MIN, LabelIndex.MAX);
            final GetEdgeResult result2 =
                    bcr.sync(thisAccount, sender, ComFetch.c(changedNodeCommited, labelGet));
            System.out.println("Labels Node1 out (name 0): " + result2);

            /* Get all fields */
            final FieldGetResult allFields = bcr.sync(thisAccount, sender,
                    ComFetch.c(changedNodeCommited, FieldGetAll.SINGLETON));
            for (final Map.Entry<FieldIndex, Serializable> fields : allFields.getValues()
                    .entrySet()) {
                System.out.println(MessageFormat
                        .format("  - {0}, Value: {1}", fields.getKey(), fields.getValue()));
            }

            /* Das file lesen */
            final ComFetch<byte[]> binResult =
                    ComFetch.c(changedNodeCommited, SingleGet.c(C1_I2, new FieldGetBin(0, 100)));
            final byte[] binResult2 = bcr.sync(thisAccount, sender, binResult);
            System.out.println("Binary Read: " + Arrays.toString(binResult2));

            /* Get in-nodes */
            GetInClassEdgeResult allInNodes = bcr.sync(thisAccount, sender, ComFetch.c(node3,
                    new GetInClassEdge(Optional.<INidOrNidVer>absent(), Optional.of(classId),
                            ClassLabelName.c(0), LabelIndex.MIN, LabelIndex.MAX, false)));
            System.out.println("All in nodes to node " + node3.getAsString() + ": " +
                    allInNodes);

            /* Index-Abfrage */
            ComQuery cq = new ComQuery(classId, C1_I1_INDEX,
                    RangeIndexKeyGen.from(NumberKeyGen.int64(Integer.MIN_VALUE)));
            bcr.sync(thisAccount, sender, cq);

        } catch (UncheckedException uce) {
            uce.getWrapped().printStackTrace();
        }

        bcr.shutdown();

        Thread.sleep(500);
        System.exit(0);
    }
}
