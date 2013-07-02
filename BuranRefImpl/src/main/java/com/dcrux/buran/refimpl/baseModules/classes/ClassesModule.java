package com.dcrux.buran.refimpl.baseModules.classes;

import com.dcrux.buran.common.classDefinition.ClassDefinition;
import com.dcrux.buran.common.classes.ClassHashId;
import com.dcrux.buran.common.classes.ClassId;
import com.dcrux.buran.common.exceptions.NodeClassNotFoundException;
import com.dcrux.buran.refimpl.baseModules.BaseModule;
import com.dcrux.buran.refimpl.baseModules.common.Module;
import com.dcrux.buran.refimpl.baseModules.nodeWrapper.ClassNameUtils;
import com.google.common.base.Optional;
import com.orientechnologies.orient.core.db.record.OIdentifiable;
import com.orientechnologies.orient.core.id.ORID;
import com.orientechnologies.orient.core.index.OIndex;
import com.orientechnologies.orient.core.metadata.schema.OSchema;
import com.orientechnologies.orient.core.record.impl.ODocument;
import org.apache.commons.lang3.SerializationUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;

/**
 * Buran.
 *
 * @author: ${USER} Date: 29.06.13 Time: 16:05
 */
public class ClassesModule extends Module<BaseModule> {

    public ClassesModule(BaseModule baseModule) {
        super(baseModule);
    }

    public void setupDb() {
        ClassDefWrapper.assureClass(getBase().getDb().getMetadata().getSchema());
    }

    public void createOrientClassIfNonExistent(ClassId classId) {
        if (existsOrientClass(classId)) return;
        final String className = ClassNameUtils.generateNodeClasName(classId);
        System.out.println("Adding class: '" + className + "'");
        final OSchema schema = getBase().getDb().getMetadata().getSchema();
        schema.createClass(className);
        schema.save();
    }

    public void assureOrientClass(ClassId classId) throws NodeClassNotFoundException {
        boolean exists = existsOrientClass(classId);
        if (!exists) {
            throw new NodeClassNotFoundException();
        }
    }

    public boolean existsOrientClass(ClassId classId) {
        final String className = ClassNameUtils.generateNodeClasName(classId);
        boolean exists = getBase().getDb().getMetadata().getSchema().existsClass(className);
        System.out.println("Exists class: '" + className + "', : " + exists);
        return exists;
    }

    public Optional<ClassId> getClassIdByClassHash(final ClassHashId classHashId) {
        final OIndex index = getBase().getDbUtils()
                .getIndex(ClassDefWrapper.CLASS_NAME, ClassDefWrapper.INDEX_HASH);
        final Object value = index.getDefinition().createValue(new Object[]{classHashId.getHash()});
        final Collection<OIdentifiable> entries = index.getValuesBetween(value, value);
        if ((entries == null) || (entries.isEmpty())) {
            return Optional.absent();
        }
        final ODocument doc = getBase().getDb().load((ORID) entries.iterator().next());
        final ClassDefWrapper classDefWrapper = new ClassDefWrapper(doc);
        return Optional.of(classDefWrapper.getClassId());
    }

    public ClassDefWrapper getClassWrapperById(final ClassId classId)
            throws NodeClassNotFoundException {
        final OIndex index = getBase().getDbUtils()
                .getIndex(ClassDefWrapper.CLASS_NAME, ClassDefWrapper.INDEX_CID);
        final Object value = index.getDefinition().createValue(classId.getId());
        final Collection<OIdentifiable> entries = index.getValuesBetween(value, value);
        if ((entries == null) || (entries.isEmpty())) {
            throw new NodeClassNotFoundException();
        }
        final ODocument doc = getBase().getDb().load((ORID) entries.iterator().next());
        final ClassDefWrapper classDefWrapper = new ClassDefWrapper(doc);
        return classDefWrapper;
    }

    public ClassHashId getClassHashIdById(ClassId classId) throws NodeClassNotFoundException {
        final ClassDefWrapper classWrapper = getClassWrapperById(classId);
        return classWrapper.getClassHashId();
    }

    private byte[] calcHash(ClassDefinition classDefinition) {
        byte[] ser = SerializationUtils.serialize(classDefinition);
        final MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(ser);
            return hash;
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }

    public ClassId declareClass(ClassDefinition classDefinition) throws Exception {
        final byte[] hashByte = calcHash(classDefinition);
        final ClassHashId classHashId = new ClassHashId(hashByte);
        final Optional<ClassId> classIdOpt = getClassIdByClassHash(classHashId);
        if (classIdOpt.isPresent()) {
            return classIdOpt.get();
        }

        /* Class is not yet declared. Declare now. */

        Optional<Exception> exception = null;
        ClassId classId = null;
        final long classIdLong = getBase().getRandom().nextLong();
        classId = new ClassId(classIdLong);
        final ClassDefWrapper classDefWrapper = ClassDefWrapper.c(classId, classHashId);
        classDefWrapper.getDocument().save();



        /*
        exception = getBase().getDbUtils().commitAndReBegin();
            if (exception.isPresent() && (!(exception.get() instanceof
                    ORecordDuplicatedException))) {
                throw exception.get();
            }*/


        return classId;
    }
}
