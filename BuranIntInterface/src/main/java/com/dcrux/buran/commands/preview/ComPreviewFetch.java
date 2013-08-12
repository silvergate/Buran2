package com.dcrux.buran.commands.preview;

import com.dcrux.buran.commands.Command;
import com.dcrux.buran.common.NidVer;
import com.dcrux.buran.common.fields.FieldTarget;
import com.dcrux.buran.common.preview.PreviewSize;
import com.dcrux.buran.common.preview.PreviewType;
import com.google.common.base.Optional;

/**
 * Buran.
 *
 * @author: ${USER} Date: 12.08.13 Time: 10:12
 */
public class ComPreviewFetch extends Command<ComPreviewPeekResult> {
    private NidVer nidVer;
    private FieldTarget binaryField;
    private Optional<FieldTarget> mimeField;
    private PreviewSize previewSize;
    private PreviewType previewType;

    public ComPreviewFetch(NidVer nidVer, FieldTarget binaryField, Optional<FieldTarget> mimeField,
            PreviewSize previewSize, PreviewType previewType) {
        super(exceptions());
        this.nidVer = nidVer;
        this.binaryField = binaryField;
        this.mimeField = mimeField;
        this.previewSize = previewSize;
        this.previewType = previewType;
    }

    private ComPreviewFetch() {
    }

    public NidVer getNidVer() {
        return nidVer;
    }

    public FieldTarget getBinaryField() {
        return binaryField;
    }

    public Optional<FieldTarget> getMimeField() {
        return mimeField;
    }

    public PreviewSize getPreviewSize() {
        return previewSize;
    }

    public PreviewType getPreviewType() {
        return previewType;
    }
}
