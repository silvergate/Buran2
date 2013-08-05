package ch.dcrux.buran.apiUsage;

import com.dcrux.buran.commandBase.ICommand;
import com.dcrux.buran.commandBase.UncheckedException;
import com.dcrux.buran.commandBase.UnknownCommandException;
import com.dcrux.buran.commandBase.WrappedExpectableException;
import com.dcrux.buran.commands.classes.ComDeclareClass;
import com.dcrux.buran.commands.dataFetch.ComFetch;
import com.dcrux.buran.commands.dataMut.ComMutate;
import com.dcrux.buran.commands.incubation.ComCommit;
import com.dcrux.buran.commands.incubation.ComCreateNew;
import com.dcrux.buran.commands.incubation.ComCreateUpdate;
import com.dcrux.buran.commands.incubation.CommitResult;
import com.dcrux.buran.common.IncNid;
import com.dcrux.buran.common.NidVer;
import com.dcrux.buran.common.classDefinition.ClassDefinition;
import com.dcrux.buran.common.classes.ClassId;
import com.dcrux.buran.common.fields.FieldIndex;
import com.dcrux.buran.common.fields.getter.FieldGetStr;
import com.dcrux.buran.common.fields.getter.SingleGet;
import com.dcrux.buran.common.fields.setter.FieldSetLink;
import com.dcrux.buran.common.fields.setter.FieldSetStr;
import com.dcrux.buran.common.fields.setter.FieldSetter;
import com.dcrux.buran.common.fields.types.LinkType;
import com.dcrux.buran.common.fields.types.StringType;
import com.dcrux.buran.common.inRelations.InRealtionGetter;
import com.dcrux.buran.common.inRelations.InRelationResult;
import com.dcrux.buran.common.inRelations.selector.InRelSelCount;
import com.dcrux.buran.common.inRelations.selector.InRelSelTarget;
import com.dcrux.buran.common.inRelations.where.InRelWhereClassId;
import com.dcrux.buran.common.inRelations.where.InRelWhereVersioned;
import com.dcrux.buran.common.indexing.IndexDefinition;
import com.dcrux.buran.common.indexing.mapFunction.MapFunction;
import com.dcrux.buran.common.indexing.mapInput.FieldTarget;
import com.dcrux.buran.common.indexing.mapInput.NodeMapInput;
import com.dcrux.buran.common.indexing.mapStore.MapIndex;
import com.dcrux.buran.common.link.LinkTargetInc;
import com.dcrux.buran.scripting.functions.FunGet;
import com.dcrux.buran.scripting.functions.FunRet;
import com.dcrux.buran.scripting.functions.integer.FunIntLit;
import com.dcrux.buran.scripting.functions.integer.FunIntToBin;
import com.dcrux.buran.scripting.functions.list.FunListNew;
import com.dcrux.buran.scripting.iface.Block;
import com.dcrux.buran.scripting.iface.VarName;
import com.google.common.base.Optional;
import com.sun.istack.internal.Nullable;

import java.util.ArrayList;

/**
 * Buran.
 *
 * @author: ${USER} Date: 17.07.13 Time: 23:44
 */
public class DescModule extends Module<BaseModule> {

    private ClassId fileClassId;

    //public static final FieldIndex FIELD_TARGET_CLASSID = FieldIndex.c(3);
    public static final FieldIndex FIELD_TITLE = FieldIndex.c(0);
    public static final FieldIndex FIELD_LONG_DESC = FieldIndex.c(1);
    public static final FieldIndex FIELD_DESCRIBES = FieldIndex.c(2);
    //public static final ClassLabelName FIELD_TARGET = ClassLabelName.c(0);
    //public static final LabelIndex FIELD_TARGET_LI = LabelIndex.c(0);

    public DescModule(BaseModule baseModule) {
        super(baseModule);
    }

    private ClassDefinition getFileClassDef() {
        ClassDefinition classDef = new ClassDefinition("A described node",
                "Eine node welche eine andere node beschreibt.");
        classDef.getFields().add(FIELD_TITLE, new StringType(0, 255), false)
                .add(FIELD_LONG_DESC, new StringType(0, StringType.MAXLEN_LIMIT), false)
                .add(FIELD_DESCRIBES, LinkType.c(), false);
        return classDef;
    }

