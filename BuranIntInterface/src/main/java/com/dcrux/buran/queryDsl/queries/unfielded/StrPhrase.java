package com.dcrux.buran.queryDsl.queries.unfielded;

import com.dcrux.buran.queryDsl.indexingDef.StrAnalyzedDef;

/**
 * Buran.
 *
 * @author: ${USER} Date: 06.08.13 Time: 21:12
 */
public class StrPhrase implements IMultifieldSimpleQuery<String, StrAnalyzedDef> {
    private String phrase;

    public StrPhrase(String phrase) {
        this.phrase = phrase;
    }

    @Override
    public boolean supports(StrAnalyzedDef indexingDef) {
        return (indexingDef.getSupportedTypes().contains(StrAnalyzedDef.SupportedTypes.phrase));
    }

    @Override
    public boolean matches(StrAnalyzedDef indexingDef, String value)
            throws ImplementationNotDefinedException {
        throw new ImplementationNotDefinedException();
    }

    @Override
    public boolean isImplementationDefined() {
        return false;
    }
}
