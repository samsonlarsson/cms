package ke.co.greencredit.nunua.StoreNavigation;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import ke.co.greencredit.nunua.General.AppConstants;
import ke.co.greencredit.nunua.General.CheckConnection;
import ke.co.greencredit.nunua.General.JSONHandler;
import ke.co.greencredit.nunua.General.ServiceHandler;
import ke.co.greencredit.nunua.R;

public class StoreListing extends AppCompatActivity {
    ListView storeListings;
    android.support.v7.widget.Toolbar toolbar;
    TextView appBarText;

    JSONHandler jsonHandler;
    ServiceHandler serviceHandler;
    CheckConnection checkConnection;
    ProgressDialog progressDialog = null;
    Context appContext;

    AppConstants mAppConstants = new AppConstants();
    String url;
    SharedPreferences sharedPreferences;

    ArrayList<Store> arrayList;
    String uuid;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_listing);
        appContext = getApplicationContext();
        checkConnection = new CheckConnection(appContext);
        progressDialog = new ProgressDialog(StoreListing.this);
        serviceHandler = new ServiceHandler();
        jsonHandler = new JSONHandler();
        storeListings = (ListView) findViewById(R.id.store_listings);
        sharedPreferences = getSharedPreferences(mAppConstants.PREFS_NAME,Context.MODE_PRIVATE);
        uuid = sharedPreferences.getString("uuid", "");



        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        restoreActionBar();

        appBarText = (TextView) toolbar.findViewById(R.id.txtStep);
        appBarText.setText("Listed Stores");
//        after this asynchtask asign


        if(checkConnection.isConnected()){

            new LoadStores().execute(uuid);
        }else{
            Toast.makeText(appContext,"Check connection",Toast.LENGTH_SHORT).show();
        }
    }


    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setHomeAsUpIndicator(R.mipmap.left);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_store_listing, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class LoadStores extends AsyncTask<String,String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setTitle("Stores");
            progressDialog.setMessage("Loading available stores. ...");
            progressDialog.setCancelable(true);
            progressDialog.show();


//            Toast.makeText(getApplicationContext(), "Fetching stores...", Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
//            Toast.makeText(appContext,"here", Toast.LENGTH_SHORT);

            if(!s.equals(null))
                if(!s.equals("")){
                    arrayList = jsonHandler.HandleStores(s,appContext);
                     if(arrayList.size() == 0){//not matching
                        Toast.makeText(appContext,"Could not load stores.",Toast.LENGTH_SHORT).show();
                    }else{

                        StoreListingAdapter storeListingAdapter = new StoreListingAdapter(getApplicationContext(),arrayList);
                        storeListings.setAdapter(storeListingAdapter);
                    }
                }
        }

        @Override
        protected String doInBackground(String... strings) {
            String param0 = URLEncoder.encode(strings[0]);
//            String param1 = URLEncoder.encode(strings[1]);

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("uid", param0));



            url = mAppConstants.baseURL + "Stores/get";
            Log.d("log Message>>>", url);
            String resp = serviceHandler.makeServiceCall(url,2,nameValuePairs);
            Log.d("log Message>>>", resp);
            return resp;
        }
    }

}
