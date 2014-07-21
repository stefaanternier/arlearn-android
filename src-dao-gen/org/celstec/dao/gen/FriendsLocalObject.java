package org.celstec.dao.gen;

import org.celstec.dao.gen.DaoSession;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table FRIENDS_LOCAL_OBJECT.
 */
public class FriendsLocalObject {

    private Long id;
    private String name;
    private byte[] icon;
    private String accountIdAsString;
    private Long accountId;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient FriendsLocalObjectDao myDao;

    private AccountLocalObject accountLocalObject;
    private Long accountLocalObject__resolvedKey;


    // KEEP FIELDS - put your custom fields here

    private boolean dirty = false;

    private boolean wasChanged = false;

    public boolean isDirty() {
        return dirty;
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    public boolean isWasChanged() {
        return wasChanged;
    }

    public void setWasChanged(boolean wasChanged) {
        this.wasChanged = wasChanged;
    }
    // KEEP FIELDS END

    public FriendsLocalObject() {
    }

    public FriendsLocalObject(Long id) {
        this.id = id;
    }

    public FriendsLocalObject(Long id, String name, byte[] icon, String accountIdAsString, Long accountId) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.accountIdAsString = accountIdAsString;
        this.accountId = accountId;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getFriendsLocalObjectDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getIcon() {
        return icon;
    }

    public void setIcon(byte[] icon) {
        this.icon = icon;
    }

    public String getAccountIdAsString() {
        return accountIdAsString;
    }

    public void setAccountIdAsString(String accountIdAsString) {
        this.accountIdAsString = accountIdAsString;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    /** To-one relationship, resolved on first access. */
    public AccountLocalObject getAccountLocalObject() {
        Long __key = this.accountId;
        if (accountLocalObject__resolvedKey == null || !accountLocalObject__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            AccountLocalObjectDao targetDao = daoSession.getAccountLocalObjectDao();
            AccountLocalObject accountLocalObjectNew = targetDao.load(__key);
            synchronized (this) {
                accountLocalObject = accountLocalObjectNew;
            	accountLocalObject__resolvedKey = __key;
            }
        }
        return accountLocalObject;
    }

    public void setAccountLocalObject(AccountLocalObject accountLocalObject) {
        synchronized (this) {
            this.accountLocalObject = accountLocalObject;
            accountId = accountLocalObject == null ? null : accountLocalObject.getId();
            accountLocalObject__resolvedKey = accountId;
        }
    }

    /** Convenient call for {@link AbstractDao#delete(Object)}. Entity must attached to an entity context. */
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.delete(this);
    }

    /** Convenient call for {@link AbstractDao#update(Object)}. Entity must attached to an entity context. */
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.update(this);
    }

    /** Convenient call for {@link AbstractDao#refresh(Object)}. Entity must attached to an entity context. */
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.refresh(this);
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}
