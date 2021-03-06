package org.celstec.dao.gen;

import org.celstec.dao.gen.DaoSession;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
import org.celstec.arlearn2.android.delegators.AccountDelegator;
import org.celstec.arlearn2.beans.run.Action;
// KEEP INCLUDES END
/**
 * Entity mapped to table ACTION_LOCAL_OBJECT.
 */
public class ActionLocalObject {

    private Long id;
    /** Not-null value. */
    private String action;
    private String generalItemType;
    private Long time;
    private Boolean isSynchronized;
    private long runId;
    private Long generalItem;
    private long account;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient ActionLocalObjectDao myDao;

    private RunLocalObject runLocalObject;
    private Long runLocalObject__resolvedKey;

    private GeneralItemLocalObject generalItemLocalObject;
    private Long generalItemLocalObject__resolvedKey;

    private AccountLocalObject accountLocalObject;
    private Long accountLocalObject__resolvedKey;


    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public ActionLocalObject() {
    }

    public ActionLocalObject(Long id) {
        this.id = id;
    }

    public ActionLocalObject(Long id, String action, String generalItemType, Long time, Boolean isSynchronized, long runId, Long generalItem, long account) {
        this.id = id;
        this.action = action;
        this.generalItemType = generalItemType;
        this.time = time;
        this.isSynchronized = isSynchronized;
        this.runId = runId;
        this.generalItem = generalItem;
        this.account = account;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getActionLocalObjectDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /** Not-null value. */
    public String getAction() {
        return action;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setAction(String action) {
        this.action = action;
    }

    public String getGeneralItemType() {
        return generalItemType;
    }

    public void setGeneralItemType(String generalItemType) {
        this.generalItemType = generalItemType;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Boolean getIsSynchronized() {
        return isSynchronized;
    }

    public void setIsSynchronized(Boolean isSynchronized) {
        this.isSynchronized = isSynchronized;
    }

    public long getRunId() {
        return runId;
    }

    public void setRunId(long runId) {
        this.runId = runId;
    }

    public Long getGeneralItem() {
        return generalItem;
    }

    public void setGeneralItem(Long generalItem) {
        this.generalItem = generalItem;
    }

    public long getAccount() {
        return account;
    }

    public void setAccount(long account) {
        this.account = account;
    }

    /** To-one relationship, resolved on first access. */
    public RunLocalObject getRunLocalObject() {
        long __key = this.runId;
        if (runLocalObject__resolvedKey == null || !runLocalObject__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            RunLocalObjectDao targetDao = daoSession.getRunLocalObjectDao();
            RunLocalObject runLocalObjectNew = targetDao.load(__key);
            synchronized (this) {
                runLocalObject = runLocalObjectNew;
            	runLocalObject__resolvedKey = __key;
            }
        }
        return runLocalObject;
    }

    public void setRunLocalObject(RunLocalObject runLocalObject) {
        if (runLocalObject == null) {
            throw new DaoException("To-one property 'runId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.runLocalObject = runLocalObject;
            runId = runLocalObject.getId();
            runLocalObject__resolvedKey = runId;
        }
    }

    /** To-one relationship, resolved on first access. */
    public GeneralItemLocalObject getGeneralItemLocalObject() {
        Long __key = this.generalItem;
        if (generalItemLocalObject__resolvedKey == null || !generalItemLocalObject__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            GeneralItemLocalObjectDao targetDao = daoSession.getGeneralItemLocalObjectDao();
            GeneralItemLocalObject generalItemLocalObjectNew = targetDao.load(__key);
            synchronized (this) {
                generalItemLocalObject = generalItemLocalObjectNew;
            	generalItemLocalObject__resolvedKey = __key;
            }
        }
        return generalItemLocalObject;
    }

    public void setGeneralItemLocalObject(GeneralItemLocalObject generalItemLocalObject) {
        synchronized (this) {
            this.generalItemLocalObject = generalItemLocalObject;
            generalItem = generalItemLocalObject == null ? null : generalItemLocalObject.getId();
            generalItemLocalObject__resolvedKey = generalItem;
        }
    }

    /** To-one relationship, resolved on first access. */
    public AccountLocalObject getAccountLocalObject() {
        long __key = this.account;
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
        if (accountLocalObject == null) {
            throw new DaoException("To-one property 'account' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.accountLocalObject = accountLocalObject;
            account = accountLocalObject.getId();
            accountLocalObject__resolvedKey = account;
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

    public ActionLocalObject(Action bean) {
        setValuesFromBean(bean);
    }

    private void setValuesFromBean(Action bean) {
        setId(bean.getIdentifier());
        if (bean.getGeneralItemId() != null) setGeneralItem(bean.getGeneralItemId());
        if (bean.getAction() != null) setAction(bean.getAction());
        if (bean.getGeneralItemType() !=null) setGeneralItemType(bean.getGeneralItemType());
        if (bean.getRunId() != null) setRunId(bean.getRunId());
        if (bean.getTime() != null) setTime(bean.getTime());
        if (bean.getUserEmail() != null
                && AccountDelegator.getInstance().getAccount(bean.getUserEmail())!= null) setAccount(AccountDelegator.getInstance().getAccount(bean.getUserEmail()).getId());
    }

    public Action getActionBean() {
        Action actionBean = new Action();
        actionBean.setUserEmail(getAccountLocalObject().getFullId());
        actionBean.setTime(getTime());
        actionBean.setRunId(getRunId());
        actionBean.setAction(getAction());
        actionBean.setGeneralItemId(getGeneralItem());
        actionBean.setGeneralItemType(getGeneralItemType());

        return actionBean;
    }
    // KEEP METHODS END

}
