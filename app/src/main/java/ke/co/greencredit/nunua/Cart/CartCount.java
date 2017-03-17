package ke.co.greencredit.nunua.Cart;

import android.content.Context;
import android.content.SharedPreferences;

import ke.co.greencredit.nunua.General.AppConstants;
import ke.co.greencredit.nunua.General.DataManager;

/**
 * Created by root on 9/27/15.
 */
public class CartCount {
    Context context;
    SharedPreferences sharedPreferences;
    AppConstants mAppConstants = new AppConstants();
    DataManager dataManager;
    String cartId;


    public CartCount(Context context) {
        this.context = context;
        dataManager = new DataManager(context,1,"");
        sharedPreferences = context.getSharedPreferences(mAppConstants.PREFS_NAME,Context.MODE_PRIVATE);
        cartId = sharedPreferences.getString("ActiveCartId","");
    }
    public int getCount(){
        return dataManager.getItemsCount(cartId);
    }
}
