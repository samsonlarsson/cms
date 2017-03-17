package ke.co.greencredit.nunua.Checkout;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import ke.co.greencredit.nunua.General.AppConstants;
import ke.co.greencredit.nunua.General.Const;
import ke.co.greencredit.nunua.General.JSONHandler;
import ke.co.greencredit.nunua.General.ServiceHandler;
import ke.co.greencredit.nunua.Home.Main;
import ke.co.greencredit.nunua.R;

public class CashOnDelivery extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private static final String[] COUNTRIES = new String[]{"Belgium", "France", "Italy", "Germany", "Spain"};
    private static final String LOG_TAG = "Log Message";
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";
    //------------ make your specific key ------------
    private static final String API_KEY = "AIzaSyCZAc7IDAi_Ek1Reh0YIYhqZhd-3uf9S0A";
    Toolbar toolbar;
    TextView stepIndicator;
    TimePicker etTime;
    AutoCompleteTextView etLocation;
    SharedPreferences sharedPreferences;
    TextView defaultlocation,subtotal;
    AppConstants appConstants = new AppConstants();
    Button btnProceed;
    ProgressDialog progressDialog;
    JSONHandler jsonHandler;
    Context mContext;
    ServiceHandler serviceHandler;
    AlertDialog.Builder builder1;
    Intent intent;
    String cartUuid;
    AutoCompleteTextView completeTextView;
    Boolean FLAG_CONTINUE = false;
    int price;

    public static ArrayList<String> autocomplete(String input) {
        ArrayList<String> resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);

            sb.append("?components=country:ke");
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));
            sb.append("&key=" + API_KEY);
            sb.append("&sensor=true");
            URL url = new URL(sb.toString());

            System.out.println("URL: " + url);
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error processing Places API URL", e);
            return resultList;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to Places API", e);
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {

            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            Log.d("Log message", jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            // Extract the Place descriptions from the results
            resultList = new ArrayList<String>(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                System.out.println(predsJsonArray.getJSONObject(i).getString("description"));
                System.out.println("============================================================");
                resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Cannot process JSON results", e);
        }

        return resultList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_on_delivery);
        sharedPreferences = getSharedPreferences(appConstants.PREFS_NAME, MODE_PRIVATE);
        serviceHandler = new ServiceHandler();

        mContext = getApplicationContext();
        builder1 = new AlertDialog.Builder(this);



        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        etLocation = (AutoCompleteTextView)findViewById(R.id.edit_location);

        etLocation.setAdapter(new GooglePlacesAutocompleteAdapter(this, R.layout.list_item));
        etLocation.setOnItemClickListener(this);

        etTime = (TimePicker)findViewById(R.id.edit_time);
        subtotal = (TextView)findViewById(R.id.subtotal);
        defaultlocation = (TextView)findViewById(R.id.tvLocation);
        progressDialog = new ProgressDialog(this);
        jsonHandler  = new JSONHandler();

        price = sharedPreferences.getInt("cartprice",0);
        subtotal.setText("Ksh. "+price+".00");
        String loca = sharedPreferences.getString("location","");
        defaultlocation.setText(loca+"(default)");
        etTime = (TimePicker)findViewById(R.id.edit_time);
        etTime.setIs24HourView(true);
        btnProceed = (Button)findViewById(R.id.btnProceed);
        btnProceed.setOnClickListener(this);
        stepIndicator = (TextView) findViewById(R.id.txtStep);
        stepIndicator.setText("Cash On Delivery");

        restoreActionBar();


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case  R.id.btnProceed:
                String refID = sharedPreferences.getString("PaymentRef","");
                Log.d("paymentref>>",refID);
                String uuid = sharedPreferences.getString("uuid", "");
                String location = etLocation.getText().toString();

//                String time = etTime.get().toString();

                int h = etTime.getCurrentHour();
                int m = etTime.getCurrentMinute();
                if(h > 18){
                    FLAG_CONTINUE  = false;
                    buildDialog("Time","Deliveries can only be made within our operational hours.",1);
                    return;
                } else{
                    FLAG_CONTINUE = true;
                }







                String time = convertToTime(h,m);
                if(location.equals("")){
                    Toast.makeText(mContext,"Location missing",Toast.LENGTH_SHORT).show();
                    return;
                }else if(time.equals("")){
                    Toast.makeText(mContext,"Time missing",Toast.LENGTH_SHORT).show();
                    return;
                }
                new CompleteOrder().execute(uuid,refID,location,time);
                break;
        }
    }

    public String convertToTime(int hour, int min){
        String time = hour +":"+min + "AM";
        return time;
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
        getMenuInflater().inflate(R.menu.menu_checkout, menu);
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

    void buildDialog(String title, String message,int type){
        builder1.setMessage(message);
        builder1.setTitle(title);
        builder1.setCancelable(true);
        AlertDialog alert11;
        switch (type){

            case 0:
                builder1.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent1 = new Intent(CashOnDelivery.this, Main.class);
                                startActivity(intent1);
                                finish();
                            }
                        });

                alert11 = builder1.create();
                alert11.show();

                break;
            case 1:
                builder1.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                alert11 = builder1.create();
                alert11.show();
                break;

            default:
                break;
        }


    }

    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        String str = (String) adapterView.getItemAtPosition(position);
//        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    private class CompleteOrder extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (progressDialog != null) {
                progressDialog.setTitle("Transaction");
                progressDialog.setMessage("Completing transaction please wait...");
                progressDialog.setCancelable(false);
                progressDialog.show();
            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            if (!s.equals(null))
                if (!s.equals("")) {

                    String resp = jsonHandler.HandleResp(s);

                    if (resp.equals("false")) {//not matching
                        Toast.makeText(getApplicationContext(), "Error submitting the cart.", Toast.LENGTH_SHORT).show();
                    } else {

                        sharedPreferences.edit().putString("cartat", "").apply();
                        sharedPreferences.edit().putBoolean("CartActive", false).apply();
                        sharedPreferences.edit().putString("cartuuid", "").apply();

                        buildDialog("Cart", "Cart payment completed. Admin will contact you", 0);


//                        buildDialog("Cart", "There is already an active cart. Check it out?", 0);

                    }
                }
        }


        @Override
        protected String doInBackground(String... strings) {
            String json = URLEncoder.encode(strings[1]);

            String finalU = Const.URL_API + "Pay/cashOnDelivery?uid=" + strings[0] + "&cartId=" + strings[1] + "&location=" + URLEncoder.encode(strings[2]) + "&time=" + strings[3];
//            String finalU = appConstants.baseURL + "Pay/checkStatus?uid=" + strings[0] + "&cartId=" + strings[1]+"&location="+strings[2]+"&time="+strings[3];
            Log.d("log message", finalU);
            String resp = serviceHandler.makeServiceCall(finalU, 1);
            Log.d("log message", resp);

            return resp;
        }//PaymentRef

    }

    class GooglePlacesAutocompleteAdapter extends ArrayAdapter<String> implements Filterable {
        private ArrayList<String> resultList;

        public GooglePlacesAutocompleteAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public String getItem(int index) {
            return resultList.get(index);
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        // Retrieve the autocomplete results.
                        resultList = autocomplete(constraint.toString());

                        // Assign the data to the FilterResults
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }
    }

    }
