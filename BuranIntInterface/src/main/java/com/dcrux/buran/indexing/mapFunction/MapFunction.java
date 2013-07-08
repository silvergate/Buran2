package com.dcrux.buran.indexing.mapFunction;

import com.dcrux.buran.scripting.iface.Block;
import com.google.common.base.Optional;

import java.io.Serializable;

/**
 * Buran.
 *
 * @author: ${USER} Date: 08.07.13 Time: 01:13
 */
public class MapFunction implements Serializable {

    public static enum TextFunctionPos {
        pre,
        post
    }

    public static MapFunction single(Block mapFunction) {
        return new MapFunction(Optional.<TextFunction>absent(), Optional.<TextFunctionPos>absent(),
                Optional.of(mapFunction));
    }

    private MapFunction(Optional<TextFunction> textFunction,
            Optional<TextFunctionPos> textFunctionPos, Optional<Block> singleMapFunction) {
        this.textFunction = textFunction;
        this.textFunctionPos = textFunctionPos;
        this.singleMapFunction = singleMapFunction;
    }

    private Optional<TextFunction> textFunction;
    private Optional<TextFunctionPos> textFunctionPos;
    private Optional<Block> singleMapFunction;

    public Optional<TextFunction> getTextFunction() {
        return textFunction;
    }

    public Optional<TextFunctionPos> getTextFunctionPos() {
        return textFunctionPos;
    }

    public Optional<Block> getSingleMapFunction() {
        return singleMapFunction;
    }
}
