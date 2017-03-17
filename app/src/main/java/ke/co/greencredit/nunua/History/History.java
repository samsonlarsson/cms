package ke.co.greencredit.nunua.History;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import ke.co.greencredit.nunua.General.AppConstants;
import ke.co.greencredit.nunua.General.CheckConnection;
import ke.co.greencredit.nunua.General.Const;
import ke.co.greencredit.nunua.General.JSONHandler;
import ke.co.greencredit.nunua.General.ServiceHandler;
import ke.co.greencredit.nunua.R;

public class History extends AppCompatActivity {
    Toolbar toolbar;
    ListView historyList;
    TextView stepIndicator;
    HistoryAdapter historyAdapter;
    ProgressDialog progressDialog;
    Context appContext;
    JSONHandler jsonHandler;
    ServiceHandler serviceHandler;
    AppConstants mAppConstants = new AppConstants();
    String uuid;
    SharedPreferences sharedPreferences;
    CheckConnection checkConnection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        checkConnection = new CheckConnection(getApplicationContext());
        restoreActionBar();
        sharedPreferences = getSharedPreferences(AppConstants.PREFS_NAME, MODE_PRIVATE);
        progressDialog  = new ProgressDialog(this);
        appContext = getApplicationContext();
        jsonHandler = new JSONHandler();
        serviceHandler = new ServiceHandler();
        uuid = sharedPreferences.getString("uuid","");
        if(checkConnection.isConnected()){
            new LoadHistory().execute(uuid);
        }else{
            Toast.makeText(getApplicationContext(),"Check data connection.",Toast.LENGTH_SHORT).show();
        }
        stepIndicator = (TextView) findViewById(R.id.txtStep);
        stepIndicator.setText("History");
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
        getMenuInflater().inflate(R.menu.menu_history, menu);
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

    private class LoadHistory extends AsyncTask<String,String, String> {
        String url;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setTitle("History");
            progressDialog.setMessage("Fetching order history...");
            progressDialog.setCancelable(true);
            progressDialog.show();

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            if(!s.equals(null))
                if(!s.equals("")){
                    ArrayList<TypeCartHistory> histories = jsonHandler.HandleOrderHistory(s);
                    if(histories == null){
                        Toast.makeText(appContext,"No order history to show",Toast.LENGTH_SHORT).show();

                        return;
                    }
                    if(histories.size() == 0){
                        Toast.makeText(appContext,"No order history",Toast.LENGTH_SHORT).show();
                    }else{
                        historyAdapter = new HistoryAdapter(appContext, histories);
                        historyList = (ListView) findViewById(R.id.historyList);
                        historyList.setAdapter(historyAdapter);
                    }
                }
        }

        @Override
        protected String doInBackground(String... strings) {

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("uid", strings[0]));

            url = Const.URL_API + "Orders/history";
            Log.d("log Message>>>", url);
            String resp = serviceHandler.makeServiceCall(url,2,nameValuePairs);
            return resp;
        }
    }
}
