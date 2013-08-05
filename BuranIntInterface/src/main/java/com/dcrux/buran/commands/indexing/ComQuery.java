package com.dcrux.buran.commands.indexing;

import com.dcrux.buran.commands.Command;
import com.dcrux.buran.common.classDefinition.ClassIndexName;
import com.dcrux.buran.common.classes.ClassId;
import com.dcrux.buran.common.indexing.keyGen.IKeyGen;

/**
 * Buran.
 *
 * @author: ${USER} Date: 09.07.13 Time: 18:07
 */
public class ComQuery extends Command<QueryResult> {

    private ResultDirection resultDirection = ResultDirection.normal;
    private LimitConfig limitConfig = new LimitConfig(true, Integer.MAX_VALUE);

    private ClassId classId;
    private ClassIndexName classIndexName;
    private IKeyGen keyGen;

    private ComQuery() {
    }

    public ComQuery(ClassId classId, ClassIndexName classIndexName, IKeyGen keyGen) {
        super(exceptions());
        this.classId = classId;
        this.classIndexName = classIndexName;
        this.keyGen = keyGen;
    }

    public static ComQuery c(ClassId classId, ClassIndexName classIndexName, IKeyGen keyGen) {
        return new ComQuery(classId, classIndexName, keyGen);
    }

    public IKeyGen getKeyGen() {
        return keyGen;
    }

    public ClassId getClassId() {
        return classId;
    }

    public ClassIndexName getClassIndexName() {
        return classIndexName;
    }

    public LimitConfig getLimitConfig() {
        return limitConfig;
    }
}
