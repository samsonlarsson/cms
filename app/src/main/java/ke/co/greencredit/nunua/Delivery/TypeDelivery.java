package ke.co.greencredit.nunua.Delivery;

import java.util.List;

/**
 * Created by root on 10/17/15.
 */
public class TypeDelivery {
    String orderID, amount, pendingAmount, paymentMethod, time, date;
    List<DeliveryStoreDetails> deliveryStoreDetails;

    public TypeDelivery(String orderID, String amount, String pendingAmount, String paymentMethod, String time, String date, List<DeliveryStoreDetails> deliveryStoreDetails) {
        this.orderID = orderID;
        this.amount = amount;
        this.pendingAmount = pendingAmount;
        this.paymentMethod = paymentMethod;
        this.time = time;
        this.date = date;
        this.deliveryStoreDetails = deliveryStoreDetails;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPendingAmount() {
        return pendingAmount;
    }

    public void setPendingAmount(String pendingAmount) {
        this.pendingAmount = pendingAmount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<DeliveryStoreDetails> getDeliveryStoreDetails() {
        return deliveryStoreDetails;
    }

    public void setDeliveryStoreDetails(List<DeliveryStoreDetails> deliveryStoreDetails) {
        this.deliveryStoreDetails = deliveryStoreDetails;
    }
}
