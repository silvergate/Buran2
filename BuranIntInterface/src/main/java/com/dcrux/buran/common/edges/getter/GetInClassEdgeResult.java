package com.dcrux.buran.common.edges.getter;

import com.dcrux.buran.common.INid;
import com.dcrux.buran.common.edges.LabelIndex;

import java.io.Serializable;
import java.util.List;

/**
 * Buran.
 *
 * @author: ${USER} Date: 03.07.13 Time: 22:10
 */
public class GetInClassEdgeResult implements Serializable {
    public static class Entry implements Serializable {
        private final INid source;
        private final LabelIndex labelIndex;

        public Entry(INid source, LabelIndex labelIndex) {
            this.source = source;
            this.labelIndex = labelIndex;
        }

        public INid getSource() {
            return source;
        }

        public LabelIndex getLabelIndex() {
            return labelIndex;
        }

        @Override
        public String toString() {
            return "Entry{" +
                    "source=" + source +
                    ", labelIndex=" + labelIndex +
                    '}';
        }
    }

    public GetInClassEdgeResult(List<Entry> entries) {
        this.entries = entries;
    }

    public List<Entry> entries;

    /**
     * Sorted by label index.
     *
     * @return
     */
    public List<Entry> getEntries() {
        return entries;
    }

    @Override
    public String toString() {
        return "GetInClassEdgeResult{" +
                "entries=" + entries +
                '}';
    }
}
