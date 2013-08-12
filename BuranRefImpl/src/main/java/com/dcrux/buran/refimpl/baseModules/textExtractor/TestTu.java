package com.dcrux.buran.refimpl.baseModules.textExtractor;

import org.apache.tika.exception.TikaException;
import org.apache.tika.io.IOUtils;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Buran.
 *
 * @author: ${USER} Date: 09.08.13 Time: 18:32
 */
public class TestTu {
    public static void main(String[] args) throws IOException, TikaException, SAXException {
        TextUtils textUtils = new TextUtils();
        FileInputStream fis =
                new FileInputStream(new File("/Users/caelis/Desktop/WebsiteText" + ".docx"));
        final ParseResult result = textUtils.parse(fis);
        System.out.println("Resutl: " + result.getMetadata());
        //System.out.println("Resutl: " + IOUtils.toString(result.getReader()));
        IOUtils.closeQuietly(result.getReader());
        IOUtils.closeQuietly(fis);

        /*FileInputStream fis2 = new FileInputStream(new File("/Users/caelis/Documents/Heiniger " +
                "Onlineshop_ Kasse.pdf"));
        System.out.println("MIME: " + textUtils.detectMime(fis2));*/


    }
}
