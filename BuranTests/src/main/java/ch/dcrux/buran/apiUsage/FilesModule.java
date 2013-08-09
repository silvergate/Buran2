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
import com.dcrux.buran.common.fields.getter.SingleGet;
import com.dcrux.buran.common.fields.setter.FieldAppendBin;
import com.dcrux.buran.common.fields.setter.FieldSetStr;
import com.dcrux.buran.common.fields.setter.FieldSetter;
import com.dcrux.buran.common.fields.types.BinaryType;
import com.dcrux.buran.common.fields.types.StringType;

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

    public static final ClassIndexName INDEX_BY_FILESIZE = new ClassIndexName("byFs");
    public static final ClassIndexName INDEX_BY_MIME_FILESIZE = new ClassIndexName("byMFs");

    public FilesModule(BaseModule baseModule) {
        super(baseModule);
        this.descModule = new DescModule(baseModule);
    }


    private ClassDefinition getFileClassDef() {
        ClassDefinition classDef = new ClassDefinition("A Node containing a file",
                "A note containing a simple file. Hier noch mehr informationen, ganz viel.");
        classDef.getFields().add(FIELD_MIME, new StringType(0, 256), false)
                .add(FIELD_DATA, BinaryType.c(BinaryType.MAXLEN_LIMIT), false);
        return classDef;
    }

    public IncNid createFile(final String mimeType)
            throws UnknownCommandException, UncheckedException, WrappedExpectableException {
        ClassId classId = getFileClassId();
        final ComCreateNew newFile = ComCreateNew.c(classId);
        IncNid incNid = getBase().sync(newFile);
        ComMutate comMutate = ComMutate
                .c(incNid, FieldSetter.c(getFileClassId(), FIELD_MIME, FieldSetStr.c(mimeType)));
        getBase().sync(comMutate);
        return incNid;
    }

    public CommitResult commitWithDesc(final IncNid incNid, final String filename)
            throws UnknownCommandException, UncheckedException, WrappedExpectableException {
        descModule.describe(incNid, filename, null);

        ComCommit comCommit = ComCommit.c(incNid);
        return getBase().sync(comCommit);
    }

    public void append(IncNid incNid, byte[] data)
            throws UnknownCommandException, UncheckedException, WrappedExpectableException {
        ComMutate comMutate = ComMutate
                .c(incNid, FieldSetter.c(getFileClassId(), FIELD_DATA, FieldAppendBin.c(data)));
        getBase().sync(comMutate);
    }

    public long getFileSize(NidVer nidVer)
            throws UnknownCommandException, UncheckedException, WrappedExpectableException {
        ComFetch<Long> comFetch = ComFetch.c(nidVer,
                SingleGet.c(getFileClassId(), FIELD_DATA, FieldGetBinLen.SINGLETON));
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
