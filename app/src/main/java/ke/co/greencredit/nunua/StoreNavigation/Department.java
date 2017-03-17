package ke.co.greencredit.nunua.StoreNavigation;

/**
 * Created by Fab on 10/2/2015.
 */
public class Department implements java.io.Serializable{

    public String deptName,deptID,storeId;
    public String icon;

    public Department(String deptName, String deptID, String storeId, String icon) {
        this.icon = icon;
        this.deptName = deptName;
        this.deptID = deptID;
        this.storeId = storeId;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getDeptID() {
        return deptID;
    }

    public void setDeptID(String deptID) {
        this.deptID = deptID;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }
}
