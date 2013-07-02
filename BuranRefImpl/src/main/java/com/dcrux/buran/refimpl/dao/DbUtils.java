package com.dcrux.buran.refimpl.dao;

import com.dcrux.buran.refimpl.baseModules.BaseModule;
import com.orientechnologies.orient.core.db.document.ODatabaseDocument;
import com.orientechnologies.orient.core.tx.OTransaction;

/**
 * Buran.
 *
 * @author: ${USER} Date: 29.06.13 Time: 14:03
 */
public class DbUtils implements IRunner {

    private final BaseModule reference;

    public DbUtils(BaseModule reference) {
        this.reference = reference;
    }

    public void run(ITransaction transaction, boolean reuse, ODatabaseDocument database)
            throws Throwable {
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
            throw t;
        }
    }

    public <T extends Object> T run(ITransRet<T> transaction, boolean reuse,
            ODatabaseDocument database) throws Throwable {
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
            throw t;
        }
    }

    @Override
    public void run(ITransaction transaction) throws Throwable {
        ODatabaseDocument database = this.reference.getDb();
        run(transaction, false, database);
    }

    @Override
    public <T extends Object> T run(ITransRet<T> transaction) throws Throwable {
        ODatabaseDocument database = this.reference.getDb();
        return run(transaction, false, database);
    }

    private final IRunner reuseRunner = new IRunner() {
        @Override
        public void run(ITransaction transaction) throws Throwable {
            ODatabaseDocument database = DbUtils.this.reference.getDb();
            DbUtils.this.run(transaction, true, database);
        }

        @Override
        public <T extends Object> T run(ITransRet<T> transaction) throws Throwable {
            ODatabaseDocument database = DbUtils.this.reference.getDb();
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
}
