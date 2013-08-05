package test;

import com.dcrux.buran.scripting.compiler.CompiledBlock;
import com.dcrux.buran.scripting.compiler.ImplProviderRegistry;
import com.dcrux.buran.scripting.functions.FunAssign;
import com.dcrux.buran.scripting.functions.FunGet;
import com.dcrux.buran.scripting.functions.FunRet;
import com.dcrux.buran.scripting.functions.flow.FunIf;
import com.dcrux.buran.scripting.functions.integer.FunIntCmp;
import com.dcrux.buran.scripting.functions.integer.FunIntLit;
import com.dcrux.buran.scripting.functions.integer.FunIntOp;
import com.dcrux.buran.scripting.functions.string.FunStrConcat;
import com.dcrux.buran.scripting.functions.string.FunStrLen;
import com.dcrux.buran.scripting.functions.string.FunStrLit;
import com.dcrux.buran.scripting.functions.string.FunStrTrim;
import com.dcrux.buran.scripting.functionsImpl.FunAssignImpl;
import com.dcrux.buran.scripting.functionsImpl.FunGetImpl;
import com.dcrux.buran.scripting.functionsImpl.FunRetImpl;
import com.dcrux.buran.scripting.functionsImpl.flow.FunIfImpl;
import com.dcrux.buran.scripting.functionsImpl.integer.FunIntCmpImpl;
import com.dcrux.buran.scripting.functionsImpl.integer.FunIntLitImpl;
import com.dcrux.buran.scripting.functionsImpl.integer.FunIntOpImpl;
import com.dcrux.buran.scripting.functionsImpl.string.FunStrConcatImpl;
import com.dcrux.buran.scripting.functionsImpl.string.FunStrLenImpl;
import com.dcrux.buran.scripting.functionsImpl.string.FunStrLitImpl;
import com.dcrux.buran.scripting.functionsImpl.string.FunStrTrimImpl;
import com.dcrux.buran.scripting.iface.Code;
import com.dcrux.buran.scripting.iface.LazyLineNum;
import com.dcrux.buran.scripting.iface.ProgrammErrorException;
import com.dcrux.buran.scripting.iface.VarName;
import com.dcrux.buran.scripting.iface.types.IntegerType;
import com.dcrux.buran.scripting.iface.types.StringType;
import com.dcrux.buran.scripting.metaRunner.MetaRunner;
import com.dcrux.buran.scripting.runner.Runner;

/**
 * Buran.
 *
 * @author: ${USER} Date: 05.07.13 Time: 07:44
 */
public class ScriptingTest1 {

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

        return ipr;
    }

    public static Code loopTest() {
        final Code code = Code.c();
        code.add(FunAssign.c(VarName.c("counter"), FunIntLit.c(10)));
        code.add(FunAssign.c(VarName.c("str"), FunStrLit.c("BEGIN: ")));
        LazyLineNum decLine = code.addLz(FunAssign.c(VarName.c("counter"),
                FunIntOp.sub(FunGet.c(VarName.c("counter"), IntegerType.class), FunIntLit.c(1))));
        code.add(FunAssign.c(VarName.c("str"), FunStrConcat
                .c(FunGet.c(VarName.c("str"), StringType.class), FunStrLit.c("++plus++"))));
        code.add(FunIf.c(decLine,
                FunIntCmp.gt(FunGet.c(VarName.c("counter"), IntegerType.class), FunIntLit.c(0))));
        code.add(FunRet.c(FunGet.c(VarName.c("str"), StringType.class)));
        return code;
    }

    public static void evalAndRun(Code code) throws ProgrammErrorException {
        MetaRunner mr = new MetaRunner(Integer.MAX_VALUE, Integer.MAX_VALUE);
        System.out.println(mr.evaluate(code));

        final com.dcrux.buran.scripting.compiler.Compiler compiler =
                new com.dcrux.buran.scripting.compiler.Compiler(getRegistry());
        final CompiledBlock compiled = compiler.compile(code);

        final Runner runner = new Runner();
        System.out.println("Run-Result: " + runner.run(compiled));

    }

    public static void main(String[] args) throws ProgrammErrorException {
        final ImplProviderRegistry registry = getRegistry();

        final Code code = Code.c();
        code.add(FunAssign.c(VarName.c("hallo"), FunIntCmp.gt(FunIntLit.c(4), FunIntLit.c(4))));
        //code.add(FunRet.c(FunGet.c(VarName.c("hallo"), BoolType.class)));

        code.add(FunAssign.c(VarName.c("str"),
                FunStrConcat.c(FunStrLit.c(" Hallo "), FunStrLit.c("  Welt "))));
        code.add(FunAssign.c(VarName.c("hallo"),
                FunStrTrim.c(FunStrTrim.c(FunGet.c(VarName.c("str"), StringType.class)))));
        code.add(FunRet.c(FunIntOp.add(FunStrLen.c(FunGet.c(VarName.c("hallo"), StringType.class)),
                FunIntLit.c(21212))));

        evalAndRun(code);

        final Code code2 = loopTest();
        evalAndRun(code2);
    }
}
