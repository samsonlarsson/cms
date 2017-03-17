package ke.co.greencredit.nunua.Loyalty;

/**
 * Created by root on 11/28/15.
 */
public class SimpleProduct {
    String name;
    int quantity;

    public SimpleProduct(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }
}
