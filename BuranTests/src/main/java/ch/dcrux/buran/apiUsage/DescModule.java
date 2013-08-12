package ch.dcrux.buran.apiUsage;

import com.dcrux.buran.commandBase.UncheckedException;
import com.dcrux.buran.commandBase.UnknownCommandException;
import com.dcrux.buran.commandBase.WrappedExpectableException;
import com.dcrux.buran.commands.classes.ComDeclareClass;
import com.dcrux.buran.commands.dataFetch.ComFetch;
import com.dcrux.buran.commands.dataMut.ComMutate;
import com.dcrux.buran.commands.incubation.ComCommit;
import com.dcrux.buran.commands.incubation.CommitResult;
import com.dcrux.buran.commands.indexing.QueryResult;
import com.dcrux.buran.commands.indexingNew.ComQueryNew;
import com.dcrux.buran.common.IncNid;
import com.dcrux.buran.common.NidVer;
import com.dcrux.buran.common.classDefinition.ClassDefinition;
import com.dcrux.buran.common.classDefinition.ClassIndexDefNew;
import com.dcrux.buran.common.classDefinition.ClassIndexId;
import com.dcrux.buran.common.classes.ClassId;
import com.dcrux.buran.common.fields.FieldIndex;
import com.dcrux.buran.common.fields.NodeFieldTarget;
import com.dcrux.buran.common.fields.getter.FieldGetStr;
import com.dcrux.buran.common.fields.getter.SingleGet;
import com.dcrux.buran.common.fields.setter.FieldSetStr;
import com.dcrux.buran.common.fields.setter.FieldSetter;
import com.dcrux.buran.common.fields.types.StringType;
import com.dcrux.buran.common.nodes.setter.ClassIdMut;
import com.dcrux.buran.query.IndexFieldTarget;
import com.dcrux.buran.query.IndexedFieldDef;
import com.dcrux.buran.query.IndexedFieldId;
import com.dcrux.buran.query.SingleIndexDef;
import com.dcrux.buran.query.indexingDef.ClassIdsIndexingDef;
import com.dcrux.buran.query.indexingDef.StrAnalyzedDef;
import com.dcrux.buran.query.queries.IQuery;
import com.dcrux.buran.query.queries.QueryTarget;
import com.dcrux.buran.query.queries.fielded.BoolQuery;
import com.dcrux.buran.query.queries.fielded.Query;
import com.dcrux.buran.query.queries.unfielded.HasClass;
import com.dcrux.buran.query.queries.unfielded.ISimpleQuery;
import com.google.common.base.Optional;
import com.sun.istack.internal.Nullable;

/**
 * Buran.
 *
 * @author: ${USER} Date: 17.07.13 Time: 23:44
 */
public class DescModule extends Module<BaseModule> {

    private ClassId fileClassId;

    public static final FieldIndex FIELD_TITLE = FieldIndex.c(0);
    public static final FieldIndex FIELD_LONG_DESC = FieldIndex.c(1);
    public static final FieldIndex FIELD_INDEX_ONLY = FieldIndex.c(2);

    public static final ClassIndexId INDEX_ONE = new ClassIndexId((short) 0);
    public static final IndexedFieldId INDEX_ONE_TITLE = new IndexedFieldId((short) 0);
    public static final IndexedFieldId INDEX_ONE_CLASSES = new IndexedFieldId((short) 1);
    public static final IndexedFieldId INDEX_ONE_INDEX_ONLY = new IndexedFieldId((short) 3);

    public DescModule(BaseModule baseModule) {
        super(baseModule);
    }

    private ClassDefinition getFileClassDef() {
        ClassDefinition classDef = new ClassDefinition("A described node",
                "Eine node welche eine andere node beschreibt.");
        classDef.getFields().add(FIELD_TITLE, new StringType(0, 255), false)
                .add(FIELD_LONG_DESC, new StringType(0, StringType.MAXLEN_LIMIT), false)
                .add(FIELD_INDEX_ONLY, new StringType(0, StringType.MAXLEN_LIMIT), false);
        defineIndexes(classDef.getIndexesNew());
        return classDef;
    }

