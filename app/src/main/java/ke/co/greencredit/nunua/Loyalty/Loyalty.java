package ke.co.greencredit.nunua.Loyalty;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;

import ke.co.greencredit.nunua.General.AppConstants;
import ke.co.greencredit.nunua.General.CheckConnection;
import ke.co.greencredit.nunua.General.Const;
import ke.co.greencredit.nunua.General.JSONHandler;
import ke.co.greencredit.nunua.General.ServiceHandler;
import ke.co.greencredit.nunua.R;
import ke.co.greencredit.nunua.Redeem;

public class Loyalty extends AppCompatActivity {
    android.support.v7.widget.Toolbar toolbar;
    TextView title;
    ListView pointsList;
    Button redeem;
    ProgressDialog progressDialog;
    JSONHandler jsonHandler;
    AppConstants  mAppConstants = new AppConstants();
    ServiceHandler serviceHandler;
    LoyaltyAdapter loyaltyAdapter;
    ArrayList<LoyaltyItems> itemsList;
    String uuid;
    SharedPreferences  sharedPreferences;
    CheckConnection checkConnection;
    int totalPoints = 0;
    TextView tvTotalPoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loyalty);

        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        progressDialog = new ProgressDialog(this);
        serviceHandler = new ServiceHandler();
        jsonHandler = new JSONHandler();

        sharedPreferences = getSharedPreferences(AppConstants.PREFS_NAME, MODE_PRIVATE);
        checkConnection = new CheckConnection(getApplicationContext());
        uuid = sharedPreferences.getString("uuid","");

        title = (TextView) findViewById(R.id.txtStep);
        title.setText("Loyalty Points");

        tvTotalPoints = (TextView)findViewById(R.id.subtotal);

        pointsList = (ListView) findViewById(R.id.list_points);
        redeem = (Button) findViewById(R.id.btnRedeem);

        redeem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Loyalty.this, Redeem.class);
                startActivity(intent);
            }
        });
        restoreActionBar();

        setPoints();
    }

    public void setPoints(){
        if(checkConnection.isConnected()){
            new GetLoyalty().execute(uuid);
        }else{
            Toast.makeText(getApplicationContext(),"check data connection",Toast.LENGTH_SHORT).show();
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
        getMenuInflater().inflate(R.menu.menu_loyalty, menu);
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

    private class GetLoyalty extends AsyncTask<String, String, String> {
        String url;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setTitle("Nunua");
            progressDialog.setMessage("Loading loyalty points...");
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            if (s != null)
                if (!s.equals("")) {
                    //json handler
                    Log.d("JSON", s);
                   itemsList = jsonHandler.handleLoyaltyItems(s);

                    if(itemsList != null){

                        for(int i = 0 ; i < itemsList.size();i++){
                            totalPoints+=itemsList.get(i).getPoints();
                        }

                        DecimalFormat formatter = new DecimalFormat("#,###");
                        tvTotalPoints.setText(formatter.format(Integer.parseInt(totalPoints + "")));

                        if (itemsList.size() == 0) {
                            Toast.makeText(getApplicationContext(), "No items", Toast.LENGTH_SHORT).show();
                        } else {

                            loyaltyAdapter = new LoyaltyAdapter(getApplicationContext(),itemsList);
                            pointsList.setAdapter(loyaltyAdapter);

                        }
                    }else{
                        Toast.makeText(getApplicationContext(),"Could not fetch loyalty points",Toast.LENGTH_SHORT).show();
                    }

                }
        }

        @Override
        protected String doInBackground(String... strings) {

            url = Const.URL_API + "Loyalty/get?uid=" + strings[0];
            Log.d("log Message>>>", url);

            String resp = serviceHandler.makeServiceCall(url, 1);
            return resp;
        }
    }


    public class LoyaltyAdapter extends BaseAdapter {
        Context mContext;
        ArrayList<LoyaltyItems> ls;


        public LoyaltyAdapter(Context context,ArrayList<LoyaltyItems> lstItems) {
            mContext = context;
            ls = lstItems;

        }

        @Override
        public int getCount() {
            if(ls != null && ls.size() != 0){
                return ls.size();
            }else {
                return 0;
            }
        }

        @Override
        public Object getItem(int position) {
            return itemsList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LoyaltyItems loyaltyItems = itemsList.get(position);

            if(convertView == null){
                convertView = LayoutInflater.from(mContext).inflate(R.layout.points_cell_item, null);
            }
            TextView date = (TextView) convertView.findViewById(R.id.txtDate);
            date.setText(loyaltyItems.getDate());

            TextView points = (TextView) convertView.findViewById(R.id.txtPoints);

            points.setText(String.valueOf(loyaltyItems.getPoints()));
            
            //// TODO: 1/5/16 show products on click 

            return convertView;
        }
    }

}
