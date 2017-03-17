package ke.co.greencredit.nunua.History;

import java.util.ArrayList;

/**
 * Created by root on 10/29/15.
 */
public class TypeCartHistory {


    String cartId,total_amount,order_id,uid,order_status,amount_left;
    ArrayList<TypeHistory> typeHistories;

    public TypeCartHistory(String cartId, String total_amount, String order_id, String uid, String order_status, String amount_left, ArrayList<TypeHistory> typeHistories) {
        this.cartId = cartId;
        this.total_amount = total_amount;
        this.order_id = order_id;
        this.uid = uid;
        this.order_status = order_status;
        this.amount_left = amount_left;
        this.typeHistories = typeHistories;
    }

    public String getCartId() {
        return cartId;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public String getOrder_id() {
        return order_id;
    }

    public String getUid() {
        return uid;
    }

    public String getOrder_status() {
        return order_status;
    }

    public String getAmount_left() {
        return amount_left;
    }

    public ArrayList<TypeHistory> getTypeHistories() {
        return typeHistories;
    }
}
