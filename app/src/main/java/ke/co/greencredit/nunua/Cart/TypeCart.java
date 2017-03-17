package ke.co.greencredit.nunua.Cart;


import ke.co.greencredit.nunua.Product.Product;

/**
 * Created by MyMSI on 9/18/2015.
 */
public class TypeCart {
    String id;
    String UUID;
    String status;
    Product cartItem;

    public TypeCart(String UUID, String status, Product cartItem) {
        this.UUID = UUID;
        this.status = status;
        this.cartItem = cartItem;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Product getCartItem() {
        return cartItem;
    }

    public void setCartItem(Product cartItem) {
        this.cartItem = cartItem;
    }
}
