package com.dcrux.buran.refimpl.commands.indexing;

import com.dcrux.buran.commands.indexing.ComQuery;
import com.dcrux.buran.commands.indexing.IQueryResult;
import com.dcrux.buran.refimpl.baseModules.BaseModule;
import com.dcrux.buran.refimpl.baseModules.index.keyGen.MapKey;
import com.dcrux.buran.refimpl.baseModules.orientUtils.IRunner;
import com.dcrux.buran.refimpl.baseModules.orientUtils.ITransRet;
import com.dcrux.buran.refimpl.commandDispatchBase.ICommandImpl;
import com.orientechnologies.orient.core.db.document.ODatabaseDocument;
import com.orientechnologies.orient.core.id.ORID;

import java.util.Collection;
import java.util.List;

/**
 * Buran.
 *
 * @author: ${USER} Date: 09.07.13 Time: 22:05
 */
public class ComQueryImpl implements ICommandImpl<BaseModule, IQueryResult, ComQuery> {

    public static final ComQueryImpl SINGLETON = new ComQueryImpl();

    @Override
    public Class<?> getCommandClass() {
        return ComQuery.class;
    }

    @Override
    public Class<BaseModule> getRunParamClass() {
        return BaseModule.class;
    }

    @Override
    public IQueryResult run(final ComQuery command, final BaseModule baseModule) throws Exception {
        /* Generate key */
        final MapKey mapKey =
                baseModule.getIndexModule().getKeyGenModule().generate(command.getKeyGen());

        /* Query in transaction */
        final Collection<ORID> results =
                baseModule.getDbUtils().run(new ITransRet<Collection<ORID>>() {
                    @Override
                    public Collection<ORID> run(ODatabaseDocument db, IRunner runner)
                            throws Throwable {
                        return baseModule.getIndexModule().getMapIndexModule()
                                .get(command.getClassId(), command.getClassIndexName(), mapKey,
                                        10000);
                    }
                });

        for (final ORID entry : results) {
            System.out.println("   - QUERY-RESULT: " + entry);
        }

        return new IQueryResult() {
            @Override
            public List<String> getResults() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }
        };
    }
}
