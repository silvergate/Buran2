package com.dcrux.buran.demoGui.infrastructure.client;

import com.dcrux.buran.commandBase.ICommand;
import com.dcrux.buran.commandBase.UncheckedException;
import com.dcrux.buran.commandBase.UnknownCommandException;
import com.dcrux.buran.commandBase.WrappedExpectableException;
import com.dcrux.buran.common.UserId;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import java.io.Serializable;

/**
 * Buran.
 *
 * @author: ${USER} Date: 14.07.13 Time: 23:21
 */
@RemoteServiceRelativePath("BuranService")
public interface BuranService extends RemoteService {

    public static final String COOKIE_USER_NAME = "userName";

    /**
     * Utility/Convenience class. Use BuranService.App.getInstance() to access static instance of
     * BuranServiceAsync
     */
    public static class App {
        private static final BuranServiceAsync ourInstance =
                (BuranServiceAsync) GWT.create(BuranService.class);

        public static BuranServiceAsync getInstance() {
            return ourInstance;
        }
    }

    <TRetVal extends Serializable> TRetVal run(UserId receiver, ICommand<TRetVal> command)
            throws UnknownCommandException, UncheckedException, WrappedExpectableException;

    void serTest(Serializable obj);
}
