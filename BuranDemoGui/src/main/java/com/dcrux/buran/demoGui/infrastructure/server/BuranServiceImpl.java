package com.dcrux.buran.demoGui.infrastructure.server;

import com.dcrux.buran.commandBase.ICommand;
import com.dcrux.buran.commandBase.UncheckedException;
import com.dcrux.buran.commandBase.UnknownCommandException;
import com.dcrux.buran.commandBase.WrappedExpectableException;
import com.dcrux.buran.common.UserId;
import com.dcrux.buran.demoGui.infrastructure.client.BuranService;
import com.dcrux.buran.refimpl.baseModules.BaseModule;
import com.dcrux.buran.refimpl.commandRunner.BuranCommandRunner;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;

/**
 * Buran.
 *
 * @author: ${USER} Date: 14.07.13 Time: 23:21
 */
public class BuranServiceImpl extends RemoteServiceServlet implements BuranService {
    private volatile BuranCommandRunner bcr;

    @Override
    public void init() throws ServletException {
        super.init();    //To change body of overridden methods use File | Settings | File
        try {
            BaseModule.createNew(new UserId(0), true);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File
            // Templates.
        }
        //final UserId thisAccount = new UserId(0);
        //final UserId sender = new UserId(332);

        this.bcr = new BuranCommandRunner(false);
    }

    private UserId getSender() {
        final HttpServletRequest request = this.getThreadLocalRequest();
        final Cookie[] cookies = request.getCookies();
        for (final Cookie cookie : cookies) {
            if (cookie.getName().equals(COOKIE_USER_NAME)) {
                String cookieValue = cookie.getValue();
                final Long userIdLong = Long.parseLong(cookieValue);
                return new UserId(userIdLong);
            }
        }
        throw new IllegalArgumentException("Sender cookie is not set!");
    }

    @Override
    public <TRetVal extends Serializable> TRetVal run(UserId receiver, ICommand<TRetVal> command)
            throws UnknownCommandException, UncheckedException, WrappedExpectableException {
        return this.bcr.sync(receiver, getSender(), command);
    }

    @Override
    public void serTest(Serializable obj) {
        System.out.println("Got " + obj + " from client.");
    }
}