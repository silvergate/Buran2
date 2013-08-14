package com.dcrux.buran.refimpl.modules.domains;

import com.dcrux.buran.commands.domains.DomDefineResult;
import com.dcrux.buran.common.domain.DomainDef;
import com.dcrux.buran.common.domain.DomainHashId;
import com.dcrux.buran.common.domain.DomainId;
import com.dcrux.buran.refimpl.modules.BaseModule;
import com.dcrux.buran.refimpl.modules.common.Module;
import com.dcrux.buran.refimpl.modules.orientUtils.IRunner;
import com.dcrux.buran.refimpl.modules.orientUtils.ITransRet;
import com.dcrux.buran.refimpl.modules.orientUtils.ITransaction;
import com.google.common.base.Optional;
import com.orientechnologies.orient.core.db.document.ODatabaseDocument;
import com.orientechnologies.orient.core.db.record.OIdentifiable;
import com.orientechnologies.orient.core.id.ORID;
import com.orientechnologies.orient.core.index.OIndex;
import com.orientechnologies.orient.core.record.impl.ODocument;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.SerializationUtils;
import org.elasticsearch.common.UUID;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.Random;

/**
 * Buran.
 *
 * @author: ${USER} Date: 13.08.13 Time: 22:24
 */
public class DomainsModule extends Module<BaseModule> {

    private final Random random = new Random();
    public static final int NUM_OF_RETRIES = 10;

    public DomainsModule(BaseModule baseModule) {
        super(baseModule);
    }

    public void setupDb() {
        DomainWrapper.setupDb(getBase());
    }

    private byte[] calcHash(byte[] bytes) {
        final MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(bytes);
            return hash;
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }

    private DomainHashId calcStr(byte[] bytes) {
        final String str = Base64.encodeBase64String(bytes);
        return new DomainHashId(str);
    }

    public DomainHashId getHash(DomainDef domainDef) {
        final UUID uuid = UUID.fromString(domainDef.getUuid());
        final byte[] data = SerializationUtils.serialize(domainDef);
        final byte[] calcHash = calcHash(data);
        return calcStr(calcHash);
    }

    public boolean exists(DomainId domainId) {
        final OIndex index =
                getBase().getDbUtils().getIndex(DomainWrapper.ORIENT_CLASS, DomainWrapper.IDX_ID);
        final Object value = index.getDefinition().createValue(domainId.getId());
        final Collection<OIdentifiable> entries = index.getValuesBetween(value, value);
        if ((entries == null) || (entries.isEmpty())) {
            return false;
        }
        return true;
    }

    public Optional<DomainHashId> getHashById(DomainId domainId) {
        final OIndex index =
                getBase().getDbUtils().getIndex(DomainWrapper.ORIENT_CLASS, DomainWrapper.IDX_ID);
        final Object value = index.getDefinition().createValue(domainId.getId());
        final Collection<OIdentifiable> entries = index.getValuesBetween(value, value);
        if ((entries == null) || (entries.isEmpty())) {
            return Optional.absent();
        }
        final ODocument doc = getBase().getDb().load((ORID) entries.iterator().next());
        final DomainWrapper domainWrapper = new DomainWrapper(doc);
        return Optional.of(domainWrapper.getDomainHashId());
    }

    public Optional<DomainId> getIdByHash(DomainHashId domainHashId) {
        final OIndex index =
                getBase().getDbUtils().getIndex(DomainWrapper.ORIENT_CLASS, DomainWrapper.IDX_HASH);
        final Object value = index.getDefinition().createValue(domainHashId.getId());
        final Collection<OIdentifiable> entries = index.getValuesBetween(value, value);
        if ((entries == null) || (entries.isEmpty())) {
            return Optional.absent();
        }
        final ODocument doc = getBase().getDb().load((ORID) entries.iterator().next());
        final DomainWrapper domainWrapper = new DomainWrapper(doc);
        return Optional.of(domainWrapper.getDomainId());
    }

    public DomainId createDomain() throws Exception {
        for (int i = 0; i < NUM_OF_RETRIES; i++) {
            final DomainId newDomainId = new DomainId(this.random.nextLong());
            try {
                getBase().getDbUtils().run(new ITransaction() {
                    @Override
                    public void run(ODatabaseDocument db, IRunner runner) throws Throwable {
                        final DomainWrapper domainWrapper = DomainWrapper.create(newDomainId);
                        domainWrapper.getDocument().save();
                    }
                });
                /* OK, everything is ok */
                return newDomainId;
            } catch (Exception exception) {
                /* Next try */
            }
        }
        throw new IllegalArgumentException("No free domain ID found");
    }

    public DomDefineResult defineDomain(DomainDef domainDef) throws Exception {
        final DomainHashId domainHashId = getHash(domainDef);
        for (int i = 0; i < NUM_OF_RETRIES; i++) {
            Optional<DomainId> domainIdOpt =
                    getBase().getDbUtils().run(new ITransRet<Optional<DomainId>>() {
                        @Override
                        public Optional<DomainId> run(ODatabaseDocument db, IRunner runner)
                                throws Throwable {
                            return getIdByHash(domainHashId);
                        }
                    });

            if (domainIdOpt.isPresent()) {
                /* Alles in ornung, ist bereits definiert */
                return new DomDefineResult(domainIdOpt.get(), domainHashId);
            }

            /* Not yet defined, generate new ID */
            final DomainId newDomainId = new DomainId(this.random.nextLong());
            try {
                getBase().getDbUtils().run(new ITransaction() {
                    @Override
                    public void run(ODatabaseDocument db, IRunner runner) throws Throwable {
                        final DomainWrapper domainWrapper =
                                DomainWrapper.create(domainHashId, newDomainId);
                        domainWrapper.getDocument().save();
                    }
                });
                /* OK, everything is ok */
                return new DomDefineResult(newDomainId, domainHashId);
            } catch (Exception exception) {
                /* Next try */
            }
        }
        throw new IllegalArgumentException("No free domain ID found");
    }


}
