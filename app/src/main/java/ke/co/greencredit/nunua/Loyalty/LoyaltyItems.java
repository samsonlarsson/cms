package ke.co.greencredit.nunua.Loyalty;

import java.util.ArrayList;

/**
 * Created by Fab on 11/1/2015.
 */
public class LoyaltyItems {
    ArrayList<SimpleProduct> loyaltyProducts;
    public String date;
    public int points;

    public LoyaltyItems(ArrayList<SimpleProduct> loyaltyProducts, String date, int points) {
        this.loyaltyProducts = loyaltyProducts;
        this.date = date;
        this.points = points;
    }

    public ArrayList<SimpleProduct> getLoyaltyProducts() {
        return loyaltyProducts;
    }

    public void setLoyaltyProducts(ArrayList<SimpleProduct> loyaltyProducts) {
        this.loyaltyProducts = loyaltyProducts;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
