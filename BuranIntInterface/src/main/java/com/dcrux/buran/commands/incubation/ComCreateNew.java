package com.dcrux.buran.commands.incubation;

import com.dcrux.buran.commands.Command;
import com.dcrux.buran.common.IIncNid;
import com.dcrux.buran.common.classes.ClassId;

import java.util.Set;

/**
 * Buran.
 *
 * @author: ${USER} Date: 02.07.13 Time: 14:35
 */
public class ComCreateNew extends Command<IIncNid> {
    private final ClassId classId;
    public static final Set<Class<? extends Exception>> EXCEPTIONS = exceptions();

    public static ComCreateNew c(ClassId classId) {
        return new ComCreateNew(classId);
    }

    public ComCreateNew(ClassId classId) {
        super(EXCEPTIONS);
        this.classId = classId;
    }

    public ClassId getClassId() {
        return classId;
    }
}
