package ke.co.greencredit.nunua.General;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Erick Genius on 7/2/2014.
 */
public class CheckConnection {
    Context context;

    public CheckConnection(Context context) {
        this.context = context;
    }

    public boolean isConnected() {
        ConnectivityManager conManager = (ConnectivityManager) context.getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conManager.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected())
            return true;
        else
            return false;

    }
}