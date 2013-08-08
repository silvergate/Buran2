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
import com.dcrux.buran.refimpl.baseModules.index.IndexModule;
import com.dcrux.buran.refimpl.baseModules.newIndexing.IndexingModuleNew;
import com.dcrux.buran.refimpl.baseModules.newIndexing.SearchModule;
import com.dcrux.buran.refimpl.baseModules.newRelations.NewRelationsModule;
import com.dcrux.buran.refimpl.baseModules.nodes.NodesModule;
import com.dcrux.buran.refimpl.baseModules.notifications.NotificationsModule;
import com.dcrux.buran.refimpl.baseModules.orientUtils.DbUtils;
import com.dcrux.buran.refimpl.baseModules.text.TextModule;
import com.dcrux.buran.refimpl.baseModules.time.CurrentTimestampProvider;
import com.dcrux.buran.refimpl.baseModules.versions.VersionsModule;
import com.dcrux.buran.refimpl.subscription.SubscriptionModule;
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
    private final IndexModule indexModule = new IndexModule(this);
    private final IndexingModuleNew indexingModule = new IndexingModuleNew(this);
    private final VersionsModule versionsModule = new VersionsModule(this);
    private final NotificationsModule notificationsModule = new NotificationsModule(this);
    private final NodesModule nodesModule = new NodesModule(this);
    private final ICallbackCommandReceiver callbackReceiver;
    private final SubscriptionModule subscriptionModule;
    private final NewRelationsModule newRelationsModule = new NewRelationsModule(this);
    private final TextModule textModule = new TextModule(this);
    private final EsModule esModule = new EsModule(this);
    private final SearchModule searchModule = new SearchModule(this);

    ODatabaseDocumentTx db;

    public static File getDbPath(UserId userId) {
        return new File("/Users/caelis/" + userId.getId() + ".orientdb");
    }

    public static String getDbString(File path) throws IOException {
        return "local:" + path.getCanonicalPath();
    }

    private void setup() {
        this.classesModule.setupDb();
        this.deltaRecorderModule.setupDb();
        this.versionsModule.setupDb();
        this.indexModule.setupDb();
        this.newRelationsModule.setupDb();
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

    public BaseModule(UserId userId, UserId sender, ICallbackCommandReceiver callbackReceiver,
            SubscriptionModule subscriptionModule) throws IOException {
        this.callbackReceiver = callbackReceiver;
        this.subscriptionModule = subscriptionModule;
        final File path = getDbPath(userId);
        this.db = ODatabaseDocumentPool.global().acquire(getDbString(path), "admin", "admin");
        setSender(sender);
        getAuthModule().setReceiver(userId);
        setup();
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
        this.esModule.deleteData();
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

    public IndexModule getIndexModule() {
        return indexModule;
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
