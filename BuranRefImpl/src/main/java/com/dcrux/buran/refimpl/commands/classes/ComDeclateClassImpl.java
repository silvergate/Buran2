package com.dcrux.buran.refimpl.commands.classes;

import com.dcrux.buran.commands.classes.ComDeclareClass;
import com.dcrux.buran.common.classes.ClassId;
import com.dcrux.buran.refimpl.commandDispatchBase.ICommandImpl;
import com.dcrux.buran.refimpl.modules.BaseModule;

/**
 * Buran.
 *
 * @author: ${USER} Date: 02.07.13 Time: 17:23
 */
public class ComDeclateClassImpl implements ICommandImpl<BaseModule, ClassId, ComDeclareClass> {
    public static final ComDeclateClassImpl SINGLETON = new ComDeclateClassImpl();

    @Override
    public Class<ComDeclareClass> getCommandClass() {
        return ComDeclareClass.class;
    }

    @Override
    public Class<BaseModule> getRunParamClass() {
        return BaseModule.class;
    }

    @Override
    public ClassId run(final ComDeclareClass command, final BaseModule baseModule)
            throws Exception {
        return baseModule.getClassesModule().declareClass(command.getClassDefinition());
    }
}
