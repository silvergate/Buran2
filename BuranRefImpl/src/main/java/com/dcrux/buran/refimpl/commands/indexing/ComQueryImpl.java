package com.dcrux.buran.refimpl.commands.indexing;

import com.dcrux.buran.commands.indexing.ComQuery;
import com.dcrux.buran.commands.indexing.QueryResult;
import com.dcrux.buran.common.NidVer;
import com.dcrux.buran.refimpl.baseModules.BaseModule;
import com.dcrux.buran.refimpl.baseModules.index.keyGen.MapKey;
import com.dcrux.buran.refimpl.baseModules.orientUtils.IRunner;
import com.dcrux.buran.refimpl.baseModules.orientUtils.ITransRet;
import com.dcrux.buran.refimpl.commandDispatchBase.ICommandImpl;
import com.orientechnologies.orient.core.db.document.ODatabaseDocument;
import com.orientechnologies.orient.core.id.ORID;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Buran.
 *
 * @author: ${USER} Date: 09.07.13 Time: 22:05
 */
public class ComQueryImpl implements ICommandImpl<BaseModule, QueryResult, ComQuery> {

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
    public QueryResult run(final ComQuery command, final BaseModule baseModule) throws Exception {
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
                                        command.getLimitConfig().getLimit() + 1);
                    }
                });

        /* No results? */
        if (results == null) {
            return new QueryResult(false, Collections.<NidVer>emptyList());
        }

        /* Limited? */
        final boolean limited = results.size() > command.getLimitConfig().getLimit();

        if (limited && (!command.getLimitConfig().isReturnPartialResults())) {
            /* Limited. Dont return parial results */
            return new QueryResult(limited, Collections.<NidVer>emptyList());
        } else {
            List<NidVer> nidVerList = new ArrayList<>();
            for (final ORID entry : results) {
                nidVerList.add(new NidVer(entry.toString()));
                System.out.println("   - QUERY-RESULT: " + entry);
            }
            if (limited) {
                /* Remove last element */
                nidVerList = nidVerList.subList(0, command.getLimitConfig().getLimit() - 1);
            }
            return new QueryResult(limited, nidVerList);
        }
    }
}
