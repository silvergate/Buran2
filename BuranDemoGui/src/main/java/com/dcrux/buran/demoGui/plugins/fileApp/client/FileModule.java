package com.dcrux.buran.demoGui.plugins.fileApp.client;

import com.dcrux.buran.common.UserId;
import com.dcrux.buran.common.classDefinition.ClassDefinition;
import com.dcrux.buran.common.classes.ClassId;
import com.dcrux.buran.common.fields.FieldIndex;
import com.dcrux.buran.common.indexing.IndexDefinition;
import com.dcrux.buran.common.indexing.mapFunction.MapFunction;
import com.dcrux.buran.demoGui.infrastructure.client.Server;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Buran.
 *
 * @author: ${USER} Date: 17.07.13 Time: 23:44
 */
public class FileModule {

    private UserId receiver;

    private ClassId fileClassId;

    public FileModule(UserId receiver) {
        this.receiver = receiver;
    }

    public static final FieldIndex FIELD_MIME = FieldIndex.c(0);
    public static final FieldIndex FIELD_DATA = FieldIndex.c(1);

    private ClassDefinition getFileClassDef() {
        ClassDefinition classDef = new ClassDefinition("A Node containing a file",
                "A note containing a simple file. Hier noch mehr informationen, ganz viel.");
        /*classDef.getFields().add(FIELD_MIME, new StringType(1, 64), true)
                .add(FIELD_DATA, BinaryType.c(BinaryType.MAXLEN_LIMIT), true);*/
        return classDef;
    }

    public void getFileClassId(final AsyncCallback<ClassId> callback) {
        if (this.fileClassId != null) {
            callback.onSuccess(this.fileClassId);
        }

        ClassDefinition classDefinition = getFileClassDef();
        System.out.println(classDefinition);

        MapFunction cfd = MapFunction.single(null);
        IndexDefinition cid = new IndexDefinition(null, null, null);
        Server.get().serTest(cfd, new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void onSuccess(Void result) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });
        Server.get().serTest(cid, new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void onSuccess(Void result) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });


        /* Register class */
        /*Server.get().run(this.receiver, new ComDeclareClass(classDefinition),
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
        });  */
    }
}
