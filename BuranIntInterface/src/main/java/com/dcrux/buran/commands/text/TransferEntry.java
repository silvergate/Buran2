package com.dcrux.buran.commands.text;

import com.dcrux.buran.common.fields.FieldTarget;

import java.io.Serializable;

/**
 * Buran.
 *
 * @author: ${USER} Date: 11.08.13 Time: 22:46
 */
public class TransferEntry implements Serializable {
    private TextField field;
    private ITransferConfig transferConfig;
    private FieldTarget targetField;

    public TransferEntry(TextField field, ITransferConfig transferConfig, FieldTarget targetField) {
        this.field = field;
        this.transferConfig = transferConfig;
        this.targetField = targetField;
    }

    private TransferEntry() {
    }

    public TextField getField() {
        return field;
    }

    public ITransferConfig getTransferConfig() {
        return transferConfig;
    }

    public FieldTarget getTargetField() {
        return targetField;
    }
}
