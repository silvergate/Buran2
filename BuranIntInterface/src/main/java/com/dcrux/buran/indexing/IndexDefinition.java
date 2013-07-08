package com.dcrux.buran.indexing;

import com.dcrux.buran.indexing.aggregatorFunction.AggregatorType;
import com.dcrux.buran.indexing.aggregatorStore.IAggregatorStore;
import com.dcrux.buran.indexing.mapFunction.MapFunction;
import com.dcrux.buran.indexing.mapInput.IMapInput;
import com.dcrux.buran.indexing.mapStore.IMapStore;
import com.google.common.base.Optional;

import java.io.Serializable;

/**
 * Buran.
 *
 * @author: ${USER} Date: 08.07.13 Time: 09:51
 */
public class IndexDefinition implements Serializable {

    public IndexDefinition(IMapInput mapInput, MapFunction mapFunction) {
        this.mapInput = mapInput;
        this.mapFunction = mapFunction;
    }

    private IMapInput mapInput;
    private MapFunction mapFunction;
    private IMapStore mapStore;
    private Optional<AggregatorType> aggregatorType;
    private Optional<IAggregatorStore> aggregatorStore;

    public IMapInput getMapInput() {
        return mapInput;
    }

    public MapFunction getMapFunction() {
        return mapFunction;
    }
}
