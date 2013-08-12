package com.dcrux.buran.commands.preview;

import com.dcrux.buran.common.preview.PreviewCacheId;

import java.io.Serializable;

/**
 * Buran.
 *
 * @author: ${USER} Date: 12.08.13 Time: 10:15
 */
public class ComPreviewFetchResult implements Serializable {
    private PreviewCacheId cacheId;
    private byte[] data;

    public ComPreviewFetchResult(PreviewCacheId cacheId, byte[] data) {
        this.cacheId = cacheId;
        this.data = data;
    }

    private ComPreviewFetchResult() {
    }

    public PreviewCacheId getCacheId() {
        return cacheId;
    }

    public byte[] getData() {
        return data;
    }
}
