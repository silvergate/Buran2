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
import com.dcrux.buran.common.Nid;
import com.dcrux.buran.common.NidVer;
import com.dcrux.buran.common.classDefinition.ClassDefinition;
import com.dcrux.buran.common.classes.ClassId;
import com.dcrux.buran.common.edges.ClassLabelName;
import com.dcrux.buran.common.edges.IEdgeTargetInc;
import com.dcrux.buran.common.edges.LabelIndex;
import com.dcrux.buran.common.edges.getter.GetInClassEdge;
import com.dcrux.buran.common.edges.getter.GetInClassEdgeResult;
import com.dcrux.buran.common.edges.setter.SetEdge;
import com.dcrux.buran.common.fields.FieldIndex;
import com.dcrux.buran.common.fields.getter.FieldGetStr;
import com.dcrux.buran.common.fields.getter.SingleGet;
import com.dcrux.buran.common.fields.setter.FieldSetInt;
import com.dcrux.buran.common.fields.setter.FieldSetStr;
import com.dcrux.buran.common.fields.setter.FieldSetter;
import com.dcrux.buran.common.fields.types.IntegerType;
import com.dcrux.buran.common.fields.types.StringType;
import com.dcrux.buran.common.getterSetter.BulkSet;
import com.google.common.base.Optional;

import java.util.HashSet;
import java.util.Set;

/**
 * Buran.
 *
 * @author: ${USER} Date: 17.07.13 Time: 23:44
 */
public class DescModule extends Module<BaseModule> {

    private ClassId fileClassId;

    public static final FieldIndex FIELD_TARGET_CLASSID = FieldIndex.c(3);
    public static final FieldIndex FIELD_TITLE = FieldIndex.c(0);
    public static final FieldIndex FIELD_LONG_DESC = FieldIndex.c(1);
    public static final ClassLabelName FIELD_TARGET = ClassLabelName.c(0);
    public static final LabelIndex FIELD_TARGET_LI = LabelIndex.c(0);

    public DescModule(BaseModule baseModule) {
        super(baseModule);
    }

    private ClassDefinition getFileClassDef() {
        ClassDefinition classDef = new ClassDefinition("A described node",
                "Eine node welche eine andere node beschreibt.");
        classDef.getFields().add(FIELD_TITLE, new StringType(0, 255), false)
                .add(FIELD_LONG_DESC, new StringType(0, StringType.MAXLEN_LIMIT), false)
                .add(FIELD_TARGET_CLASSID, IntegerType.cInt64Range(), false);
        return classDef;
    }

    public IncNid describe(Optional<NidVer> toUpdate, final String shortDesc,
            final ClassId targetClassId, final IEdgeTargetInc target)
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
                BulkSet.c(SetEdge.c(FIELD_TARGET).add(FIELD_TARGET_LI, target)).add(FieldSetter
                        .c(FIELD_TARGET_CLASSID, FieldSetInt.c(targetClassId.getId()))
                        .add(FIELD_TITLE, FieldSetStr.c(shortDescFinal))));
        getBase().sync(cm);

        return incNid;
    }

    public NidVer commit(IncNid descNode)
            throws UnknownCommandException, UncheckedException, WrappedExpectableException {
        ComCommit cc = ComCommit.c(descNode);
        final CommitResult commitResult = getBase().sync(cc);
        return commitResult.getNid(descNode);
    }

    public Set<Nid> getDescriptions(final NidVer forNode, boolean versioned)
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
    }

    public String getBestDescription(final NidVer forNode, final String alternative)
            throws UnknownCommandException, UncheckedException, WrappedExpectableException {
        final Set<Nid> verResults = getDescriptions(forNode, true);
        if (verResults.isEmpty()) {
            /* Unversioned results */
            final Set<Nid> unVerResults = getDescriptions(forNode, false);
            if (!unVerResults.isEmpty()) {
                return getTitle(unVerResults.iterator().next());

            } else {
                return alternative;
            }

        } else {
            return getTitle(verResults.iterator().next());
        }
    }

    public String getTitle(Nid nid)
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
