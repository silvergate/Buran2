package com.dcrux.buran.common.subscription;

import com.dcrux.buran.common.classDefinition.ClassIndexName;
import com.dcrux.buran.common.classes.ClassId;
import com.dcrux.buran.common.indexing.keyGen.IKeyGen;

import java.io.Serializable;

/**
 * Buran.
 *
 * @author: ${USER} Date: 17.07.13 Time: 01:22
 */
public class SubDefinition implements Serializable {
    private ClassId classId;
    private ClassIndexName classIndexName;
    private IKeyGen keyGen;

    public SubDefinition(ClassId classId, ClassIndexName classIndexName, IKeyGen keyGen) {
        this.classId = classId;
        this.classIndexName = classIndexName;
        this.keyGen = keyGen;
    }

    private SubDefinition() {
    }

    public ClassId getClassId() {
        return classId;
    }

    public ClassIndexName getClassIndexName() {
        return classIndexName;
    }

    public IKeyGen getKeyGen() {
        return keyGen;
    }
}
