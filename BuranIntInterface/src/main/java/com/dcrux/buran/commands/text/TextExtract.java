package com.dcrux.buran.commands.text;

import com.dcrux.buran.common.fields.FieldTarget;
import com.dcrux.buran.common.getterSetter.IDataSetter;

import java.util.HashSet;
import java.util.Set;

/**
 * Buran.
 *
 * @author: ${USER} Date: 11.08.13 Time: 22:35
 */
public class TextExtract implements IDataSetter {
    private Set<TransferEntry> transferEntries;
    private FieldTarget binaryField;

    public TextExtract(Set<TransferEntry> transferEntries, FieldTarget binaryField) {
        this.transferEntries = transferEntries;
        this.binaryField = binaryField;
    }

    public static TextExtract c(FieldTarget binaryField) {
        return new TextExtract(new HashSet<TransferEntry>(), binaryField);
    }

    public TextExtract addText(TextField textField, FieldTarget targetField, int charLimit,
            boolean append) {
        final TextTransfer textTransfer = new TextTransfer(charLimit, append);
        final TransferEntry transferEntry = new TransferEntry(textField, textTransfer, targetField);
        getTransferEntries().add(transferEntry);
        return this;
    }

    private TextExtract() {
    }

    public Set<TransferEntry> getTransferEntries() {
        return transferEntries;
    }

    public FieldTarget getBinaryField() {
        return binaryField;
    }
}
