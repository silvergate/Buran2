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
public class ComQuery extends Command<IQueryResult> {

    private final ResultDirection resultDirection = ResultDirection.normal;
    private final LimitConfig limitConfig = new LimitConfig(true, Integer.MAX_VALUE);

    private final ClassId classId;
    private final ClassIndexName classIndexName;
    private final IKeyGen keyGen;

    public ComQuery(ClassId classId, ClassIndexName classIndexName, IKeyGen keyGen) {
        super(exceptions());
        this.classId = classId;
        this.classIndexName = classIndexName;
        this.keyGen = keyGen;
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
}
