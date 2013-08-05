package ch.dcrux.buran.apiUsage;

import com.dcrux.buran.commandBase.UncheckedException;
import com.dcrux.buran.commandBase.UnknownCommandException;
import com.dcrux.buran.commandBase.WrappedExpectableException;
import com.dcrux.buran.commands.classes.ComDeclareClass;
import com.dcrux.buran.commands.dataFetch.ComFetch;
import com.dcrux.buran.commands.dataMut.ComMutate;
import com.dcrux.buran.commands.incubation.ComCommit;
import com.dcrux.buran.commands.incubation.ComCreateNew;
import com.dcrux.buran.commands.incubation.CommitResult;
import com.dcrux.buran.common.IncNid;
import com.dcrux.buran.common.NidVer;
import com.dcrux.buran.common.classDefinition.ClassDefinition;
import com.dcrux.buran.common.classDefinition.ClassIndexName;
import com.dcrux.buran.common.classes.ClassId;
import com.dcrux.buran.common.fields.FieldIndex;
import com.dcrux.buran.common.fields.getter.FieldGetBinLen;
import com.dcrux.buran.common.fields.getter.FieldGetStrLimit;
import com.dcrux.buran.common.fields.getter.SingleGet;
import com.dcrux.buran.common.fields.setter.FieldAppendBin;
import com.dcrux.buran.common.fields.setter.FieldSetLink;
import com.dcrux.buran.common.fields.setter.FieldSetStr;
import com.dcrux.buran.common.fields.setter.FieldSetter;
import com.dcrux.buran.common.fields.types.BinaryType;
import com.dcrux.buran.common.fields.types.LinkType;
import com.dcrux.buran.common.fields.types.StringType;
import com.dcrux.buran.common.indexing.IndexDefinition;
import com.dcrux.buran.common.indexing.mapFunction.MapFunction;
import com.dcrux.buran.common.indexing.mapInput.FieldTarget;
import com.dcrux.buran.common.indexing.mapInput.NodeMapInput;
import com.dcrux.buran.common.indexing.mapStore.MapIndex;
import com.dcrux.buran.common.link.LinkTargetInc;
import com.dcrux.buran.scripting.functions.FunGet;
import com.dcrux.buran.scripting.functions.FunRet;
import com.dcrux.buran.scripting.functions.bin.FunBinConcat;
import com.dcrux.buran.scripting.functions.integer.FunIntLit;
import com.dcrux.buran.scripting.functions.integer.FunIntToBin;
import com.dcrux.buran.scripting.functions.list.FunListNew;
import com.dcrux.buran.scripting.functions.string.FunStrHash;
import com.dcrux.buran.scripting.iface.Block;
import com.dcrux.buran.scripting.iface.VarName;
import com.dcrux.buran.scripting.iface.types.IntegerType;
import com.google.common.base.Optional;

/**
 * Buran.
 *
 * @author: ${USER} Date: 23.07.13 Time: 21:43
 */
public class FilesModule extends Module<BaseModule> {

    private ClassId fileClassId;

    private DescModule descModule;

    public static final FieldIndex FIELD_MIME = FieldIndex.c(0);
    public static final FieldIndex FIELD_DATA = FieldIndex.c(1);
    public static final FieldIndex FIELD_DESCRIPTION = FieldIndex.c(2);
    // public static final ClassLabelName LABEL_DESC = ClassLabelName.c(0);

    public static final ClassIndexName INDEX_BY_FILESIZE = new ClassIndexName("byFs");
    public static final ClassIndexName INDEX_BY_MIME_FILESIZE = new ClassIndexName("byMFs");

    public FilesModule(BaseModule baseModule) {
        super(baseModule);
        this.descModule = new DescModule(baseModule);
    }

    private IndexDefinition getIndexByFileSize() {
        final VarName fsVarName = VarName.c("fileSize");
        final NodeMapInput mapInput = new NodeMapInput();
        mapInput.getFields().put(fsVarName, FieldTarget.cRequired(FIELD_DATA));

        Block mapBlock = new Block();
        mapBlock.add(FunRet.c(FunListNew.c().add(FunIntToBin
                .c(FunGet.c(fsVarName, IntegerType.class),
                        com.dcrux.buran.scripting.iface.types.IntegerType.NumOfBits.int64))
                .add(FunIntLit.c(0)).get()));

        MapFunction mapFunction = MapFunction.single(mapBlock);
        MapIndex mapIndex = new MapIndex(true);

        final IndexDefinition indexDefinition =
                new IndexDefinition(mapInput, mapFunction, mapIndex);
        return indexDefinition;
    }

