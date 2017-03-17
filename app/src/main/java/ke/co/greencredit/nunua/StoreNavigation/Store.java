package ke.co.greencredit.nunua.StoreNavigation;

import java.util.ArrayList;

/**
 * Created by Fab on 10/1/2015.
 */
public class Store {
    String storeName,storeId;
    ArrayList<Department> departments;

    public String storeGraphic;

    public Store(String storeName, String storeId, String storeGraphic, ArrayList<Department> departments) {
        this.storeName = storeName;
        this.storeId = storeId;
        this.departments = departments;
        this.storeGraphic = storeGraphic;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public ArrayList<Department> getDepartments() {
        return departments;
    }

    public void setDepartments(ArrayList<Department> departments) {
        this.departments = departments;
    }

    public String getStoreGraphic() {
        return storeGraphic;
    }

    public void setStoreGraphic(String storeGraphic) {
        this.storeGraphic = storeGraphic;
    }
}
