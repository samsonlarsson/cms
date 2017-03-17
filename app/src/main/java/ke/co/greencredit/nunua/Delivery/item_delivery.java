package ke.co.greencredit.nunua.Delivery;

/**
 * Created by root on 10/17/15.
 */
public class item_delivery {
    String productName, productQuantity, unitMeasure, unitPrice, unitQuantity;

    public item_delivery(String productName, String productQuantity, String unitMeasure, String unitPrice, String unitQuantity) {
        this.productName = productName;
        this.productQuantity = productQuantity;
        this.unitMeasure = unitMeasure;
        this.unitPrice = unitPrice;
        this.unitQuantity = unitQuantity;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(String productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getUnitMeasure() {
        return unitMeasure;
    }

    public void setUnitMeasure(String unitMeasure) {
        this.unitMeasure = unitMeasure;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getUnitQuantity() {
        return unitQuantity;
    }

    public void setUnitQuantity(String unitQuantity) {
        this.unitQuantity = unitQuantity;
    }
}
