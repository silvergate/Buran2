package com.dcrux.buran.refimpl.baseModules.newIndexing.processors;

import com.dcrux.buran.query.indexingDef.StrAnalyzedDef;
import com.dcrux.buran.query.queries.unfielded.ISimpleQuery;
import com.dcrux.buran.query.queries.unfielded.StrPhrase;
import com.dcrux.buran.refimpl.baseModules.newIndexing.processorsIface.FilterOrQueryBuilder;
import com.dcrux.buran.refimpl.baseModules.newIndexing.processorsIface.IIndexingDefImpl;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import java.io.IOException;

/**
 * Buran.
 *
 * @author: ${USER} Date: 07.08.13 Time: 18:56
 */
public class StrAnalyzedIndexingDefImpl implements IIndexingDefImpl<String, StrAnalyzedDef> {

    @Override
    public Class<StrAnalyzedDef> getSupportedType() {
        return StrAnalyzedDef.class;
    }

    @Override
    public String getIndexDefSimplified(StrAnalyzedDef intIndexingDef) {
        return "type=string, store=no, index=analyzed, include_in_all=false";
    }

    @Override
    public void index(StrAnalyzedDef indexingTypeDef, XContentBuilder contentBuilder,
            String fieldName, Object value) throws IOException {
        final String str = (String) value;
        final String strShort;
        if (str.length() > indexingTypeDef.getNumberOfChars()) {
            strShort = str.substring(0, indexingTypeDef.getNumberOfChars());
        } else {
            strShort = str;
        }
        contentBuilder.field(fieldName, strShort);
    }

    @Override
    public FilterOrQueryBuilder build(String field,
            ISimpleQuery<String, StrAnalyzedDef> simpleQuery) {
        return buildMultifield(simpleQuery, field);
    }

    @Override
    public FilterOrQueryBuilder buildMultifield(ISimpleQuery<String, StrAnalyzedDef> simpleQuery,
            String... fields) {
        if (simpleQuery instanceof StrPhrase) {
            final QueryBuilder query;
            final StrPhrase strPhrase = (StrPhrase) simpleQuery;
            if (fields.length == 1) {
                final String singleField = fields[0];
                if (strPhrase.isIncludePrefix()) {
                    query = QueryBuilders
                            .matchPhrasePrefixQuery(singleField, strPhrase.getPhrase());
                } else {
                    query = QueryBuilders.matchPhraseQuery(singleField, strPhrase.getPhrase());
                }
            } else if (fields.length > 1) {
                if (strPhrase.isIncludePrefix()) {
                    query = QueryBuilders.multiMatchQuery(strPhrase.getPhrase(), fields)
                            .type(MatchQueryBuilder.Type.PHRASE_PREFIX);
                } else {
                    query = QueryBuilders.multiMatchQuery(strPhrase.getPhrase(), fields)
                            .type(MatchQueryBuilder.Type.PHRASE);

                }
            } else {
                throw new IllegalArgumentException("fields.length==0");
            }
            return FilterOrQueryBuilder.query(query);
        }

        throw new IllegalArgumentException("Unknown query type for this index");
    }

    @Override
    public int hashCode() {
        return StrAnalyzedIndexingDefImpl.class.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return StrAnalyzedIndexingDefImpl.class.equals(obj.getClass());
    }
}
