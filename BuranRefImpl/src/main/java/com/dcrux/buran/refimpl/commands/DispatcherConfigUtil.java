package com.dcrux.buran.refimpl.commands;

import com.dcrux.buran.refimpl.commandDispatchBase.CommandDispatcher;
import com.dcrux.buran.refimpl.commandDispatchBase.ICommandRunParamProvider;
import com.dcrux.buran.refimpl.commands.classes.ComClassHashIdByIdImpl;
import com.dcrux.buran.refimpl.commands.classes.ComClassIdByHashImpl;
import com.dcrux.buran.refimpl.commands.classes.ComDeclateClassImpl;
import com.dcrux.buran.refimpl.commands.dataFetch.ComFetchImpl;
import com.dcrux.buran.refimpl.commands.dataMut.ComMutateImpl;
import com.dcrux.buran.refimpl.commands.domains.ComDomCreateImpl;
import com.dcrux.buran.refimpl.commands.domains.ComDomDefineImpl;
import com.dcrux.buran.refimpl.commands.domains.ComDomGetIdImpl;
import com.dcrux.buran.refimpl.commands.incubation.ComCommitImpl;
import com.dcrux.buran.refimpl.commands.incubation.ComCreateNewImpl;
import com.dcrux.buran.refimpl.commands.incubation.ComCreateUpdateImpl;
import com.dcrux.buran.refimpl.commands.indexingNew.ComQueryNewImpl;
import com.dcrux.buran.refimpl.commands.subscription.ComAddSubImpl;
import com.dcrux.buran.refimpl.modules.BaseModule;

/**
 * Buran.
 *
 * @author: ${USER} Date: 02.07.13 Time: 15:12
 */
public class DispatcherConfigUtil {
    public static void config(CommandDispatcher dispatcher,
            ICommandRunParamProvider<BaseModule> runParam) {
        dispatcher.register(ComCreateNewImpl.SINGLETON, runParam);
        dispatcher.register(ComCommitImpl.SINGLETON, runParam);
        dispatcher.register(ComDeclateClassImpl.SINGLETON, runParam);
        dispatcher.register(ComMutateImpl.SINGLETON, runParam);
        dispatcher.register(ComFetchImpl.SINGLETON, runParam);
        dispatcher.register(ComClassIdByHashImpl.SINGLETON, runParam);
        dispatcher.register(ComClassHashIdByIdImpl.SINGLETON, runParam);
        dispatcher.register(ComCreateUpdateImpl.SINGLETON, runParam);
        dispatcher.register(ComQueryNewImpl.SINGLETON, runParam);
        dispatcher.register(ComAddSubImpl.SINGLETON, runParam);

        /* Domains */
        dispatcher.register(ComDomCreateImpl.SINGLETON, runParam);
        dispatcher.register(ComDomDefineImpl.SINGLETON, runParam);
        dispatcher.register(ComDomGetIdImpl.SINGLETON, runParam);
    }
}
