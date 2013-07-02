package com.dcrux.buran.refimpl.commands.classes;

import com.dcrux.buran.commands.classes.ComDeclareClass;
import com.dcrux.buran.common.classes.ClassId;
import com.dcrux.buran.refimpl.baseModules.BaseModule;
import com.dcrux.buran.refimpl.baseModules.orientUtils.IRunner;
import com.dcrux.buran.refimpl.baseModules.orientUtils.ITransRet;
import com.dcrux.buran.refimpl.commandDispatchBase.ICommandImpl;
import com.orientechnologies.orient.core.db.document.ODatabaseDocument;

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
        try {
            ClassId classId;
            //TODO: Das ist noch nicht gut so, braucht mehrere versuche (duplicates)
            classId = baseModule.getDbUtils().run(new ITransRet<ClassId>() {
                @Override
                public ClassId run(ODatabaseDocument db, IRunner runner) throws Throwable {

                    return baseModule.getClassesModule().declareClass(command.getClassDefinition());
                }
            });
            baseModule.getClassesModule().createOrientClassIfNonExistent(classId);
            return classId;
        } catch (Throwable th) {
            throw new Exception(th);
        }
    }
}
