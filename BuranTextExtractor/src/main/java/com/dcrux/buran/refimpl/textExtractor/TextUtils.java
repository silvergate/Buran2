package com.dcrux.buran.refimpl.textExtractor;

import org.apache.tika.Tika;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.detect.Detector;
import org.apache.tika.exception.TikaException;
import org.apache.tika.io.IOUtils;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.TikaCoreProperties;
import org.apache.tika.mime.MediaType;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.ParsingReader;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;

/**
 * Buran.
 *
 * @author: ${USER} Date: 06.08.13 Time: 11:01
 */
public class TextUtils {
    private final Tika tika = new Tika();

    public ParseResult parse(InputStream inputStream)
            throws IOException, TikaException, SAXException {
        Metadata metadata = new Metadata();

        ParseContext context = new ParseContext();

        //ContentHandler handler = new DefaultHandler();

        final ParsingReader reader =
                new ParsingReader(tika.getParser(), inputStream, metadata, context);

        return new ParseResult(reader, metadata);
    }

    public static void toMeta(Metadata metadata,
            com.dcrux.buran.refimpl.textExtractor.Metadata meta) {
        meta.putNull(MetadataTags.MIME, metadata.get(TikaCoreProperties.TYPE));
        meta.putNull(MetadataTags.LANG, metadata.get(TikaCoreProperties.LANGUAGE));
        meta.putNull(MetadataTags.TITLE, metadata.get(TikaCoreProperties.TITLE));
        meta.putNull(MetadataTags.DESCRIPTION, metadata.get(TikaCoreProperties.DESCRIPTION));
        meta.putNull(MetadataTags.SUBJECT, metadata.get(TikaCoreProperties.KEYWORDS));
        meta.putNull(MetadataTags.PUBLISHER, metadata.get(TikaCoreProperties.PUBLISHER));
        meta.putNull(MetadataTags.CREATOR, metadata.get(TikaCoreProperties.CREATOR));
        meta.putNull(MetadataTags.CREATED_AT, metadata.getDate(TikaCoreProperties.CREATED));
        meta.putNull(MetadataTags.LAST_MODIFIED, metadata.getDate(TikaCoreProperties.MODIFIED));
    }

    public String detectMime(InputStream inputStream) throws IOException {
        TikaConfig config = TikaConfig.getDefaultConfig();
        Detector detector = config.getDetector();

        TikaInputStream stream = TikaInputStream.get(inputStream);

        Metadata metadata = new Metadata();
        //metadata.add(Metadata.RESOURCE_NAME_KEY, filenameWithExtension);
        MediaType mediaType = detector.detect(stream, metadata);
        IOUtils.closeQuietly(inputStream);


        System.out.println("Meta: " + metadata.get(TikaCoreProperties.DESCRIPTION));

        return mediaType.getType() + "/" + mediaType.getSubtype();
    }

}
