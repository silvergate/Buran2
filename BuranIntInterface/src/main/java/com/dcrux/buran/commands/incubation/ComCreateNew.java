package com.dcrux.buran.commands.incubation;

import com.dcrux.buran.commands.Command;
import com.dcrux.buran.common.IncNid;
import com.dcrux.buran.common.classes.ClassId;

import java.util.Set;

/**
 * Buran.
 *
 * @author: ${USER} Date: 02.07.13 Time: 14:35
 */
public class ComCreateNew extends Command<IncNid> {
    private ClassId primaryClassId;
    public static final Set<Class<? extends Exception>> EXCEPTIONS = exceptions();

    private ComCreateNew() {
    }

    public static ComCreateNew c(ClassId primaryClassId) {
        return new ComCreateNew(primaryClassId);
    }

    public ComCreateNew(ClassId primaryClassId) {
        super(EXCEPTIONS);
        this.primaryClassId = primaryClassId;
    }

    public ClassId getPrimaryClassId() {
        return primaryClassId;
    }
}
