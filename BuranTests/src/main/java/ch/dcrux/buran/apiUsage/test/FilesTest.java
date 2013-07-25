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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Buran.
 *
 * @author: ${USER} Date: 24.07.13 Time: 16:05
 */
public class FilesTest {

    private FilesModule filesModule;
    private DescModule descModule;

    private static final String TEST_FILE = "/testFiles/testFileOne.jpg";

    public FilesTest(BaseModule baseModule) {
        this.filesModule = new FilesModule(baseModule);
        this.descModule = new DescModule(baseModule);
    }

    public void createAndReadDesc()
            throws UnknownCommandException, WrappedExpectableException, UncheckedException,
            IOException {
        String fileName = "testFileOne.jpg";
        NidVer nidVer = createFile(fileName);
        final String desc = descModule.getBestDescription(nidVer, "unknown");
        System.out.println("Description: " + desc);
        testSize(nidVer);
    }

    private NidVer createFile(String filename)
            throws UnknownCommandException, UncheckedException, WrappedExpectableException,
            IOException {
        final IncNid incNide = filesModule.createFile("image/jpeg");

        final InputStream is = Test2.class.getResourceAsStream(TEST_FILE);
        int size = is.available();
        byte[] buffer = new byte[size];
        int numRead = is.read(buffer);
        filesModule.append(incNide, buffer);
        is.close();
        final CommitResult cr = filesModule.commitWithDesc(incNide, filename);
        return cr.getNid(incNide);
    }

    private void testSize(NidVer nidVer)
            throws UnknownCommandException, UncheckedException, WrappedExpectableException,
            IOException {
        long size = filesModule.getFileSize(nidVer);
        final URL url = getClass().getResource(TEST_FILE);
        final InputStream is = url.openStream();
        final long avail = is.available();
        System.out.println("File Size: " + size + ", should be: " + avail);
        is.close();
    }
}
