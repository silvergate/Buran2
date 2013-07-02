package com.dcrux.buran.commands.dataFetch;

import com.google.common.base.Optional;

import java.io.Serializable;

/**
 * Buran.
 *
 * @author: ${USER} Date: 02.07.13 Time: 18:19
 */
public class FetchResult<TFieldResult extends Serializable, TLabelResult extends Serializable>
        implements Serializable {
    private final Optional<TFieldResult> fieldResult;
    private final Optional<TLabelResult> labelResult;

    public FetchResult(Optional<TFieldResult> fieldResult, Optional<TLabelResult> labelResult) {
        this.fieldResult = fieldResult;
        this.labelResult = labelResult;
    }

    public Optional<TFieldResult> getFieldResult() {
        return fieldResult;
    }

    public Optional<TLabelResult> getLabelResult() {
        return labelResult;
    }
}
