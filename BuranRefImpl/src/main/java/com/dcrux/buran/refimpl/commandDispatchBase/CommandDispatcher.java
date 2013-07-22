package com.dcrux.buran.refimpl.commandDispatchBase;

import com.dcrux.buran.commandBase.ICommand;
import com.dcrux.buran.commandBase.UncheckedException;
import com.dcrux.buran.commandBase.UnknownCommandException;
import com.dcrux.buran.commandBase.WrappedExpectableException;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Buran.
 *
 * @author: ${USER} Date: 02.07.13 Time: 14:45
 */
public class CommandDispatcher {

    private final ICommandInvoker invoker;

    public CommandDispatcher(ICommandInvoker invoker) {
        this.invoker = invoker;
    }

    private static class Entry {
        private final ICommandImpl<?, ?, ?> commandImpl;
        private final ICommandRunParamProvider<?> runParamProvider;

        private Entry(ICommandImpl<?, ?, ?> commandImpl,
                ICommandRunParamProvider<?> runParamProvider) {
            this.commandImpl = commandImpl;
            this.runParamProvider = runParamProvider;
        }

        private ICommandImpl<?, ?, ?> getCommandImpl() {
            return commandImpl;
        }

        private ICommandRunParamProvider<?> getRunParamProvider() {
            return runParamProvider;
        }
    }

    private final Map<Class<?>, Entry> commandImpls = new HashMap<>();

    public <TRunParam, TRetval extends Serializable, TCommand extends ICommand<TRetval>> void
    register(
            ICommandImpl<TRunParam, TRetval, TCommand> impl,
            ICommandRunParamProvider<TRunParam> commandRunParamProvider) {
        this.commandImpls.put(impl.getCommandClass(), new Entry(impl, commandRunParamProvider));
    }

    public <TRetval extends Serializable, TCommand extends ICommand<TRetval>> TRetval invoke(
            TCommand command)
            throws UncheckedException, UnknownCommandException, WrappedExpectableException {
        final Entry entry = this.commandImpls.get(command.getClass());
        if (entry == null) {
            throw new UnknownCommandException();
        }
        final ICommandRunParamProvider runParam = entry.getRunParamProvider();
        final ICommandImpl impl = entry.getCommandImpl();
        try {
            return (TRetval) this.invoker.invoke(command, impl, runParam);
        } catch (Throwable throwable) {
            final Set<Class<? extends Exception>> checkedExceptions =
                    command.getExpectableExceptions();
            if (checkedExceptions != null && checkedExceptions.contains(throwable.getClass())) {
                throw new WrappedExpectableException((Exception) throwable);
            }
            throw new UncheckedException(throwable);
        }
    }
}
