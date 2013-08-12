package com.dcrux.buran.refimpl.baseModules.textExtractor;

import java.util.Date;

/**
 * Buran. See Dublin core.
 *
 * @author: ${USER} Date: 11.08.13 Time: 20:53
 */
public class MetadataTags {
    public static final MetadataTag<String> MIME = new MetadataTag<>();
    public static final MetadataTag<String> LANG = new MetadataTag<>();
    public static final MetadataTag<String> TITLE = new MetadataTag<>();
    public static final MetadataTag<String> DESCRIPTION = new MetadataTag<>();
    public static final MetadataTag<String> SUBJECT = new MetadataTag<>();

    public static final MetadataTag<String> PUBLISHER = new MetadataTag<>();
    public static final MetadataTag<String> CREATOR = new MetadataTag<>();

    public static final MetadataTag<Date> CREATED_AT = new MetadataTag<>();
    public static final MetadataTag<Date> LAST_MODIFIED = new MetadataTag<>();
}
