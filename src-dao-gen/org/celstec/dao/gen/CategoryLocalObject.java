package org.celstec.dao.gen;

import java.util.List;
import org.celstec.dao.gen.DaoSession;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table CATEGORY_LOCAL_OBJECT.
 */
public class CategoryLocalObject {

    private Long id;
    private String lang;
    private String category;
    private Boolean deleted;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient CategoryLocalObjectDao myDao;

    private List<GameCategoryLocalObject> games;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public CategoryLocalObject() {
    }

    public CategoryLocalObject(Long id) {
        this.id = id;
    }

    public CategoryLocalObject(Long id, String lang, String category, Boolean deleted) {
        this.id = id;
        this.lang = lang;
        this.category = category;
        this.deleted = deleted;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getCategoryLocalObjectDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    public List<GameCategoryLocalObject> getGames() {
        if (games == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            GameCategoryLocalObjectDao targetDao = daoSession.getGameCategoryLocalObjectDao();
            List<GameCategoryLocalObject> gamesNew = targetDao._queryCategoryLocalObject_Games(id);
            synchronized (this) {
                if(games == null) {
                    games = gamesNew;
                }
            }
        }
        return games;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    public synchronized void resetGames() {
        games = null;
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
