package com.dcrux.buran.refimpl.commands;

import com.dcrux.buran.refimpl.baseModules.BaseModule;
import com.dcrux.buran.refimpl.commandDispatchBase.CommandDispatcher;
import com.dcrux.buran.refimpl.commandDispatchBase.ICommandRunParamProvider;
import com.dcrux.buran.refimpl.commands.classes.ComClassHashIdByIdImpl;
import com.dcrux.buran.refimpl.commands.classes.ComClassIdByHashImpl;
import com.dcrux.buran.refimpl.commands.classes.ComDeclateClassImpl;
import com.dcrux.buran.refimpl.commands.dataFetch.ComFetchImpl;
import com.dcrux.buran.refimpl.commands.dataMut.ComMutateImpl;
import com.dcrux.buran.refimpl.commands.incubation.ComCommitImpl;
import com.dcrux.buran.refimpl.commands.incubation.ComCreateNewImpl;

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
    }
}
