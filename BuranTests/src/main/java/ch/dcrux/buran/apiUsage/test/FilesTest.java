package ch.dcrux.buran.apiUsage.test;

import ch.dcrux.buran.apiUsage.BaseModule;
import ch.dcrux.buran.apiUsage.DescModule;
import ch.dcrux.buran.apiUsage.FilesModule;
import com.dcrux.buran.commandBase.UncheckedException;
import com.dcrux.buran.commandBase.UnknownCommandException;
import com.dcrux.buran.commandBase.WrappedExpectableException;
import com.dcrux.buran.commands.incubation.CommitResult;
import com.dcrux.buran.commands.subscription.ComAddSub;
import com.dcrux.buran.common.IncNid;
import com.dcrux.buran.common.NidVer;
import com.dcrux.buran.common.classes.ClassId;
import com.dcrux.buran.common.subscription.SubId;
import com.dcrux.buran.query.queries.IQuery;
import com.dcrux.buran.query.queries.QueryTarget;
import com.dcrux.buran.query.queries.fielded.BoolQuery;
import com.dcrux.buran.query.queries.fielded.IOrQueryInput;
import com.dcrux.buran.query.queries.fielded.Query;
import com.dcrux.buran.query.queries.unfielded.IntCmp;
import com.dcrux.buran.query.queries.unfielded.StrPhrase;
import com.google.common.base.Optional;

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

    private static final String TEST_FILE = "/testFiles/WebsiteText.docx";

    public FilesTest(BaseModule baseModule) {
        this.filesModule = new FilesModule(baseModule);
        this.descModule = new DescModule(baseModule);
    }

    public void addSubscriptionNewFile()
            throws UnknownCommandException, UncheckedException, WrappedExpectableException {

        final IOrQueryInput query =
                Query.c(QueryTarget.cDef(this.filesModule.getFileClassId(), FilesModule.INDEX_SIZE),
                        IntCmp.ge(136000000));
        final IOrQueryInput query2 = Query.c(QueryTarget
                .cDef(this.descModule.getDescClassId(), DescModule.INDEX_ONE_TITLE),
                StrPhrase.prefix("testFileOne"));
        final IQuery iq2 = BoolQuery.c().must(query).must(query2);

        ComAddSub comAddSub = ComAddSub.c(new SubId(212), iq2);
        this.filesModule.getBase().sync(comAddSub);
    }

    public void createAndReadDesc()
            throws UnknownCommandException, WrappedExpectableException, UncheckedException,
            IOException {
        String fileName = "testFileOne.jpg Dies ist Microsoft Windows XP 2000.";
        NidVer nidVer = createFile(fileName);
        final String desc = descModule.getTitle(nidVer);
        System.out.println("Description (Read): " + desc);
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

    public void findByTitleNew()
            throws UnknownCommandException, UncheckedException, WrappedExpectableException {
        descModule.findByTitleNew(StrPhrase.prefix("dies ist Micro"),
                Optional.<ClassId>of(this.filesModule.getFileClassId()));

        /*System.out.println("QUERY BY FILESIZE:");
        final IQuery query = Query.c(
                QueryTarget.cDef(this.filesModule.getFileClassId(), FilesModule.INDEX_SIZE),
                IntCmp.ge(40000000));
        this.filesModule.getBase().sync(new ComQueryNew(query));*/
    }
}
