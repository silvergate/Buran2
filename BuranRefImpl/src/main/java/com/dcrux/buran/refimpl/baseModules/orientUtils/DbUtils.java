package com.dcrux.buran.refimpl.baseModules.orientUtils;

import com.dcrux.buran.refimpl.baseModules.BaseModule;
import com.dcrux.buran.refimpl.baseModules.common.Module;
import com.google.common.base.Optional;
import com.orientechnologies.orient.core.db.document.ODatabaseDocument;
import com.orientechnologies.orient.core.index.OIndex;
import com.orientechnologies.orient.core.index.OIndexManagerProxy;
import com.orientechnologies.orient.core.tx.OTransaction;

/**
 * Buran.
 *
 * @author: ${USER} Date: 29.06.13 Time: 14:03
 */
public class DbUtils extends Module<BaseModule> implements IRunner {

    public DbUtils(BaseModule baseModule) {
        super(baseModule);
    }

    public void run(ITransaction transaction, boolean reuse, ODatabaseDocument database)
            throws Exception {
        if (!reuse) {
            database.begin(OTransaction.TXTYPE.OPTIMISTIC);
        }
        try {
            transaction.run(database, this);
            if (!reuse) {
                database.commit();
            }
        } catch (Throwable t) {
            if (!reuse) {
                database.rollback();
            }
            if (t instanceof Exception) {
                throw (Exception) t;
            } else {
                throw new Exception(t);
            }
        }
    }

    public <T extends Object> T run(ITransRet<T> transaction, boolean reuse,
            ODatabaseDocument database) throws Exception {
        if (!reuse) {
            database.begin(OTransaction.TXTYPE.OPTIMISTIC);
        }
        try {
            final T retVal = transaction.run(database, this);
            if (!reuse) {
                database.commit();
            }
            return retVal;
        } catch (Throwable t) {
            if (!reuse) {
                database.rollback();
            }
            if (t instanceof Exception) {
                throw (Exception) t;
            } else {
                throw new Exception(t);
            }
        }
    }

    @Override
    public void run(ITransaction transaction) throws Exception {
        ODatabaseDocument database = getBase().getDb();
        run(transaction, false, database);
    }

    @Override
    public <T extends Object> T run(ITransRet<T> transaction) throws Exception {
        ODatabaseDocument database = getBase().getDb();
        return run(transaction, false, database);
    }

    private final IRunner reuseRunner = new IRunner() {
        @Override
        public void run(ITransaction transaction) throws Throwable {
            ODatabaseDocument database = getBase().getDb();
            DbUtils.this.run(transaction, true, database);
        }

        @Override
        public <T extends Object> T run(ITransRet<T> transaction) throws Throwable {
            ODatabaseDocument database = getBase().getDb();
            return DbUtils.this.run(transaction, true, database);
        }

        @Override
        public IRunner reuse() {
            return this;
        }
    };

    @Override
    public IRunner reuse() {
        return this.reuseRunner;
    }

    public OIndex getIndex(String orientClassName, String indexName) {
        final OIndexManagerProxy indexManager = getBase().getDb().getMetadata().getIndexManager();
        final OIndex<?> index = indexManager.getClassIndex(orientClassName, indexName);
        return index;
    }

    public Optional<Exception> commitAndReBegin() {
        try {
            getBase().getDb().commit();
        } catch (Exception throwable) {
            return Optional.of(throwable);
        } finally {
            getBase().getDb().begin(OTransaction.TXTYPE.OPTIMISTIC);
        }
        return Optional.absent();
    }
}