    private IndexDefinition getIndexByFileSize() {
        final VarName fsVarName = VarName.c("fileSize");
        final NodeMapInput mapInput = new NodeMapInput();
        mapInput.getFields().put(fsVarName, FieldTarget.cRequired(FIELD_TITLE));

        Block mapBlock = new Block();
        mapBlock.add(FunRet.c(FunListNew.c().add(FunIntToBin
                .c(FunGet.c(fsVarName, com.dcrux.buran.scripting.iface.types.IntegerType.class),
                        com.dcrux.buran.scripting.iface.types.IntegerType.NumOfBits.int64))
                .add(FunIntLit.c(0)).get()));

        MapFunction mapFunction = MapFunction.single(mapBlock);
        MapIndex mapIndex = new MapIndex(true);

        final IndexDefinition indexDefinition =
                new IndexDefinition(mapInput, mapFunction, mapIndex);
        return indexDefinition;
    }

    public IncNid describe(Optional<NidVer> toUpdate, final String shortDesc, LinkTargetInc target)
            throws UnknownCommandException, UncheckedException, WrappedExpectableException {
        final String shortDescFinal;
        if (shortDesc.length() > 255) {
            shortDescFinal = shortDesc.substring(0, 254);
        } else {
            shortDescFinal = shortDesc;
        }

        ICommand updateCommand;
        if (toUpdate.isPresent()) {
            updateCommand = ComCreateUpdate.c(toUpdate.get());
        } else {
            final ClassId classId = getDescClassId();
            updateCommand = ComCreateNew.c(classId);
        }

        final IncNid incNid = (IncNid) getBase().sync(updateCommand);

        ComMutate cm = ComMutate.c(incNid,
                FieldSetter.c().add(FIELD_TITLE, FieldSetStr.c(shortDescFinal))
                        .add(FIELD_DESCRIBES, FieldSetLink.c(target)));
        getBase().sync(cm);

        return incNid;
    }

    public NidVer commit(IncNid descNode)
            throws UnknownCommandException, UncheckedException, WrappedExpectableException {
        ComCommit cc = ComCommit.c(descNode);
        final CommitResult commitResult = getBase().sync(cc);
        return commitResult.getNid(descNode);
    }

    /*public Set<Nid> getDescriptions(final NidVer forNode, boolean versioned)
            throws UnknownCommandException, UncheckedException, WrappedExpectableException {
        final ClassId classId = getDescClassId();
        final ComFetch<GetInClassEdgeResult> comFetch = ComFetch.c(forNode,
                GetInClassEdge.classEdge(classId, FIELD_TARGET, FIELD_TARGET_LI, versioned));
        final GetInClassEdgeResult foundNodes = getBase().sync(comFetch);

        Set<Nid> sources = new HashSet<Nid>();
        for (GetInClassEdgeResult.Entry entry : foundNodes.getEntries()) {
            sources.add(entry.getSource());
        }
        return sources;
    } */

    @Nullable
    public NidVer getDescription(final NidVer forNode)
            throws UnknownCommandException, UncheckedException, WrappedExpectableException {
        final InRealtionGetter<ArrayList<NidVer>> getter =
                InRealtionGetter.<ArrayList<NidVer>>select(InRelSelTarget.SINGLETON).limit(1)
                        .unversioned()
                        .where(InRelWhereClassId.withFieldIndex(getDescClassId(), FIELD_DESCRIBES))
                        .get();
        ComFetch<InRelationResult<ArrayList<NidVer>>> comFetch = ComFetch.c(forNode, getter);
        final InRelationResult<ArrayList<NidVer>> results = getBase().sync(comFetch);

        final InRealtionGetter<Integer> countGetter =
                InRealtionGetter.<Integer>select(InRelSelCount.SINGLETON).limit(1).unversioned()
                        .where(InRelWhereVersioned.SINGLETON).get();
        ComFetch<InRelationResult<Integer>> comFetch1 = ComFetch.c(forNode, countGetter);
        getBase().sync(comFetch1);

        if (results.getResult().isEmpty()) {
            return null;
        } else {
            return results.getResult().get(0);
        }
    }

    public String getBestDescription(final NidVer forNode, final String alternative)
            throws UnknownCommandException, UncheckedException, WrappedExpectableException {
        final NidVer nid = getDescription(forNode);
        if (nid == null) {
            System.out.println("No description found for Node: " + forNode);
            return alternative;
        }
        return getTitle(nid);
    }

    public String getTitle(NidVer nid)
            throws UnknownCommandException, UncheckedException, WrappedExpectableException {
        ComFetch<String> cf = ComFetch.c(nid, SingleGet.c(FIELD_TITLE, FieldGetStr.SINGLETON));
        return getBase().sync(cf);
    }

    public ClassId getDescClassId()
            throws UnknownCommandException, UncheckedException, WrappedExpectableException {
        if (this.fileClassId != null) {
            return this.fileClassId;
        }

        ClassDefinition classDefinition = getFileClassDef();

        /* Register class */
        final ComDeclareClass cdc = new ComDeclareClass(classDefinition);
        this.fileClassId = getBase().sync(cdc);
        return this.fileClassId;
    }
}
