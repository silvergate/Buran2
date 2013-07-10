package com.dcrux.buran.common.indexing;

import com.dcrux.buran.common.indexing.aggregatorFunction.AggregatorType;
import com.dcrux.buran.common.indexing.aggregatorStore.IAggregatorStore;
import com.dcrux.buran.common.indexing.mapFunction.MapFunction;
import com.dcrux.buran.common.indexing.mapInput.IMapInput;
import com.dcrux.buran.common.indexing.mapStore.IMapIndex;
import com.google.common.base.Optional;

import java.io.Serializable;

/**
 * Buran.
 *
 * @author: ${USER} Date: 08.07.13 Time: 09:51
 */
public class IndexDefinition implements Serializable {

    public IndexDefinition(IMapInput mapInput, MapFunction mapFunction, IMapIndex mapIndexType) {
        this.mapInput = mapInput;
        this.mapFunction = mapFunction;
        this.mapIndexType = mapIndexType;
    }

    private IMapInput mapInput;
    private MapFunction mapFunction;
    private IMapIndex mapIndexType;
    private Optional<AggregatorType> aggregatorType;
    private Optional<IAggregatorStore> aggregatorStore;

    public IMapInput getMapInput() {
        return mapInput;
    }

    public MapFunction getMapFunction() {
        return mapFunction;
    }

    public IMapIndex getMapIndexType() {
        return mapIndexType;
    }
}
