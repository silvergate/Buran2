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
import com.dcrux.buran.commands.indexing.ComQuery;
import com.dcrux.buran.commands.indexing.QueryResult;
import com.dcrux.buran.commands.indexingNew.ComQueryNew;
import com.dcrux.buran.common.IncNid;
import com.dcrux.buran.common.NidVer;
import com.dcrux.buran.common.classDefinition.ClassDefinition;
import com.dcrux.buran.common.classDefinition.ClassIndexDefNew;
import com.dcrux.buran.common.classDefinition.ClassIndexId;
import com.dcrux.buran.common.classDefinition.ClassIndexName;
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
import com.dcrux.buran.common.indexing.keyGen.Tokens;
import com.dcrux.buran.common.indexing.mapFunction.MapFunction;
import com.dcrux.buran.common.indexing.mapFunction.TextFunction;
import com.dcrux.buran.common.indexing.mapInput.FieldTarget;
import com.dcrux.buran.common.indexing.mapInput.NodeMapInput;
import com.dcrux.buran.common.indexing.mapStore.MapIndex;
import com.dcrux.buran.common.link.LinkTargetInc;
import com.dcrux.buran.query.IndexFieldTarget;
import com.dcrux.buran.query.IndexedFieldDef;
import com.dcrux.buran.query.IndexedFieldId;
import com.dcrux.buran.query.SingleIndexDef;
import com.dcrux.buran.query.indexingDef.ClassIdIndexingDef;
import com.dcrux.buran.query.indexingDef.StrAnalyzedDef;
import com.dcrux.buran.query.queries.IQuery;
import com.dcrux.buran.query.queries.QueryTarget;
import com.dcrux.buran.query.queries.fielded.BoolQuery;
import com.dcrux.buran.query.queries.fielded.Query;
import com.dcrux.buran.query.queries.unfielded.ISimpleQuery;
import com.dcrux.buran.query.queries.unfielded.IsClass;
import com.dcrux.buran.scripting.functions.FunGet;
import com.dcrux.buran.scripting.functions.FunRet;
import com.dcrux.buran.scripting.functions.integer.FunIntLit;
import com.dcrux.buran.scripting.functions.integer.FunIntToBin;
import com.dcrux.buran.scripting.functions.list.FunListNew;
import com.dcrux.buran.scripting.iface.Code;
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

    public static final FieldIndex FIELD_TITLE = FieldIndex.c(0);
    public static final FieldIndex FIELD_LONG_DESC = FieldIndex.c(1);
    public static final FieldIndex FIELD_DESCRIBES = FieldIndex.c(2);

    public static final ClassIndexId INDEX_ONE = new ClassIndexId((short) 0);
    public static final IndexedFieldId INDEX_ONE_TITLE = new IndexedFieldId((short) 0);
    public static final IndexedFieldId INDEX_ONE_TARGET_CLASS = new IndexedFieldId((short) 1);

    public static final ClassIndexName INDEX_BY_TITLE = new ClassIndexName("byTitiel");

    public DescModule(BaseModule baseModule) {
        super(baseModule);
    }

    private ClassDefinition getFileClassDef() {
        ClassDefinition classDef = new ClassDefinition("A described node",
                "Eine node welche eine andere node beschreibt.");
        classDef.getFields().add(FIELD_TITLE, new StringType(0, 255), false)
                .add(FIELD_LONG_DESC, new StringType(0, StringType.MAXLEN_LIMIT), false)
                .add(FIELD_DESCRIBES, LinkType.c(), false);
        classDef.getIndexes().add(INDEX_BY_TITLE, getIndexByText());
        defineIndexes(classDef.getIndexesNew());
        return classDef;
    }

    private void defineIndexes(ClassIndexDefNew classIndexDefNew) {
        SingleIndexDef singleIndexDef = new SingleIndexDef();

        IndexedFieldDef indexedFieldDef = new IndexedFieldDef(IndexFieldTarget.index(FIELD_TITLE),
                new StrAnalyzedDef(false, 255, FieldGetStr.SINGLETON));
        IndexedFieldDef indexedFieldDefClassId =
                new IndexedFieldDef(IndexFieldTarget.index(FIELD_DESCRIBES),
                        new ClassIdIndexingDef(false));

        singleIndexDef.getFieldDef().put(INDEX_ONE_TITLE, indexedFieldDef);
        singleIndexDef.getFieldDef().put(INDEX_ONE_TARGET_CLASS, indexedFieldDefClassId);

        classIndexDefNew.getIndexes().put(INDEX_ONE, singleIndexDef);
    }

    private IndexDefinition getIndexByText() {
        final NodeMapInput mapInput = new NodeMapInput();

        TextFunction textFunction = TextFunction.c(FIELD_TITLE);
        MapFunction mapFunction = MapFunction.text(textFunction);
        MapIndex mapIndex = new MapIndex(true);

        final IndexDefinition indexDefinition =
                new IndexDefinition(mapInput, mapFunction, mapIndex);
        return indexDefinition;
    }

    private IndexDefinition getIndexByFileSize() {
        final VarName fsVarName = VarName.c("fileSize");
        final NodeMapInput mapInput = new NodeMapInput();
        mapInput.getFields().put(fsVarName, FieldTarget.cRequired(FIELD_TITLE));

        Code mapCode = new Code();
        mapCode.add(FunRet.c(FunListNew.c().add(FunIntToBin
                .c(FunGet.c(fsVarName, com.dcrux.buran.scripting.iface.types.IntegerType.class),
                        com.dcrux.buran.scripting.iface.types.IntegerType.NumOfBits.int64))
                .add(FunIntLit.c(0)).get()));

        MapFunction mapFunction = MapFunction.single(mapCode);
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
                FieldSetter.c().add(getDescClassId(), FIELD_TITLE, FieldSetStr.c(shortDescFinal))
                        .add(getDescClassId(), FIELD_DESCRIBES, FieldSetLink.c(target)));
        getBase().sync(cm);

        return incNid;
    }

    public NidVer commit(IncNid descNode)
            throws UnknownCommandException, UncheckedException, WrappedExpectableException {
        ComCommit cc = ComCommit.c(descNode);
        final CommitResult commitResult = getBase().sync(cc);
        return commitResult.getNid(descNode);
    }

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


    public void findByTitleNew(ISimpleQuery<? extends String, ? extends StrAnalyzedDef> title,
            Optional<ClassId> targetClassId)
            throws UnknownCommandException, UncheckedException, WrappedExpectableException {
        final QueryTarget target = QueryTarget.cDef(getDescClassId(), INDEX_ONE_TITLE);
        Query titleQuery = Query.c(target, title);

        final IQuery query;
        if (targetClassId.isPresent()) {
            final QueryTarget cidTarget =
                    QueryTarget.cDef(getDescClassId(), INDEX_ONE_TARGET_CLASS);
            Query cidQuery = Query.c(cidTarget, new IsClass(targetClassId.get()));
            query = BoolQuery.c().must(titleQuery).must(cidQuery);
        } else {
            query = titleQuery;
        }
        ComQueryNew cq = new ComQueryNew(query);
        final QueryResult result = getBase().sync(cq);
    }

    @Deprecated
    public void findByTitle(String query)
            throws UnknownCommandException, UncheckedException, WrappedExpectableException {
        ComQuery cq = ComQuery.c(getDescClassId(), INDEX_BY_TITLE, Tokens.c(query));
        final QueryResult result = getBase().sync(cq);
        for (NidVer nidVer : result.getResults()) {
            System.out.println("    -DESCRIPTION FOUND: " + nidVer);
        }
        if (result.getResults().isEmpty()) {
            System.out.println("    -DESCRIPTION FOUND: NONE");
        }
    }

    public String getTitle(NidVer nid)
            throws UnknownCommandException, UncheckedException, WrappedExpectableException {
        ComFetch<String> cf =
                ComFetch.c(nid, SingleGet.c(getDescClassId(), FIELD_TITLE, FieldGetStr.SINGLETON));
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
