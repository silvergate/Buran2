package com.dcrux.buran.refimpl.baseModules.classes;

import com.dcrux.buran.common.UserId;
import com.dcrux.buran.common.classDefinition.ClassDefinition;
import com.dcrux.buran.common.classDefinition.ClassDependenciesDef;
import com.dcrux.buran.common.classDefinition.DependencyIndex;
import com.dcrux.buran.common.classes.ClassHashId;
import com.dcrux.buran.common.classes.ClassId;
import com.dcrux.buran.common.exceptions.NodeClassNotFoundException;
import com.dcrux.buran.refimpl.baseModules.BaseModule;
import com.dcrux.buran.refimpl.baseModules.common.Module;
import com.dcrux.buran.refimpl.baseModules.nodeWrapper.ClassNameUtils;
import com.dcrux.buran.refimpl.baseModules.orientUtils.IRunner;
import com.dcrux.buran.refimpl.baseModules.orientUtils.ITransRet;
import com.dcrux.buran.refimpl.baseModules.orientUtils.ITransaction;
import com.google.common.base.Optional;
import com.orientechnologies.orient.core.db.document.ODatabaseDocument;
import com.orientechnologies.orient.core.db.record.OIdentifiable;
import com.orientechnologies.orient.core.id.ORID;
import com.orientechnologies.orient.core.index.OIndex;
import com.orientechnologies.orient.core.metadata.schema.OSchema;
import com.orientechnologies.orient.core.record.impl.ODocument;
import org.apache.commons.lang3.SerializationUtils;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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
        //System.out.println("BoolEq class: '" + className + "', : " + exists);
        return exists;
    }

    public ClassDefExt getClassDefExtById(ClassId classId) throws NodeClassNotFoundException {
        //TODO: Diese methode wird sehr oft aufgerufen, einen cache machen dafür!
        final ClassDefWrapper wrapper = getClassWrapperById(classId);
        final byte[] definition = wrapper.getDef();
        final ClassDefExt classDef = (ClassDefExt) SerializationUtils.deserialize(definition);
        return classDef;
    }

    public ClassDefinition getClassDefById(ClassId classId) throws NodeClassNotFoundException {
        return getClassDefExtById(classId).getClassDefinition();
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

    private byte[] toBinary(Serializable classDefinition) {
        //TODO: Das muss irgendwann mal canonical werden
        return SerializationUtils.serialize(classDefinition);
    }

    private byte[] calcHash(byte[] classDefinitionBinary) {
        final MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(classDefinitionBinary);
            return hash;
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }

    public ClassId declareClass(ClassDefinition classDefinition) throws Exception {
        final byte[] classAsBinary = toBinary(classDefinition);
        final byte[] hashByte = calcHash(classAsBinary);
        final ClassHashId classHashId = new ClassHashId(hashByte);
        final Optional<ClassId> classIdOpt =
                getBase().getDbUtils().run(new ITransRet<Optional<ClassId>>() {
                    @Override
                    public Optional<ClassId> run(ODatabaseDocument db, IRunner runner)
                            throws Throwable {
                        return getClassIdByClassHash(classHashId);
                    }
                });
        if (classIdOpt.isPresent()) {
            return classIdOpt.get();
        }

        /* Find all dependencies */
        final Map<DependencyIndex, ClassId> classIdDependencies = new HashMap<>();
        final ClassDependenciesDef dependencies = classDefinition.getDependencies();
        for (final DependencyIndex depId : dependencies.getDependencies()) {
            final ClassHashId classHashIdDep = dependencies.getDependency(depId);
            final Optional<ClassId> depClassId =
                    getBase().getDbUtils().run(new ITransRet<Optional<ClassId>>() {
                        @Override
                        public Optional<ClassId> run(ODatabaseDocument db, IRunner runner)
                                throws Throwable {
                            return getClassIdByClassHash(classHashIdDep);
                        }
                    });
            if (!depClassId.isPresent()) {
                throw new IllegalArgumentException(MessageFormat.format("Dependency with hash " +
                        "{0} was not found. Dependencies have to be declared in advance.",
                        classHashIdDep));
            }
            classIdDependencies.put(depId, depClassId.get());
        }

        /* Class is not yet declared. Declare now. */

        final int maxNumOfRetries = 100;
        ClassDefWrapper classDefWrapper = null;
        /* Find a free classID */
        for (int i = 0; i < 100; i++) {
            try {
                classDefWrapper = getBase().getDbUtils().run(new ITransRet<ClassDefWrapper>() {
                    @Override
                    public ClassDefWrapper run(ODatabaseDocument db, IRunner runner)
                            throws Throwable {
                        final long classIdLong = getBase().getRandom().nextLong();
                        final ClassDefWrapper classDefWrapper =
                                ClassDefWrapper.c(new ClassId(classIdLong));
                        classDefWrapper.getDocument().save();
                        return classDefWrapper;
                    }
                });
            } catch (Exception ex) {
                /* Next try */
            }
            if (classDefWrapper != null) {
                break;
            }
        }
        if (classDefWrapper == null) {
            throw new IllegalStateException("No free class ID found");
        }

        ClassId classId = classDefWrapper.getClassId();

        /* Create information for storage */
        ClassDefExt classDefExt = new ClassDefExt(classDefinition, classIdDependencies);
        final byte[] completedBinary = toBinary(classDefExt);

        /* Create orientDB-classes */
        getBase().getClassesModule().createOrientClassIfNonExistent(classId);

        /* Got everything, store now complete class */
        final ClassDefWrapper classDefWrapperFinal = classDefWrapper;
        getBase().getDbUtils().run(new ITransaction() {
            @Override
            public void run(ODatabaseDocument db, IRunner runner) throws Throwable {
                classDefWrapperFinal.complete(classHashId, completedBinary);
                classDefWrapperFinal.getDocument().save();
            }
        });

        /* Say hello to the indexer */
        final UserId receiver = getBase().getAuthModule().getReceiver();
        getBase().getIndexingModule().prepareForIndexing(receiver, classId, classDefinition);

        return classId;
    }
}
