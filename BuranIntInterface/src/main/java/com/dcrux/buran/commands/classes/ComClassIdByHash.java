package com.dcrux.buran.commands.classes;

import com.dcrux.buran.commands.Command;
import com.dcrux.buran.common.classes.ClassHashId;
import com.dcrux.buran.common.classes.ClassId;
import com.google.common.base.Optional;

import java.util.Set;

/**
 * Buran.
 *
 * @author: ${USER} Date: 02.07.13 Time: 17:17
 */
public class ComClassIdByHash extends Command<Optional<ClassId>> {

    public static final Set<Class<? extends Exception>> EXCEPTIONS = exceptions();
    private final ClassHashId classHashId;

    public ComClassIdByHash(ClassHashId classHashId) {
        super(EXCEPTIONS);
        this.classHashId = classHashId;
    }

    public static ComClassIdByHash c(ClassHashId classHashId) {
        return new ComClassIdByHash(classHashId);
    }

    public ClassHashId getClassHashId() {
        return classHashId;
    }
}
