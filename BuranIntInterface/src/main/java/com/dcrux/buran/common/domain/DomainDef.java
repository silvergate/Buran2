package com.dcrux.buran.common.domain;

import java.io.Serializable;

/**
 * Buran.
 *
 * @author: ${USER} Date: 13.08.13 Time: 21:49
 */
public class DomainDef implements Serializable {
    private String title;
    private String description;
    private String uuid;
    private String creatorMail;

    public static final int TITLE_MIN_LEN = 5;
    public static final int TITLE_MAX_LEN = 32;

    public static final int DESCRIPTION_MIN_LEN = 20;
    public static final int DESCRIPTION_MAX_LEN = 128;

    public static final int UUID_LEN = 36;

    public static final int CREATOR_MAIL_MIN_LEN = 7;
    public static final int CREATOR_MAIL_MAX_LEN = 82;

    public DomainDef(String title, String description, String uuid, String creatorMail) {
        if ((title.length() < TITLE_MIN_LEN) || (title.length() > TITLE_MAX_LEN)) {
            throw new IllegalArgumentException(
                    "(title.length()<TITLE_MIN_LEN) || (title.length()>TITLE_MAX_LEN)");
        }
        if ((description.length() < DESCRIPTION_MIN_LEN) ||
                (description.length() > DESCRIPTION_MAX_LEN)) {
            throw new IllegalArgumentException("(description.length()<DESCRIPTION_MIN_LEN) || " +
                    "(description.length()\n" +
                    "                >DESCRIPTION_MAX_LEN)");
        }
        if ((uuid.length() != UUID_LEN)) {
            throw new IllegalArgumentException("uuid.length()!=UUID_LEN");
        }
        if ((creatorMail.length() < CREATOR_MAIL_MIN_LEN) ||
                (creatorMail.length() > CREATOR_MAIL_MAX_LEN)) {
            throw new IllegalArgumentException("(creatorMail.length()<CREATOR_MAIL_MIN_LEN) || " +
                    "(creatorMail.length()\n" +
                    "                >CREATOR_MAIL_MAX_LEN)");
        }

        this.title = title;
        this.description = description;
        this.uuid = uuid;
        this.creatorMail = creatorMail;
    }

    private DomainDef() {
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getUuid() {
        return uuid;
    }

    public String getCreatorMail() {
        return creatorMail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DomainDef domainDef = (DomainDef) o;

        if (!creatorMail.equals(domainDef.creatorMail)) return false;
        if (!description.equals(domainDef.description)) return false;
        if (!title.equals(domainDef.title)) return false;
        if (!uuid.equals(domainDef.uuid)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = title.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + uuid.hashCode();
        result = 31 * result + creatorMail.hashCode();
        return result;
    }
}
