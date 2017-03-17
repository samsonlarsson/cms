package ke.co.greencredit.nunua.Home;

import java.util.List;

import ke.co.greencredit.nunua.Product.Product;

/**
 * Created by eorenge on 5/5/16.
 */
public class PopularItem {
    String StoreId;
    String StoreName;
    List<Product> StorePopularItems;


    public PopularItem(String storeId, String storeName, List<Product> storePopularItems) {
        StoreId = storeId;
        StoreName = storeName;
        StorePopularItems = storePopularItems;
    }

    public String getStoreId() {
        return StoreId;
    }

    public void setStoreId(String storeId) {
        StoreId = storeId;
    }

    public String getStoreName() {
        return StoreName;
    }

    public void setStoreName(String storeName) {
        StoreName = storeName;
    }

    public List<Product> getStorePopularItems() {
        return StorePopularItems;
    }

    public void setStorePopularItems(List<Product> storePopularItems) {
        StorePopularItems = storePopularItems;
    }
}
