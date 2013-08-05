package com.dcrux.buran.scripting.compiler;

import com.dcrux.buran.scripting.iface.*;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Buran.
 *
 * @author: ${USER} Date: 05.07.13 Time: 12:05
 */
public class Compiler {
    private final IImplementationProvider implementationProvider;

    public Compiler(IImplementationProvider implementationProvider) {
        this.implementationProvider = implementationProvider;
    }

    public CompiledBlock compile(Code code) {
        final CompiledFunction[] compiledFunctions = new CompiledFunction[code.getNumberOfLines()];
        for (int i = 0; i < code.getNumberOfLines(); i++) {
            compiledFunctions[i] = compileFunction(code.getLine(i));
        }
        return new CompiledBlock(compiledFunctions);
    }

    private CompiledFunction compileFunction(IFunctionDeclaration declaration) {
        final List<IFunctionDeclaration<?>> inputs = declaration.getInput();
        final List<CompiledFunction> compiledArgs = new ArrayList<CompiledFunction>();
        for (final IFunctionDeclaration<?> arg : inputs) {
            compiledArgs.add(compileFunction(arg));
        }

        /* Get implementation */
        final IFunctionImplFactory factory =
                this.implementationProvider.getFactory(declaration.getClass());
        if (factory == null) {
            throw new IllegalArgumentException(MessageFormat
                    .format("No implementation for {0} " + "found", declaration.getClass()));
        }
        final IFunctionImplementation impl = factory.getImplementation(declaration);
        return new CompiledFunction(impl, compiledArgs.toArray(new IEvaluable[]{}));
    }
}
