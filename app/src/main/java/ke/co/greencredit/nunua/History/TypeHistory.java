package ke.co.greencredit.nunua.History;

/**
 * Created by root on 10/12/15.
 */
public class TypeHistory {
    String name, quantity, storeName;

    public TypeHistory(String name, String quantity, String storeName) {
        this.name = name;
        this.quantity = quantity;
        this.storeName = storeName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }
}
