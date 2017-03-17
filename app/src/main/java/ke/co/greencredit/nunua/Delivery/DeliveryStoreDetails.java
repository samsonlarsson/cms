package ke.co.greencredit.nunua.Delivery;

import java.util.ArrayList;

/**
 * Created by eorenge on 5/17/16.
 */
public class DeliveryStoreDetails {
    String storeId, StoreName;
    ArrayList<item_delivery> arrayPendigItems;

    public DeliveryStoreDetails(String storeId, String storeName, ArrayList<item_delivery> arrayPendigItems) {
        this.storeId = storeId;
        StoreName = storeName;
        this.arrayPendigItems = arrayPendigItems;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return StoreName;
    }

    public void setStoreName(String storeName) {
        StoreName = storeName;
    }

    public ArrayList<item_delivery> getArrayPendigItems() {
        return arrayPendigItems;
    }

    public void setArrayPendigItems(ArrayList<item_delivery> arrayPendigItems) {
        this.arrayPendigItems = arrayPendigItems;
    }
}
