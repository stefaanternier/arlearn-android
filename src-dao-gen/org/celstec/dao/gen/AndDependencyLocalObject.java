package org.celstec.dao.gen;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
import daoBase.DaoConfiguration;
import org.celstec.arlearn2.beans.dependencies.AndDependency;
import org.celstec.arlearn2.beans.dependencies.Dependency;
// KEEP INCLUDES END
/**
 * Entity mapped to table AND_DEPENDENCY_LOCAL_OBJECT.
 */
public class AndDependencyLocalObject extends DependencyLocalObject  {

    private Long dummyProperty;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public AndDependencyLocalObject() {
    }

    public AndDependencyLocalObject(Long dummyProperty) {
        this.dummyProperty = dummyProperty;
    }

    public Long getDummyProperty() {
        return dummyProperty;
    }

    public void setDummyProperty(Long dummyProperty) {
        this.dummyProperty = dummyProperty;
    }

    // KEEP METHODS - put your custom methods here
    public AndDependencyLocalObject(AndDependency dependencyBean) {
        DaoConfiguration.getInstance().getDependencyLocalObjectDao().insertOrReplace(this);
        for (Dependency dep: dependencyBean.getDependencies()){
            DependencyLocalObject localObject = DependencyLocalObject.createDependencyLocalObject(dep);
            localObject.setParentDependency(this.getId());
            DaoConfiguration.getInstance().getDependencyLocalObjectDao().insertOrReplace(localObject);
        }
    }

    public String toString() {
        String retString = "[and: ";
        for (DependencyLocalObject dependencyLocalObject: this.getChildDeps()) {
            retString += dependencyLocalObject.toString();
        }
        return retString+"]";
    }

    public void recursiveDelete() {
        for (DependencyLocalObject dependencyLocalObject: this.getChildDeps()) {
            dependencyLocalObject.recursiveDelete();
        }
        this.delete();
    }
    // KEEP METHODS END

}