    private IndexDefinition getIndexByMimeAndFileSize() {
        final VarName fsVarName = VarName.c("fileSize");
        final VarName mimeTypeVarName = VarName.c("mimeType");

        final NodeMapInput mapInput = new NodeMapInput();
        mapInput.getFields().put(mimeTypeVarName, FieldTarget.cRequired(FIELD_MIME));
        mapInput.getFields()
                .put(fsVarName, FieldTarget.cRequired(FIELD_DATA, FieldGetStrLimit.limit(200)));

        Block mapBlock = new Block();

        FunStrHash funStrHash = FunStrHash
                .c(FunGet.c(mimeTypeVarName, com.dcrux.buran.scripting.iface.types.StringType
                        .class), FunStrHash.Length.bits64);
        FunIntToBin funIntToBin = FunIntToBin.c(FunGet.c(fsVarName, IntegerType.class),
                com.dcrux.buran.scripting.iface.types.IntegerType.NumOfBits.int64);

        mapBlock.add(FunRet.c(
                FunListNew.c().add(FunBinConcat.c(funStrHash, funIntToBin)).add(FunIntLit.c(0))
                        .get()));

        MapFunction mapFunction = MapFunction.single(mapBlock);
        MapIndex mapIndex = new MapIndex(true);

        final IndexDefinition indexDefinition =
                new IndexDefinition(mapInput, mapFunction, mapIndex);
        return indexDefinition;
    }

    private ClassDefinition getFileClassDef() {
        ClassDefinition classDef = new ClassDefinition("A Node containing a file",
                "A note containing a simple file. Hier noch mehr informationen, ganz viel.");
        classDef.getFields().add(FIELD_MIME, new StringType(0, 256), false)
                .add(FIELD_DATA, BinaryType.c(BinaryType.MAXLEN_LIMIT), false)
                .add(FIELD_DESCRIPTION, LinkType.c(), false);

        /* File-size index */
        classDef.getIndexes().add(INDEX_BY_FILESIZE, getIndexByFileSize());

        /* Mime and fileSize */
        classDef.getIndexes().add(INDEX_BY_MIME_FILESIZE, getIndexByMimeAndFileSize());

        return classDef;
    }

    public IncNid createFile(final String mimeType)
            throws UnknownCommandException, UncheckedException, WrappedExpectableException {
        ClassId classId = getFileClassId();
        final ComCreateNew newFile = ComCreateNew.c(classId);
        IncNid incNid = getBase().sync(newFile);
        ComMutate comMutate =
                ComMutate.c(incNid, FieldSetter.c(FIELD_MIME, FieldSetStr.c(mimeType)));
        getBase().sync(comMutate);
        return incNid;
    }

    public CommitResult commitWithDesc(final IncNid incNid, final String filename)
            throws UnknownCommandException, UncheckedException, WrappedExpectableException {
        IncNid descNode =
                descModule.describe(Optional.<NidVer>absent(), filename, LinkTargetInc.inc(incNid));

        ComMutate comMutate = ComMutate.c(incNid,
                FieldSetter.c(FIELD_DESCRIPTION, FieldSetLink.c(LinkTargetInc.inc(descNode))));
        getBase().sync(comMutate);

        ComCommit comCommit = ComCommit.c(incNid, descNode);
        return getBase().sync(comCommit);
    }

    public void append(IncNid incNid, byte[] data)
            throws UnknownCommandException, UncheckedException, WrappedExpectableException {
        ComMutate comMutate =
                ComMutate.c(incNid, FieldSetter.c(FIELD_DATA, FieldAppendBin.c(data)));
        getBase().sync(comMutate);
    }

    public long getFileSize(NidVer nidVer)
            throws UnknownCommandException, UncheckedException, WrappedExpectableException {
        ComFetch<Long> comFetch =
                ComFetch.c(nidVer, SingleGet.c(FIELD_DATA, FieldGetBinLen.SINGLETON));
        return getBase().sync(comFetch);
    }

    public ClassId getFileClassId()
            throws UnknownCommandException, UncheckedException, WrappedExpectableException {
        if (this.fileClassId != null) {
            return this.fileClassId;
        }

        ClassDefinition classDefinition = getFileClassDef();
        final ComDeclareClass comDeclareClass = new ComDeclareClass(classDefinition);

        /* Register class */
        this.fileClassId = getBase().sync(comDeclareClass);
        return this.fileClassId;
    }
}
