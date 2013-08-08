package com.dcrux.buran.query.queries.unfielded;

import com.dcrux.buran.query.indexingDef.StrAnalyzedDef;

/**
 * Buran.
 *
 * @author: ${USER} Date: 06.08.13 Time: 21:12
 */
public class StrPhrase
        implements IMultifieldSimpleQuery<String, StrAnalyzedDef>, IConstantIndicator {
    private String phrase;
    private boolean constant;
    private boolean includePrefix;

    public StrPhrase(String phrase, boolean includePrefix) {
        this.phrase = phrase;
        this.includePrefix = includePrefix;
    }

    public static StrPhrase exact(String phrase) {
        return new StrPhrase(phrase, false);
    }

    public static StrPhrase prefix(String phrase) {
        return new StrPhrase(phrase, true);
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

    @Override
    public boolean isConstantQuery() {
        return this.constant;
    }

    public boolean isIncludePrefix() {
        return includePrefix;
    }

    public String getPhrase() {
        return phrase;
    }
}
