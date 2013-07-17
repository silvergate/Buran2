package com.dcrux.buran.commands.classes;

import com.dcrux.buran.commands.Command;
import com.dcrux.buran.common.classDefinition.ClassDefinition;
import com.dcrux.buran.common.classes.ClassId;

import java.util.Set;

/**
 * TODO: Das ist nur zum testen. Buran.
 *
 * @author: ${USER} Date: 02.07.13 Time: 17:17
 */
public class ComDeclareClass extends Command<ClassId> {

    public static final Set<Class<? extends Exception>> EXCEPTIONS = exceptions();
    private ClassDefinition classDefinition;

    public ComDeclareClass(ClassDefinition classDefinition) {
        super(EXCEPTIONS);
        this.classDefinition = classDefinition;
    }

    private ComDeclareClass() {
    }

    public ClassDefinition getClassDefinition() {
        return classDefinition;
    }
}
