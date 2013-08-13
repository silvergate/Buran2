package com.dcrux.buran.refimpl.baseModules;

import com.dcrux.buran.callbacksBase.ICallbackCommand;
import com.dcrux.buran.common.UserId;
import com.dcrux.buran.refimpl.baseModules.auth.AuthModule;
import com.dcrux.buran.refimpl.baseModules.classes.ClassesModule;
import com.dcrux.buran.refimpl.baseModules.commit.CommitModule;
import com.dcrux.buran.refimpl.baseModules.dataFetch.DataFetchModule;
import com.dcrux.buran.refimpl.baseModules.dataMut.DataMutModule;
import com.dcrux.buran.refimpl.baseModules.deltaRecorder.DeltaRecorderModule;
import com.dcrux.buran.refimpl.baseModules.elasticSearch.EsModule;
import com.dcrux.buran.refimpl.baseModules.fields.FieldsModule;
import com.dcrux.buran.refimpl.baseModules.incubation.IncubationModule;
import com.dcrux.buran.refimpl.baseModules.newIndexing.IndexingModuleNew;
import com.dcrux.buran.refimpl.baseModules.newIndexing.SearchModule;
import com.dcrux.buran.refimpl.baseModules.newRelations.NewRelationsModule;
import com.dcrux.buran.refimpl.baseModules.nodes.NodesModule;
import com.dcrux.buran.refimpl.baseModules.notifications.NotificationsModule;
import com.dcrux.buran.refimpl.baseModules.orientUtils.DbUtils;
import com.dcrux.buran.refimpl.baseModules.subscription.SubscriptionModule;
import com.dcrux.buran.refimpl.baseModules.text.TextModule;
import com.dcrux.buran.refimpl.baseModules.time.CurrentTimestampProvider;
import com.dcrux.buran.refimpl.baseModules.versions.VersionsModule;
import com.orientechnologies.orient.core.db.document.ODatabaseDocument;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentPool;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;

import java.io.File;
import java.io.IOException;
import java.util.Random;

/**
 * Buran.
 *
 * @author: ${USER} Date: 29.06.13 Time: 13:45
 */
public class BaseModule {

    private final CurrentTimestampProvider currentTimestampProvider =
            new CurrentTimestampProvider();
    private final IncubationModule incubationModule = new IncubationModule(this);
    private final DbUtils dbUtils = new DbUtils(this);
    private final ClassesModule classesModule = new ClassesModule(this);
    private final FieldsModule fieldsModule = new FieldsModule(this);
    private final DataFetchModule dataFetchModule = new DataFetchModule(this);
    private final DataMutModule dataMutModule = new DataMutModule(this);
    private final CommitModule commitModule = new CommitModule(this);
    private final AuthModule authModule = new AuthModule(this);
    private final DeltaRecorderModule deltaRecorderModule = new DeltaRecorderModule(this);
    private final Random random = new Random();
    private final IndexingModuleNew indexingModule = new IndexingModuleNew(this);
    private final VersionsModule versionsModule = new VersionsModule(this);
    private final NotificationsModule notificationsModule = new NotificationsModule(this);
    private final NodesModule nodesModule = new NodesModule(this);
    private final ICallbackCommandReceiver callbackReceiver;
    private final SubscriptionModule subscriptionModule = new SubscriptionModule(this);
    private final NewRelationsModule newRelationsModule = new NewRelationsModule(this);
    private final TextModule textModule = new TextModule(this);
    private final EsModule esModule = new EsModule();
    private final SearchModule searchModule = new SearchModule(this);

    ODatabaseDocumentTx db;

    public static File getBuranHome() {
        final String userHomePath = System.getProperty("user.home");
        final File buranDir = new File(userHomePath, "buran_v0");
        if (!buranDir.exists()) {
            buranDir.mkdirs();
        }
        return buranDir;
    }

