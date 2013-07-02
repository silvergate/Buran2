package com.dcrux.buran.commands.classes;

import com.dcrux.buran.commands.Command;
import com.dcrux.buran.common.classes.ClassHashId;
import com.dcrux.buran.common.classes.ClassId;
import com.dcrux.buran.common.exceptions.NodeClassNotFoundException;

import java.util.Set;

/**
 * Buran.
 *
 * @author: ${USER} Date: 02.07.13 Time: 17:17
 */
public class ComClassHashIdById extends Command<ClassHashId> {

    public static final Set<Class<? extends Exception>> EXCEPTIONS =
            exceptions(NodeClassNotFoundException.class);
    private final ClassId classId;

    public ComClassHashIdById(ClassId classId) {
        super(EXCEPTIONS);
        this.classId = classId;
    }

    public static ComClassHashIdById c(ClassId classId) {
        return new ComClassHashIdById(classId);
    }

    public ClassId getClassId() {
        return classId;
    }
}
