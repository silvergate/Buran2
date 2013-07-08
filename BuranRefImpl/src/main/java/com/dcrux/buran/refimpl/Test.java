package com.dcrux.buran.refimpl;

import com.dcrux.buran.commandBase.UncheckedException;
import com.dcrux.buran.commands.classes.ComClassHashIdById;
import com.dcrux.buran.commands.classes.ComClassIdByHash;
import com.dcrux.buran.commands.classes.ComDeclareClass;
import com.dcrux.buran.commands.dataFetch.ComFetch;
import com.dcrux.buran.commands.dataMut.ComMutate;
import com.dcrux.buran.commands.incubation.ComCommit;
import com.dcrux.buran.commands.incubation.ComCreateNew;
import com.dcrux.buran.commands.incubation.ComCreateUpdate;
import com.dcrux.buran.commands.incubation.ICommitResult;
import com.dcrux.buran.common.IIncNid;
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
import com.dcrux.buran.common.fields.getter.FieldGetAll;
import com.dcrux.buran.common.fields.getter.FieldGetResult;
import com.dcrux.buran.common.fields.getter.FieldGetStr;
import com.dcrux.buran.common.fields.getter.SingleGet;
import com.dcrux.buran.common.fields.setter.FieldSetInt;
import com.dcrux.buran.common.fields.setter.FieldSetStr;
import com.dcrux.buran.common.fields.setter.FieldSetter;
import com.dcrux.buran.common.fields.types.IntegerType;
import com.dcrux.buran.common.fields.types.StringType;
import com.dcrux.buran.common.getterSetter.BulkSet;
import com.dcrux.buran.common.getterSetter.IDataSetter;
import com.dcrux.buran.indexing.IndexDefinition;
import com.dcrux.buran.indexing.mapFunction.MapFunction;
import com.dcrux.buran.indexing.mapInput.FieldTarget;
import com.dcrux.buran.indexing.mapInput.NodeMapInput;
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

import java.text.MessageFormat;
import java.util.Map;

/**
 * Buran.
 *
 * @author: ${USER} Date: 29.06.13 Time: 14:34
 */
public class Test {

    public static final FieldIndex C1_I0 = FieldIndex.c(0);
    public static final FieldIndex C1_I1 = FieldIndex.c(1);
    public static final ClassIndexName C1_I1_INDEX = new ClassIndexName("byI1");


    public static ClassDefinition cdef1() {
        NodeMapInput nodeMapInput = new NodeMapInput();
        nodeMapInput.getFields().put(VarName.c("input"), new FieldTarget(C1_I1, true));
        Block block = new Block();
        block.add(FunRet.c(FunListNew.c().add(FunIntToBin.c(FunGet
                .c(VarName.c("input"), com.dcrux.buran.scripting.iface.types.IntegerType.class),
                com.dcrux.buran.scripting.iface.types.IntegerType.NumOfBits.int64))
                .add(FunIntLit.c(212)).get()));
        MapFunction mapFunction = MapFunction.single(block);
        IndexDefinition byField1 = new IndexDefinition(nodeMapInput, mapFunction);

        ClassDefinition classDef = new ClassDefinition("Hallo Welt",
                "kfk lkamdlkmalksml kmalksmld kmalsm sdf asdf sd fsdf sdf sdf a.");
        classDef.getFields().add(C1_I0, new StringType(0, 200), false)
                .add(C1_I1, IntegerType.cInt16Range(), false);
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
        try {
            ClassId classId = bcr.sync(thisAccount, sender, new ComDeclareClass(cdef1()));

            final IIncNid incNid1 = bcr.sync(thisAccount, sender, ComCreateNew.c(classId));
            final IIncNid incNid2 = bcr.sync(thisAccount, sender, ComCreateNew.c(classId));
            final IIncNid incNid3 = bcr.sync(thisAccount, sender, ComCreateNew.c(classId));

            final SetEdge setEdge1 = SetEdge.c(ClassLabelName.c(0))
                    .add(LabelIndex.c(0l), EdgeTargetInc.unversioned(incNid3))
                    .add(LabelIndex.c(1), EdgeTargetInc.versioned(incNid2));

            bcr.sync(thisAccount, sender, ComMutate.c(incNid1, BulkSet.c(
                    FieldSetter.c(0, FieldSetStr.c("Hallo Welt")).add(1, FieldSetInt.c(32)))
                    .add(setEdge1)));

            final ICommitResult comResult =
                    bcr.sync(thisAccount, sender, ComCommit.c(incNid1, incNid2, incNid3));

            final NidVer comNode = comResult.getNid(incNid1);
            final NidVer node3 = comResult.getNid(incNid3);
            final NidVer node2 = comResult.getNid(incNid2);

            final String value = bcr.sync(thisAccount, sender,
                    ComFetch.c(comNode, SingleGet.c(0, FieldGetStr.SINGLETON)));

            System.out.println("Data at index 0: " + value);

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

            IIncNid changedNode = bcr.sync(thisAccount, sender, comCreateUpdate);

            final IDataSetter changeLabelSetter =
                    FieldSetter.c(0, FieldSetStr.c("Ich bin eine Änderung!"))
                            .add(C1_I1, FieldSetInt.c(666));
            bcr.sync(thisAccount, sender, ComMutate.c(changedNode, changeLabelSetter));

            final IEdgeSetter newLabel = SetEdge.c(ClassLabelName.c(0))
                    .add(LabelIndex.c(33), EdgeTargetInc.versioned(changedNode));
            bcr.sync(thisAccount, sender, ComMutate.c(changedNode, newLabel));

            NidVer changedNodeCommited =
                    bcr.sync(thisAccount, sender, ComCommit.c(changedNode)).getNid(changedNode);

            /* Änderung holen */
            final String value2 = bcr.sync(thisAccount, sender,
                    ComFetch.c(changedNodeCommited, SingleGet.c(0, FieldGetStr.SINGLETON)));

            System.out.println("Data at index 0 (After change): " + value2);

            GetEdge labelGet2 = GetEdge.c(ClassLabelName.c(0), LabelIndex.MIN, LabelIndex.MAX);
            final GetEdgeResult result2 =
                    bcr.sync(thisAccount, sender, ComFetch.c(changedNodeCommited, labelGet));
            System.out.println("Labels Node1 out (name 0): " + result2);

            /* Get all fields */
            final FieldGetResult allFields = bcr.sync(thisAccount, sender,
                    ComFetch.c(changedNodeCommited, FieldGetAll.SINGLETON));
            for (final Map.Entry<FieldIndex, Object> fields : allFields.getValues().entrySet()) {
                System.out.println(MessageFormat
                        .format("  - {0}, Value: {1}", fields.getKey(), fields.getValue()));
            }

            /* Get in-nodes */
            GetInClassEdgeResult allInNodes = bcr.sync(thisAccount, sender, ComFetch.c(node3,
                    GetInClassEdge
                            .c(classId, ClassLabelName.c(0), LabelIndex.MIN, LabelIndex.MAX)));
            System.out.println("All in nodes to node " + node3.getNid().getAsString() + ": " +
                    allInNodes);

        } catch (UncheckedException uce) {
            uce.getWrapped().printStackTrace();
        }

        bcr.shutdown();
    }
}
