package com.dcrux.buran.refimpl.baseModules.text.processors.impl.enUK;

import com.dcrux.buran.refimpl.baseModules.text.processors.INormalizer;

import java.util.Locale;

/**
 * Buran.
 *
 * @author: ${USER} Date: 05.08.13 Time: 19:56
 */
public class Normalizer implements INormalizer {
    @Override
    public String normalize(String input) {
        return input.toLowerCase(Locale.ENGLISH);
    }
}
