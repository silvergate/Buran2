package com.dcrux.buran.refimpl.commands.classes;

import com.dcrux.buran.commands.classes.ComClassHashIdById;
import com.dcrux.buran.common.classes.ClassHashId;
import com.dcrux.buran.refimpl.commands.TransactionalCommand;
import com.dcrux.buran.refimpl.modules.BaseModule;

/**
 * Buran.
 *
 * @author: ${USER} Date: 02.07.13 Time: 18:37
 */
public class ComClassHashIdByIdImpl extends TransactionalCommand<ClassHashId, ComClassHashIdById> {
    public static final ComClassHashIdByIdImpl SINGLETON = new ComClassHashIdByIdImpl();


    @Override
    protected ClassHashId transactional(ComClassHashIdById command, BaseModule baseModule)
            throws Exception {
        return baseModule.getClassesModule().getClassHashIdById(command.getClassId());
    }

    @Override
    public Class<?> getCommandClass() {
        return ComClassHashIdById.class;
    }
}