package org.celstec.dao.gen;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
import daoBase.DaoConfiguration;
import org.celstec.arlearn2.beans.dependencies.Dependency;
import org.celstec.arlearn2.beans.dependencies.OrDependency;
// KEEP INCLUDES END

/**
 * Entity mapped to table OR_DEPENDENCY_LOCAL_OBJECT.
 */
public class OrDependencyLocalObject extends DependencyLocalObject  {

    private Long dummyProperty;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public OrDependencyLocalObject() {
    }

    public OrDependencyLocalObject(Long dummyProperty) {
        this.dummyProperty = dummyProperty;
    }



    public Long getDummyProperty() {
        return dummyProperty;
    }

    public void setDummyProperty(Long dummyProperty) {
        this.dummyProperty = dummyProperty;
    }

    // KEEP METHODS - put your custom methods here

    public OrDependencyLocalObject(OrDependency dependencyBean) {
        DaoConfiguration.getInstance().getDependencyLocalObjectDao().insertOrReplace(this);
        for (Dependency dep: dependencyBean.getDependencies()){
            DependencyLocalObject localObject = DependencyLocalObject.createDependencyLocalObject(dep);
            localObject.setParentDependency(this.getId());
            DaoConfiguration.getInstance().getDependencyLocalObjectDao().insertOrReplace(localObject);
        }
    }

    public String toString() {
        String retString = "[or: ";
        for (DependencyLocalObject dependencyLocalObject: this.getChildDeps()) {
            retString += dependencyLocalObject.toString();
        }
        return retString+"]";
    }
    // KEEP METHODS END

}
