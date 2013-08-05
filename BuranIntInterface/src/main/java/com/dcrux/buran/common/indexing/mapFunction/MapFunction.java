package com.dcrux.buran.common.indexing.mapFunction;

import com.dcrux.buran.scripting.iface.Code;
import com.google.common.base.Optional;

import java.io.Serializable;

/**
 * Buran.
 *
 * @author: ${USER} Date: 08.07.13 Time: 01:13
 */
public class MapFunction implements Serializable {

    private MapFunction() {
    }

    public static enum TextFunctionPos {
        pre, // Text funktion kann nicht pre sein.
        post
    }

    public static MapFunction single(Code mapFunction) {
        return new MapFunction(Optional.<TextFunction>absent(), Optional.of(mapFunction));
    }

    public static MapFunction withText(Code mapFunction, TextFunction textFunction) {
        return new MapFunction(Optional.<TextFunction>of(textFunction), Optional.of(mapFunction));
    }

    public static MapFunction text(TextFunction textFunction) {
        return new MapFunction(Optional.<TextFunction>of(textFunction), Optional.<Code>absent());
    }

    private MapFunction(Optional<TextFunction> textFunction, Optional<Code> singleMapFunction) {
        this.textFunction = textFunction;
        this.textFunctionPos = null;
        this.singleMapFunction = singleMapFunction;
    }

    private Optional<TextFunction> textFunction;
    private Optional<TextFunctionPos> textFunctionPos;
    private Optional<Code> singleMapFunction;

    public Optional<TextFunction> getTextFunction() {
        return textFunction;
    }

    @Deprecated
    public Optional<TextFunctionPos> getTextFunctionPos() {
        return textFunctionPos;
    }

    public Optional<Code> getSingleMapFunction() {
        return singleMapFunction;
    }
}
