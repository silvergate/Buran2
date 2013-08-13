package com.dcrux.buran.refimpl.baseModules.newIndexing.processorsIface;

import com.dcrux.buran.common.query.indexingDef.IIndexingDef;
import com.dcrux.buran.common.query.queries.unfielded.ISimpleQuery;
import org.elasticsearch.common.xcontent.XContentBuilder;

import java.io.IOException;
import java.io.Serializable;

/**
 * Buran.
 *
 * @author: ${USER} Date: 08.08.13 Time: 13:49
 */
public interface IIndexingDefImpl<TJavaType extends Serializable,
        TIndexingType extends IIndexingDef<TJavaType>> {
    Class<TIndexingType> getSupportedType();

    String getIndexDefSimplified(TIndexingType indexingType);

    void index(TIndexingType indexingTypeDef, XContentBuilder contentBuilder, String fieldName,
            Object value) throws IOException;

    FilterOrQueryBuilder build(String field, ISimpleQuery<TJavaType, TIndexingType> simpleQuery);

    FilterOrQueryBuilder buildMultifield(ISimpleQuery<TJavaType, TIndexingType> simpleQuery,
            String... fields);
}
