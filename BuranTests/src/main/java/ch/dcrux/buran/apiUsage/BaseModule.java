package ch.dcrux.buran.apiUsage;

import com.dcrux.buran.commandBase.*;
import com.dcrux.buran.common.UserId;

import java.io.Serializable;

/**
 * Buran.
 *
 * @author: ${USER} Date: 29.06.13 Time: 13:45
 */
public class BaseModule {

    private final UserId receiver;
    private final UserId sender;
    private final ICommandRunner commandRunner;

    public BaseModule(UserId receiver, UserId sender, ICommandRunner commandRunner) {
        this.receiver = receiver;
        this.sender = sender;
        this.commandRunner = commandRunner;
    }

    public UserId getReceiver() {
        return receiver;
    }

    public UserId getSender() {
        return sender;
    }

    public ICommandRunner getCommandRunner() {
        return commandRunner;
    }

    public <TRetval extends Serializable> TRetval sync(ICommand<TRetval> command)
            throws UncheckedException, UnknownCommandException, WrappedExpectableException {
        return this.commandRunner.sync(getReceiver(), getSender(), command);
    }
}
