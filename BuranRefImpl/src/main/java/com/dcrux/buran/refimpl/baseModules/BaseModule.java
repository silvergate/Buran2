package com.dcrux.buran.refimpl.baseModules;

import com.dcrux.buran.UserId;
import com.dcrux.buran.refimpl.baseModules.fields.GetterSetterSrv;
import com.dcrux.buran.refimpl.baseModules.label.LabelSrv;
import com.dcrux.buran.refimpl.dao.Classes;
import com.dcrux.buran.refimpl.dao.DataSrv;
import com.dcrux.buran.refimpl.dao.DbUtils;
import com.dcrux.buran.refimpl.dao.IncubationSrv;
import com.dcrux.buran.refimpl.service.CurrentTimestampProvider;
import com.orientechnologies.orient.core.db.document.ODatabaseDocument;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentPool;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;

import java.io.File;
import java.io.IOException;

/**
 * Buran.
 *
 * @author: ${USER} Date: 29.06.13 Time: 13:45
 */
public class BaseModule {

    private final CurrentTimestampProvider currentTimestampProvider =
            new CurrentTimestampProvider();
    private final IncubationSrv incubationSrv = new IncubationSrv(this);
    private final DbUtils dbUtils = new DbUtils(this);
    private final Classes classes = new Classes(this);
    private final GetterSetterSrv getterSetterSrv = new GetterSetterSrv(this);
    private final DataSrv dataSrv = new DataSrv(this);
    private final LabelSrv labelSrv = new LabelSrv(this);

    ODatabaseDocumentTx db;

    public static File getDbPath(UserId userId) {
        return new File("/Users/caelis/" + userId.getId() + ".orientdb");
    }

    public static String getDbString(File path) throws IOException {
        return "local:" + path.getCanonicalPath();
    }

    private void setup() {
        this.labelSrv.setupDb();
    }

    public ODatabaseDocument getDb() {
        return this.db;
    }

    public static void createNew(UserId userId, boolean removeIfExisting) throws IOException {
        final File path = getDbPath(userId);
        if (path.exists() && removeIfExisting) {
            for (final File file : path.listFiles()) {
                file.delete();
            }
            path.delete();
        }
        ODatabaseDocumentTx db = new ODatabaseDocumentTx(getDbString(path)).create();
        db.close();
    }

    public BaseModule(UserId userId) throws IOException {
        final File path = getDbPath(userId);
        this.db = ODatabaseDocumentPool.global().acquire(getDbString(path), "admin", "admin");
        setup();
    }

    public void close() {
        this.db.close();
    }

    public CurrentTimestampProvider getCurrentTimestampProvider() {
        return currentTimestampProvider;
    }

    public IncubationSrv getIncubationSrv() {
        return incubationSrv;
    }

    public DbUtils getDbUtils() {
        return dbUtils;
    }

    public Classes getClasses() {
        return classes;
    }

    public GetterSetterSrv getGetterSetterSrv() {
        return getterSetterSrv;
    }

    public DataSrv getDataSrv() {
        return dataSrv;
    }

    public LabelSrv getLabelSrv() {
        return labelSrv;
    }
}
