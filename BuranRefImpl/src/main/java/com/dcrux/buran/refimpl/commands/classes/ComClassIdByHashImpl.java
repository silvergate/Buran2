package com.dcrux.buran.refimpl.commands.classes;

import com.dcrux.buran.commands.classes.ComClassIdByHash;
import com.dcrux.buran.common.classes.ClassId;
import com.dcrux.buran.refimpl.commands.TransactionalCommand;
import com.dcrux.buran.refimpl.modules.BaseModule;
import com.google.common.base.Optional;

/**
 * Buran.
 *
 * @author: ${USER} Date: 02.07.13 Time: 18:37
 */
public class ComClassIdByHashImpl
        extends TransactionalCommand<Optional<ClassId>, ComClassIdByHash> {
    public static final ComClassIdByHashImpl SINGLETON = new ComClassIdByHashImpl();


    @Override
    protected Optional<ClassId> transactional(ComClassIdByHash command, BaseModule baseModule)
            throws Exception {
        return baseModule.getClassesModule().getClassIdByClassHash(command.getClassHashId());
    }

    @Override
    public Class<?> getCommandClass() {
        return ComClassIdByHash.class;
    }
}