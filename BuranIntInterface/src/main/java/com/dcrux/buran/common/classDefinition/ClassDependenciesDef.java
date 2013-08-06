package com.dcrux.buran.common.classDefinition;

import com.dcrux.buran.common.classes.ClassHashId;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Buran.
 *
 * @author: ${USER} Date: 06.08.13 Time: 21:55
 */
public class ClassDependenciesDef implements Serializable {

    private Map<DependencyIndex, ClassHashId> classHashIdMap =
            new HashMap<DependencyIndex, ClassHashId>();

    public Set<DependencyIndex> getDependencies() {
        return this.classHashIdMap.keySet();
    }

    @Nullable
    public ClassHashId getDependency(DependencyIndex dependencyIndex) {
        return this.classHashIdMap.get(dependencyIndex);
    }

    public void add(DependencyIndex dependencyIndex, ClassHashId classHashId) {
        this.classHashIdMap.put(dependencyIndex, classHashId);
    }

}
