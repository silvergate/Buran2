package com.dcrux.buran.commands;

import com.dcrux.buran.commandBase.ICommand;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Buran.
 *
 * @author: ${USER} Date: 02.07.13 Time: 14:23
 */
public class Command<TRetval extends Serializable> implements ICommand<TRetval> {

    protected Command() {
    }

    protected static Set<Class<? extends Exception>> exceptions(
            Class<? extends Exception>... exceptions) {
        final Set<Class<? extends Exception>> exSet = new HashSet<Class<? extends Exception>>();
        exSet.addAll(Arrays.asList(exceptions));
        return Collections.unmodifiableSet(exSet);
    }

    public Command(Set<Class<? extends Exception>> expectableExceptions) {
        //this.expectableExceptions = expectableExceptions;
    }

    //private Set<Class<? extends Exception>> expectableExceptions;

    @Override
    public Set<Class<? extends Exception>> getExpectableExceptions() {
        return null;
        //return this.expectableExceptions;
    }
}
