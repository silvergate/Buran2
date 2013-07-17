package com.dcrux.buran.common.classDefinition;

import com.dcrux.buran.common.fields.FieldIndex;
import com.dcrux.buran.common.fields.typeDef.ITypeDef;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Buran.
 *
 * @author: ${USER} Date: 02.07.13 Time: 19:35
 */
public class ClassFieldsDefinition implements Serializable {

    public static class FieldEntry implements Serializable {
        private ITypeDef typeDef;
        private boolean required;

        public FieldEntry(ITypeDef typeDef, boolean required) {
            this.typeDef = typeDef;
            this.required = required;
        }

        private FieldEntry() {
        }

        public ITypeDef getTypeDef() {
            return typeDef;
        }

        public boolean isRequired() {
            return required;
        }
    }

    private final Map<FieldIndex, FieldEntry> fieldEntries = new HashMap<FieldIndex, FieldEntry>();

    public ClassFieldsDefinition add(FieldIndex index, ITypeDef def, boolean required) {
        this.fieldEntries.put(index, new FieldEntry(def, required));
        return this;
    }

    public Map<FieldIndex, FieldEntry> getFieldEntries() {
        return fieldEntries;
    }

    public Set<FieldIndex> getFieldIndexes() {
        return this.fieldEntries.keySet();
    }
}
