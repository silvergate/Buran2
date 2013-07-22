package com.dcrux.buran.demoGui.plugins.listview.client;

import com.dcrux.buran.commandBase.VoidType;
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
import com.dcrux.buran.common.UserId;
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
import com.dcrux.buran.demoGui.infrastructure.client.Server;
import com.google.common.base.Optional;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.HashSet;
import java.util.Set;

/**
 * Buran.
 *
 * @author: ${USER} Date: 17.07.13 Time: 23:44
 */
public class DescModule {

    private UserId receiver;

    private ClassId fileClassId;

    public DescModule(UserId receiver) {
        this.receiver = receiver;
    }

    public static final FieldIndex FIELD_TARGET_CLASSID = FieldIndex.c(3);
    public static final FieldIndex FIELD_TITLE = FieldIndex.c(0);
    public static final FieldIndex FIELD_LONG_DESC = FieldIndex.c(1);
    public static final ClassLabelName FIELD_TARGET = ClassLabelName.c(0);
    public static final LabelIndex FIELD_TARGET_LI = LabelIndex.c(0);

    private ClassDefinition getFileClassDef() {
        ClassDefinition classDef = new ClassDefinition("A described node",
                "Eine node welche eine andere node beschreibt.");
        classDef.getFields().add(FIELD_TITLE, new StringType(0, 255), false)
                .add(FIELD_LONG_DESC, new StringType(0, StringType.MAXLEN_LIMIT), false)
                .add(FIELD_TARGET_CLASSID, IntegerType.cInt64Range(), false);
        return classDef;
    }

    public void describe(Optional<NidVer> toUpdate, final String shortDesc,
            final ClassId targetClassId, final IEdgeTargetInc target,
            final AsyncCallback<NidVer> callback) {
        final String shortDescFinal;
        if (shortDesc.length() > 255) {
            shortDescFinal = shortDesc.substring(0, 254);
        } else {
            shortDescFinal = shortDesc;
        }

        if (toUpdate.isPresent()) {
            ComCreateUpdate updateCommand;
            updateCommand = ComCreateUpdate.c(toUpdate.get());
            Server.get().run(receiver, updateCommand, new AsyncCallback<IncNid>() {
                @Override
                public void onFailure(Throwable caught) {
                    callback.onFailure(caught);
                }

                @Override
                public void onSuccess(final IncNid result) {
                    ComMutate cm = ComMutate.c(result,
                            BulkSet.c(SetEdge.c(FIELD_TARGET).add(FIELD_TARGET_LI, target))
                                    .add(FieldSetter.c(FIELD_TARGET_CLASSID,
                                            FieldSetInt.c(targetClassId.getId()))
                                            .add(FIELD_TITLE, FieldSetStr.c(shortDescFinal))));
                    Server.get().run(receiver, cm, new AsyncCallback<VoidType>() {
                        @Override
                        public void onFailure(Throwable caught) {
                            callback.onFailure(caught);
                        }

                        @Override
                        public void onSuccess(VoidType result2) {
                            ComCommit cc = ComCommit.c(result);
                            Server.get().run(receiver, cc, new AsyncCallback<CommitResult>() {
                                @Override
                                public void onFailure(Throwable caught) {
                                    callback.onFailure(caught);
                                }

                                @Override
                                public void onSuccess(CommitResult result3) {
                                    callback.onSuccess(result3.getNid(result));
                                }
                            });
                        }
                    });
                }
            });
        } else {
            getDescClassId(new AsyncCallback<ClassId>() {
                @Override
                public void onFailure(Throwable caught) {
                    callback.onFailure(caught);
                }

                @Override
                public void onSuccess(ClassId result) {
                    ComCreateNew updateCommand;
                    updateCommand = ComCreateNew.c(result);
                    Server.get().run(receiver, updateCommand, new AsyncCallback<IncNid>() {
                        @Override
                        public void onFailure(Throwable caught) {
                            callback.onFailure(caught);
                        }

                        @Override
                        public void onSuccess(final IncNid result) {
                            ComMutate cm = ComMutate.c(result,
                                    BulkSet.c(SetEdge.c(FIELD_TARGET).add(FIELD_TARGET_LI, target))
                                            .add(FieldSetter.c(FIELD_TARGET_CLASSID,
                                                    FieldSetInt.c(targetClassId.getId()))
                                                    .add(FIELD_TITLE,
                                                            FieldSetStr.c(shortDescFinal))));
                            Server.get().run(receiver, cm, new AsyncCallback<VoidType>() {
                                @Override
                                public void onFailure(Throwable caught) {
                                    callback.onFailure(caught);
                                }

                                @Override
                                public void onSuccess(VoidType result2) {
                                    ComCommit cc = ComCommit.c(result);
                                    Server.get()
                                            .run(receiver, cc, new AsyncCallback<CommitResult>() {
                                                @Override
                                                public void onFailure(Throwable caught) {
                                                    callback.onFailure(caught);
                                                }

                                                @Override
                                                public void onSuccess(CommitResult result3) {
                                                    callback.onSuccess(result3.getNid(result));
                                                }
                                            });
                                }
                            });
                        }
                    });
                }
            });
        }

    }

    public void getDescriptions(final NidVer forNode, final AsyncCallback<Set<Nid>> callback) {
        getDescClassId(new AsyncCallback<ClassId>() {
            @Override
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            @Override
            public void onSuccess(ClassId result) {
                final ComFetch comFetch = ComFetch.c(forNode,
                        GetInClassEdge.c(result, FIELD_TARGET, FIELD_TARGET_LI));
                Server.get().run(receiver, comFetch, new AsyncCallback<GetInClassEdgeResult>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        callback.onFailure(caught);
                    }

                    @Override
                    public void onSuccess(GetInClassEdgeResult result) {
                        Set<Nid> sources = new HashSet<Nid>();
                        for (GetInClassEdgeResult.Entry entry : result.getEntries()) {
                            sources.add(entry.getSource());
                        }
                        callback.onSuccess(sources);
                    }
                });
            }
        });
    }

    public void getAnyDescription(final NidVer forNode, final String alternative,
            final AsyncCallback<String> callback) {
        getDescriptions(forNode, new AsyncCallback<Set<Nid>>() {
            @Override
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            @Override
            public void onSuccess(Set<Nid> result) {
                if (result.isEmpty()) {
                    callback.onSuccess(alternative);
                }
                final Nid nid = result.iterator().next();
                getTitle(nid, callback);
            }
        });
    }

    public void getTitle(Nid nid, final AsyncCallback<String> callback) {
        ComFetch<String> cf = ComFetch.c(nid, SingleGet.c(FIELD_TITLE, FieldGetStr.SINGLETON));
        Server.get().run(receiver, cf, new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            @Override
            public void onSuccess(String result) {
                callback.onSuccess(result);
            }
        });
    }

    public void getDescClassId(final AsyncCallback<ClassId> callback) {
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
                        DescModule.this.fileClassId = result;
                        callback.onSuccess(result);
                    }
                });
    }
}
