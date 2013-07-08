package com.dcrux.buran.refimpl.baseModules.index.functionCompiler;

import com.dcrux.buran.common.classDefinition.ClassIndexDefinition;
import com.dcrux.buran.common.classDefinition.ClassIndexName;
import com.dcrux.buran.indexing.IndexDefinition;
import com.dcrux.buran.indexing.mapFunction.MapFunction;
import com.dcrux.buran.refimpl.baseModules.BaseModule;
import com.dcrux.buran.refimpl.baseModules.common.Module;
import com.dcrux.buran.scripting.functionsImpl.FunAssignImpl;
import com.dcrux.buran.scripting.functionsImpl.FunGetImpl;
import com.dcrux.buran.scripting.functionsImpl.FunRetImpl;
import com.dcrux.buran.scripting.functionsImpl.flow.FunIfImpl;
import com.dcrux.buran.scripting.functionsImpl.integer.FunIntCmpImpl;
import com.dcrux.buran.scripting.functionsImpl.integer.FunIntLitImpl;
import com.dcrux.buran.scripting.functionsImpl.integer.FunIntOpImpl;
import com.dcrux.buran.scripting.functionsImpl.integer.FunIntToBinImpl;
import com.dcrux.buran.scripting.functionsImpl.list.FunListNewImpl;
import com.dcrux.buran.scripting.functionsImpl.string.FunStrConcatImpl;
import com.dcrux.buran.scripting.functionsImpl.string.FunStrLenImpl;
import com.dcrux.buran.scripting.functionsImpl.string.FunStrLitImpl;
import com.dcrux.buran.scripting.functionsImpl.string.FunStrTrimImpl;
import com.dcrux.buran.scripting.iface.Block;
import com.dcrux.buran.scripting.iface.compiler.CompiledBlock;
import com.dcrux.buran.scripting.iface.compiler.ImplProviderRegistry;
import com.google.common.base.Optional;

import java.util.HashMap;
import java.util.Map;

/**
 * Buran.
 *
 * @author: ${USER} Date: 08.07.13 Time: 22:34
 */
public class FunctionCompiler extends Module<BaseModule> {

    public static ImplProviderRegistry getRegistry() {
        final ImplProviderRegistry ipr = new ImplProviderRegistry();
        ipr.register(FunAssignImpl.FACTORY());
        ipr.register(FunGetImpl.FACTORY());
        ipr.register(FunIntLitImpl.FACTORY);
        ipr.register(FunRetImpl.FACTORY());
        ipr.register(FunStrLitImpl.FACTORY);
        ipr.register(FunStrLenImpl.FACTORY);
        ipr.register(FunStrTrimImpl.FACTORY);
        ipr.register(FunStrConcatImpl.FACTORY);
        ipr.register(FunIntOpImpl.FACTORY);
        ipr.register(FunIfImpl.FACTORY);
        ipr.register(FunIntCmpImpl.FACTORY);
        ipr.register(FunListNewImpl.FACTORY);
        ipr.register(FunIntToBinImpl.FACTORY);

        return ipr;
    }

    public FunctionCompiler(BaseModule baseModule) {
        super(baseModule);
    }

    public Map<ClassIndexName, CompiledBlock> compileAndValidateMapFunctions(
            ClassIndexDefinition cid) {
        final Map<ClassIndexName, CompiledBlock> result = new HashMap<>();
        for (final Map.Entry<ClassIndexName, IndexDefinition> entry : cid.getIndexDefinitionMap()
                .entrySet()) {
            final IndexDefinition indexDefinition = entry.getValue();
            final MapFunction mapFunction = indexDefinition.getMapFunction();
            final Optional<Block> singleFunctionOpt = mapFunction.getSingleMapFunction();
            if (singleFunctionOpt.isPresent()) {
                final Block singleFunction = singleFunctionOpt.get();
                final com.dcrux.buran.scripting.iface.compiler.Compiler compiler =
                        new com.dcrux.buran.scripting.iface.compiler.Compiler(getRegistry());
                final CompiledBlock compiled = compiler.compile(singleFunction);
                result.put(entry.getKey(), compiled);
            }
        }
        return result;
    }
}
