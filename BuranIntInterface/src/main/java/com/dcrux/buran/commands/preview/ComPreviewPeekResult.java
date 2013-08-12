package com.dcrux.buran.commands.preview;

import com.dcrux.buran.common.preview.PreviewCacheId;

import java.io.Serializable;

/**
 * Buran.
 *
 * @author: ${USER} Date: 12.08.13 Time: 10:15
 */
public class ComPreviewPeekResult implements Serializable {
    private PreviewCacheId cacheId;
    private int size;

    public ComPreviewPeekResult(PreviewCacheId cacheId, int size) {
        this.cacheId = cacheId;
        this.size = size;
    }

    private ComPreviewPeekResult() {
    }

    public PreviewCacheId getCacheId() {
        return cacheId;
    }

    public int getSize() {
        return size;
    }
}
