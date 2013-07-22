package com.dcrux.buran.demoGui.plugins.fileApp.client;

import com.dcrux.buran.commandBase.VoidType;
import com.dcrux.buran.commands.classes.ComDeclareClass;
import com.dcrux.buran.commands.dataMut.ComMutate;
import com.dcrux.buran.commands.incubation.ComCommit;
import com.dcrux.buran.commands.incubation.ComCreateNew;
import com.dcrux.buran.commands.incubation.CommitResult;
import com.dcrux.buran.common.IncNid;
import com.dcrux.buran.common.NidVer;
import com.dcrux.buran.common.UserId;
import com.dcrux.buran.common.classDefinition.ClassDefinition;
import com.dcrux.buran.common.classes.ClassId;
import com.dcrux.buran.common.edges.targets.EdgeTarget;
import com.dcrux.buran.common.fields.FieldIndex;
import com.dcrux.buran.common.fields.setter.FieldAppendBin;
import com.dcrux.buran.common.fields.setter.FieldSetStr;
import com.dcrux.buran.common.fields.setter.FieldSetter;
import com.dcrux.buran.common.fields.types.BinaryType;
import com.dcrux.buran.common.fields.types.StringType;
import com.dcrux.buran.demoGui.infrastructure.client.Server;
import com.dcrux.buran.demoGui.plugins.listview.client.DescModule;
import com.google.common.base.Optional;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Buran.
 *
 * @author: ${USER} Date: 17.07.13 Time: 23:44
 */
public class FileModule {

    private UserId receiver;

    private ClassId fileClassId;

    private DescModule descModule;

    public FileModule(UserId receiver) {
        this.receiver = receiver;
        this.descModule = new DescModule(this.receiver);
    }

    public static final FieldIndex FIELD_MIME = FieldIndex.c(0);
    public static final FieldIndex FIELD_DATA = FieldIndex.c(1);

    private ClassDefinition getFileClassDef() {
        ClassDefinition classDef = new ClassDefinition("A Node containing a file",
                "A note containing a simple file. Hier noch mehr informationen, ganz viel.");
        classDef.getFields().add(FIELD_MIME, new StringType(0, 256), false)
                .add(FIELD_DATA, BinaryType.c(BinaryType.MAXLEN_LIMIT), false);
        return classDef;
    }

    public void createFile(final String mimeType, final AsyncCallback<IncNid> callback) {
        getFileClassId(new AsyncCallback<ClassId>() {
            @Override
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            @Override
            public void onSuccess(ClassId result) {
                final ComCreateNew newFile = ComCreateNew.c(result);
                Server.get().run(receiver, newFile, new AsyncCallback<IncNid>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        callback.onFailure(caught);
                    }

                    @Override
                    public void onSuccess(final IncNid result) {
                        ComMutate comMutate = ComMutate
                                .c(result, FieldSetter.c(FIELD_MIME, FieldSetStr.c(mimeType)));
                        Server.get().run(receiver, comMutate, new AsyncCallback<VoidType>() {
                            @Override
                            public void onFailure(Throwable caught) {
                                callback.onFailure(caught);
                            }

                            @Override
                            public void onSuccess(VoidType result2) {
                                callback.onSuccess(result);
                            }
                        });
                    }
                });
            }
        });
    }

    public void commit(final IncNid incNid, final String filename,
            final AsyncCallback<CommitResult> callback) {
        ComCommit comCommit = ComCommit.c(incNid);
        Server.get().run(receiver, comCommit, new AsyncCallback<CommitResult>() {
            @Override
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            @Override
            public void onSuccess(final CommitResult result) {
                final NidVer nidVer = result.getNid(incNid);
                descModule.describe(Optional.<NidVer>absent(), filename, ClassId.c(0),
                        EdgeTarget.unversioned(nidVer), new AsyncCallback<NidVer>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        callback.onFailure(caught);
                    }

                    @Override
                    public void onSuccess(NidVer resultZ) {
                        callback.onSuccess(result);
                    }
                });
            }
        });
    }

    public void append(IncNid incNid, byte[] data, AsyncCallback<VoidType> callback) {
        ComMutate comMutate =
                ComMutate.c(incNid, FieldSetter.c(FIELD_DATA, FieldAppendBin.c(data)));
        Server.get().run(receiver, comMutate, callback);
    }

    public void getFileClassId(final AsyncCallback<ClassId> callback) {
        if (this.fileClassId != null) {
            callback.onSuccess(this.fileClassId);
        }

        ClassDefinition classDefinition = getFileClassDef();

        /* Register class */
        Server.get().run(this.receiver, new ComDeclareClass(classDefinition),
                new AsyncCallback<ClassId>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        callback.onFailure(caught);
                    }

                    @Override
                    public void onSuccess(ClassId result) {
                        FileModule.this.fileClassId = result;
                        callback.onSuccess(result);
                    }
                });
    }
}
