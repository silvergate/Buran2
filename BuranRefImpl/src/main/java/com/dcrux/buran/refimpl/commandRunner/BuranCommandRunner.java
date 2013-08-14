package com.dcrux.buran.refimpl.commandRunner;

import com.dcrux.buran.callbacksBase.ICallbackCommand;
import com.dcrux.buran.callbacksBase.ICallbackReceiver;
import com.dcrux.buran.commandBase.*;
import com.dcrux.buran.common.UserId;
import com.dcrux.buran.refimpl.commandDispatchBase.CommandDispatcher;
import com.dcrux.buran.refimpl.commandDispatchBase.ICommandImpl;
import com.dcrux.buran.refimpl.commandDispatchBase.ICommandInvoker;
import com.dcrux.buran.refimpl.commandDispatchBase.ICommandRunParamProvider;
import com.dcrux.buran.refimpl.commands.DispatcherConfigUtil;
import com.dcrux.buran.refimpl.modules.BaseModule;
import com.dcrux.buran.refimpl.modules.ICallbackCommandReceiver;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Queue;

/**
 * Buran.
 *
 * @author: ${USER} Date: 02.07.13 Time: 15:32
 */
public class BuranCommandRunner implements ICommandRunner, ICallbackReceiver {

    private final boolean clearDbs;

    private final ThreadLocal<BaseModule> baseModuleThreadLocal = new ThreadLocal<>();
    private final Multimap<UserId, BaseModule> inactiveBaseModulesUnSync = HashMultimap.create();
    private final Multimap<UserId, BaseModule> inactiveBaseModules =
            Multimaps.synchronizedMultimap(this.inactiveBaseModulesUnSync);
    private final CallbackQueuesRegistry callbackQueuesRegistry = new CallbackQueuesRegistry();

    private final ICommandInvoker commandInvoker = new ICommandInvoker() {
        @Override
        public <TRunParam, TRetval extends Serializable, TCommand extends ICommand<TRetval>>
        TRetval invoke(
                TCommand command, ICommandImpl<TRunParam, TRetval, TCommand> impl,
                ICommandRunParamProvider<TRunParam> commandRunParamProvider) throws Exception {
            return impl.run(command, commandRunParamProvider.getRunParam());
        }
    };

    private final ICommandRunParamProvider<BaseModule> baseModuleICommandRunParamProvider =
            new ICommandRunParamProvider<BaseModule>() {
                @Override
                public BaseModule getRunParam() {
                    return baseModuleThreadLocal.get();
                }
            };

    public BuranCommandRunner(boolean clearDbs) {
        this.clearDbs = clearDbs;
        DispatcherConfigUtil.config(this.commandDispatcher, baseModuleICommandRunParamProvider);
    }

    private BaseModule getActiveBaseModule() {
        return this.baseModuleThreadLocal.get();
    }

    private synchronized void activateBaseModule(UserId receiver, UserId sender)
            throws IOException {
        //TODO: Double check lock
        final BaseModule baseModuleGot = this.baseModuleThreadLocal.get();
        if (baseModuleGot != null) {
            throw new IllegalStateException("Already activated");
        }

        /* Can reuse */
        final Collection<BaseModule> reusables = this.inactiveBaseModules.get(receiver);
        if (reusables.size() > 0) {
            final BaseModule reuse = reusables.iterator().next();
            reuse.setSender(sender);
            this.baseModuleThreadLocal.set(reuse);
        } else {
            /* Nothing to reuse */
            System.out.println("BuranCommandRunner: Open new DB");
            /*if (this.clearDbs) {
                BaseModule.createNew(receiver, true);
            } */
            final BaseModule newModule = new BaseModule(receiver, sender, callbackReceiver);
            this.baseModuleThreadLocal.set(newModule);
        }
    }

    private final ICallbackCommandReceiver callbackReceiver = new ICallbackCommandReceiver() {
        @Override
        public boolean emit(UserId receiver, ICallbackCommand command) {
            return callbackQueuesRegistry.emit(receiver, command);
        }
    };

    private void deactivateBaseModule(UserId receiver) {
        BaseModule getActive = getActiveBaseModule();
        this.baseModuleThreadLocal.remove();
        inactiveBaseModules.put(receiver, getActive);
    }

    private final CommandDispatcher commandDispatcher = new CommandDispatcher(this.commandInvoker);

    public void shutdown() {
        for (final Map.Entry<UserId, BaseModule> entry : this.inactiveBaseModules.entries()) {
            final BaseModule baseModule = entry.getValue();
            baseModule.close();
        }
    }

    @Override
    public <TRetval extends Serializable> TRetval sync(UserId receiver, UserId sender,
            ICommand<TRetval> command)
            throws UncheckedException, UnknownCommandException, WrappedExpectableException {
        try {
            activateBaseModule(receiver, sender);
        } catch (IOException e) {
            throw new UncheckedException(e);
        }
        try {
            return this.commandDispatcher.invoke(command);
        } finally {
            deactivateBaseModule(receiver);
        }
    }

    @Override
    public void register(UserId receiver, Queue<ICallbackCommand> callbackCommandQueue) {
        this.callbackQueuesRegistry.register(receiver, callbackCommandQueue);
    }
}
