package ke.co.greencredit.nunua.Cart;

/**
 * Created by Fab on 10/2/2015.
 */
public class CartItem {

    public String pName;
    public String store;
    public String pPrice;
    public int pImg;
    public int pQuantity;

    public int getpQuantity() {
        return pQuantity;
    }

    public void setpQuantity(int pQuantity) {
        this.pQuantity = pQuantity;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public String getpPrice() {
        return pPrice;
    }

    public void setpPrice(String pPrice) {
        this.pPrice = pPrice;
    }

    public int getpImg() {
        return pImg;
    }

    public void setpImg(int pImg) {
        this.pImg = pImg;
    }
}