    private void defineIndexes(ClassIndexDefNew classIndexDefNew) {
        SingleIndexDef singleIndexDef = new SingleIndexDef();

        IndexedFieldDef indexedFieldDef = new IndexedFieldDef(IndexFieldTarget.index(FIELD_TITLE),
                new StrAnalyzedDef(false, 255, FieldGetStr.SINGLETON));
        IndexedFieldDef indexedFieldOnly =
                new IndexedFieldDef(IndexFieldTarget.index(FIELD_INDEX_ONLY),
                        new StrAnalyzedDef(false, StringType.MAXLEN_LIMIT, FieldGetStr.SINGLETON));
        IndexedFieldDef indexedFieldDefClassId =
                new IndexedFieldDef(IndexFieldTarget.node(NodeFieldTarget.classes),
                        ClassIdsIndexingDef.c());

        singleIndexDef.getFieldDef().put(INDEX_ONE_TITLE, indexedFieldDef);
        singleIndexDef.getFieldDef().put(INDEX_ONE_CLASSES, indexedFieldDefClassId);
        singleIndexDef.getFieldDef().put(INDEX_ONE_INDEX_ONLY, indexedFieldOnly);

        classIndexDefNew.getIndexes().put(INDEX_ONE, singleIndexDef);
    }

    public void describe(IncNid incNid, @Nullable String title, @Nullable String longDesc)
            throws UnknownCommandException, UncheckedException, WrappedExpectableException {
        ClassIdMut addClass = ClassIdMut.add(getDescClassId());

        ComMutate cm = ComMutate.c(incNid, addClass);
        getBase().sync(cm);

        /* Set description */
        chageDescription(incNid, title, longDesc);
    }

    public void chageDescription(IncNid incNid, @Nullable String title, @Nullable String longDesc)
            throws UnknownCommandException, UncheckedException, WrappedExpectableException {
        final FieldSetter fieldSetter = FieldSetter.c();
        if (title != null) {
            fieldSetter.add(getDescClassId(), FIELD_TITLE, FieldSetStr.c(title));
        }
        if (longDesc != null) {
            fieldSetter.add(getDescClassId(), FIELD_LONG_DESC, FieldSetStr.c(longDesc));
        }

        ComMutate cm = ComMutate.c(incNid, fieldSetter);
        getBase().sync(cm);
    }

    public NidVer commit(IncNid descNode)
            throws UnknownCommandException, UncheckedException, WrappedExpectableException {
        ComCommit cc = ComCommit.c(descNode);
        final CommitResult commitResult = getBase().sync(cc);
        return commitResult.getNid(descNode);
    }

    public void findByTitleNew(ISimpleQuery<? extends String, ? extends StrAnalyzedDef> title,
            Optional<ClassId> targetClassId)
            throws UnknownCommandException, UncheckedException, WrappedExpectableException {
        final QueryTarget target = QueryTarget.cDef(getDescClassId(), INDEX_ONE_TITLE);
        Query titleQuery = Query.c(target, title);

        final IQuery query;
        if (targetClassId.isPresent()) {
            final QueryTarget cidTarget = QueryTarget.cDef(getDescClassId(), INDEX_ONE_CLASSES);
            Query cidQuery = Query.c(cidTarget, HasClass.contains(targetClassId.get()));
            query = BoolQuery.c().must(titleQuery).must(cidQuery);
        } else {
            query = titleQuery;
        }
        ComQueryNew cq = new ComQueryNew(query);
        final QueryResult result = getBase().sync(cq);
    }

    @Nullable
    public String getTitle(NidVer nid)
            throws UnknownCommandException, UncheckedException, WrappedExpectableException {
        ComFetch<String> cf =
                ComFetch.c(nid, SingleGet.c(getDescClassId(), FIELD_TITLE, FieldGetStr.SINGLETON));
        return getBase().sync(cf);
    }

    @Nullable
    public String getDescription(NidVer nid)
            throws UnknownCommandException, UncheckedException, WrappedExpectableException {
        ComFetch<String> cf = ComFetch.c(nid,
                SingleGet.c(getDescClassId(), FIELD_LONG_DESC, FieldGetStr.SINGLETON));
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
