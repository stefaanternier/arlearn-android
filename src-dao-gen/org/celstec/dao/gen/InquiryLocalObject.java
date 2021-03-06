package org.celstec.dao.gen;

import java.util.List;
import org.celstec.dao.gen.DaoSession;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table INQUIRY_LOCAL_OBJECT.
 */
public class InquiryLocalObject {

    private Long id;
    private String title;
    private String description;
    private String hypothesisTitle;
    private String hypothesisDescription;
    private String reflection;
    private Boolean isSynchronized;
    private byte[] icon;
    private long runId;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient InquiryLocalObjectDao myDao;

    private RunLocalObject runLocalObject;
    private Long runLocalObject__resolvedKey;

    private List<BadgeLocalObject> badges;
    private List<InquiryQuestionLocalObject> questions;
    private List<InquiryQuestionAnswerLocalObject> answers;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public InquiryLocalObject() {
    }

    public InquiryLocalObject(Long id) {
        this.id = id;
    }

    public InquiryLocalObject(Long id, String title, String description, String hypothesisTitle, String hypothesisDescription, String reflection, Boolean isSynchronized, byte[] icon, long runId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.hypothesisTitle = hypothesisTitle;
        this.hypothesisDescription = hypothesisDescription;
        this.reflection = reflection;
        this.isSynchronized = isSynchronized;
        this.icon = icon;
        this.runId = runId;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getInquiryLocalObjectDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHypothesisTitle() {
        return hypothesisTitle;
    }

    public void setHypothesisTitle(String hypothesisTitle) {
        this.hypothesisTitle = hypothesisTitle;
    }

    public String getHypothesisDescription() {
        return hypothesisDescription;
    }

    public void setHypothesisDescription(String hypothesisDescription) {
        this.hypothesisDescription = hypothesisDescription;
    }

    public String getReflection() {
        return reflection;
    }

    public void setReflection(String reflection) {
        this.reflection = reflection;
    }

    public Boolean getIsSynchronized() {
        return isSynchronized;
    }

    public void setIsSynchronized(Boolean isSynchronized) {
        this.isSynchronized = isSynchronized;
    }

    public byte[] getIcon() {
        return icon;
    }

    public void setIcon(byte[] icon) {
        this.icon = icon;
    }

    public long getRunId() {
        return runId;
    }

    public void setRunId(long runId) {
        this.runId = runId;
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

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    public List<BadgeLocalObject> getBadges() {
        if (badges == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            BadgeLocalObjectDao targetDao = daoSession.getBadgeLocalObjectDao();
            List<BadgeLocalObject> badgesNew = targetDao._queryInquiryLocalObject_Badges(id);
            synchronized (this) {
                if(badges == null) {
                    badges = badgesNew;
                }
            }
        }
        return badges;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    public synchronized void resetBadges() {
        badges = null;
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    public List<InquiryQuestionLocalObject> getQuestions() {
        if (questions == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            InquiryQuestionLocalObjectDao targetDao = daoSession.getInquiryQuestionLocalObjectDao();
            List<InquiryQuestionLocalObject> questionsNew = targetDao._queryInquiryLocalObject_Questions(id);
            synchronized (this) {
                if(questions == null) {
                    questions = questionsNew;
                }
            }
        }
        return questions;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    public synchronized void resetQuestions() {
        questions = null;
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    public List<InquiryQuestionAnswerLocalObject> getAnswers() {
        if (answers == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            InquiryQuestionAnswerLocalObjectDao targetDao = daoSession.getInquiryQuestionAnswerLocalObjectDao();
            List<InquiryQuestionAnswerLocalObject> answersNew = targetDao._queryInquiryLocalObject_Answers(id);
            synchronized (this) {
                if(answers == null) {
                    answers = answersNew;
                }
            }
        }
        return answers;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    public synchronized void resetAnswers() {
        answers = null;
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
