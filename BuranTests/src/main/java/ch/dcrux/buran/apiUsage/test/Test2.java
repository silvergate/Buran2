package ch.dcrux.buran.apiUsage.test;

import ch.dcrux.buran.apiUsage.BaseModule;
import ch.dcrux.buran.apiUsage.DescModule;
import ch.dcrux.buran.apiUsage.FilesModule;
import com.dcrux.buran.commandBase.UncheckedException;
import com.dcrux.buran.commandBase.UnknownCommandException;
import com.dcrux.buran.commandBase.WrappedExpectableException;
import com.dcrux.buran.commands.incubation.CommitResult;
import com.dcrux.buran.common.IncNid;
import com.dcrux.buran.common.NidVer;
import com.dcrux.buran.common.UserId;
import com.dcrux.buran.refimpl.commandRunner.BuranCommandRunner;

import java.io.IOException;
import java.io.InputStream;

/**
 * Buran.
 *
 * @author: ${USER} Date: 24.07.13 Time: 15:07
 */
public class Test2 {

    private static void createAndReadDesc(DescModule descModule, FilesModule filesModule)
            throws UnknownCommandException, WrappedExpectableException, UncheckedException,
            IOException {
        String fileName = "testFileOne.jpg";
        NidVer nidVer = createFile(filesModule, fileName);
        final String desc = descModule.getBestDescription(nidVer, "unknown");
        System.out.println("Description: " + desc);
    }

    private static NidVer createFile(FilesModule filesModule, String filename)
            throws UnknownCommandException, UncheckedException, WrappedExpectableException,
            IOException {
        final IncNid incNide = filesModule.createFile("image/jpeg");

        final InputStream is = Test2.class.getResourceAsStream("/testFiles/testFileOne.jpg");
        int size = is.available();
        byte[] buffer = new byte[size];
        int numRead = is.read(buffer);
        filesModule.append(incNide, buffer);
        is.close();
        final CommitResult cr = filesModule.commitWithDesc(incNide, filename);
        return cr.getNid(incNide);
    }

    public static void main(String[] args)
            throws IOException, InterruptedException, UnknownCommandException, UncheckedException,
            WrappedExpectableException {
        com.dcrux.buran.refimpl.baseModules.BaseModule.createNew(new UserId(0), true);
        final UserId thisAccount = new UserId(0);
        final UserId sender = new UserId(332);

        BuranCommandRunner bcr = new BuranCommandRunner(false);
        BaseModule bm = new BaseModule(thisAccount, sender, bcr);

        final FilesTest filesTest = new FilesTest(bm);
        filesTest.createAndReadDesc();

        bcr.shutdown();

        Thread.sleep(500);
        System.exit(0);
    }
}