    public static File getOrientDbPath(UserId userId, boolean create) {
        final File buranHome = getBuranHome();
        final File orientHome = new File(buranHome, "orient");
        if (!orientHome.exists()) {
            orientHome.mkdirs();
        }

        final File buranDir = new File(orientHome, Long.toHexString(userId.getId()));
        if ((!buranDir.exists()) && (create)) {
            buranDir.mkdirs();
        }
        return buranDir;
    }

    public static String getDbString(File path) throws IOException {
        return "local:" + path.getCanonicalPath();
    }

    private void setup() {
        this.classesModule.setupDb();
        this.deltaRecorderModule.setupDb();
        this.versionsModule.setupDb();
        this.newRelationsModule.setupDb();
    }

    public ODatabaseDocument getDb() {
        return this.db;
    }

    public static void removeAccount(UserId userId) throws IOException {
        final File path = getOrientDbPath(userId, false);
        if (path.exists()) {
            for (final File file : path.listFiles()) {
                file.delete();
            }
            path.delete();
        }

        /* Remove es index */
        EsModule esMod = new EsModule();
        esMod.removeUserIndex(userId);
    }

    public BaseModule(UserId userId, UserId sender, ICallbackCommandReceiver callbackReceiver)
            throws IOException {
        this.callbackReceiver = callbackReceiver;
        final File path = getOrientDbPath(userId, false);
        final boolean needCreate = !path.exists();
        if (needCreate) {
            path.mkdirs();

            ODatabaseDocumentTx db = new ODatabaseDocumentTx(getDbString(path)).create();
            db.close();
        }
        this.db = ODatabaseDocumentPool.global().acquire(getDbString(path), "admin", "admin");
        setSender(sender);
        getAuthModule().setReceiver(userId);
        setup();

        if (needCreate) {
            /* Create index */
            getEsModule().removeUserIndex(userId);
            getEsModule().ensureIndex(userId);
        }
    }

    public boolean emitCallbackCommand(ICallbackCommand command) {
        return this.callbackReceiver.emit(getReceiver(), command);
    }

    public UserId getReceiver() {
        return getAuthModule().getReceiver();
    }

    public void setSender(UserId sender) {
        getAuthModule().setSender(sender);
    }

    public void close() {
        this.db.close();
        this.esModule.shutdown();
        //TODO: Zum testen
        // this.esModule.deleteData();
    }

    @Override
    protected void finalize() throws Throwable {
        close();
        super.finalize();
    }

    public CurrentTimestampProvider getCurrentTimestampProvider() {
        return currentTimestampProvider;
    }

    public IncubationModule getIncubationModule() {
        return incubationModule;
    }

    public DbUtils getDbUtils() {
        return dbUtils;
    }

    public ClassesModule getClassesModule() {
        return classesModule;
    }

    public FieldsModule getFieldsModule() {
        return fieldsModule;
    }

    public DataFetchModule getDataFetchModule() {
        return dataFetchModule;
    }

    public DataMutModule getDataMutModule() {
        return dataMutModule;
    }

    public CommitModule getCommitModule() {
        return commitModule;
    }

    public AuthModule getAuthModule() {
        return authModule;
    }

    public Random getRandom() {
        return random;
    }

    public DeltaRecorderModule getDeltaRecorderModule() {
        return deltaRecorderModule;
    }

    public VersionsModule getVersionsModule() {
        return versionsModule;
    }

    public NotificationsModule getNotificationsModule() {
        return notificationsModule;
    }

    public NodesModule getNodesModule() {
        return nodesModule;
    }

    public SubscriptionModule getSubscriptionModule() {
        return subscriptionModule;
    }

    public NewRelationsModule getNewRelationsModule() {
        return newRelationsModule;
    }

    public TextModule getTextModule() {
        return textModule;
    }

    public IndexingModuleNew getIndexingModule() {
        return indexingModule;
    }

    public EsModule getEsModule() {
        return esModule;
    }

    public SearchModule getSearchModule() {
        return searchModule;
    }
}
