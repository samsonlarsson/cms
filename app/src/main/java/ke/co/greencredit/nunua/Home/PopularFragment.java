package ke.co.greencredit.nunua.Home;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import ke.co.greencredit.nunua.General.CheckConnection;
import ke.co.greencredit.nunua.General.Const;
import ke.co.greencredit.nunua.General.JSONHandler;
import ke.co.greencredit.nunua.General.ServiceHandler;
import ke.co.greencredit.nunua.R;
import ke.co.greencredit.nunua.StoreNavigation.Store;

/**
 * Created by Fab on 5/9/2016.
 */
public class PopularFragment extends Fragment {

    RecyclerView popularList;
    Context mContext;

    List<PopularItem> popularItems;
    HomeAdapter homeAdapter;
    ProgressDialog progressDialog;
    JSONHandler jsonHandler;
    ServiceHandler serviceHandler;
    SharedPreferences settings;
    String uuid, url;
    List<Store> arrayList;

    public PopularFragment() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.popular_fragment, container, false);
        popularList = (RecyclerView) view.findViewById(R.id.homeList);
        popularList.setLayoutManager(new LinearLayoutManager(mContext));

        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        popularItems = new ArrayList<>();
        jsonHandler = new JSONHandler();
        serviceHandler = new ServiceHandler();

        settings = mContext.getSharedPreferences(Const.PREFS_NAME, Context.MODE_PRIVATE);
        uuid = settings.getString("uuid", "");
        CheckConnection checkConnection = new CheckConnection(mContext);
        settings.getBoolean("logged", false);

        if ((getActivity() != null) && checkConnection.isConnected() && settings.getBoolean("logged", false)) {

            new FetchMainScreenDetails().execute(uuid);

        } else {
            Toast.makeText(mContext, "Service not available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private class FetchMainScreenDetails extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(getActivity(), "Home", "Loading products..", true, true);

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            if (s != null)
                if (!s.equals("")) {
                    //json handler
                    Log.d("JSON", s);
                    popularItems = jsonHandler.handlePopularItems(s);

                    homeAdapter = new HomeAdapter(popularItems, mContext);


                    if (popularItems.size() == 0) {
                        Toast.makeText(mContext, "No items", Toast.LENGTH_SHORT).show();
                    } else {
                        popularList.setAdapter(homeAdapter);
                    }
                }
        }

        @Override
        protected String doInBackground(String... strings) {

//            url = mAppConstants.baseURL + "Products/popular";
//            Log.d("log Message>>>", url);
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("uid", strings[0]));


            String resp = serviceHandler.makeServiceCall(Const.URL_API + "Products/popular", 2, nameValuePairs);
            return resp;
        }
    }


}
