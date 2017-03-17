package ke.co.greencredit.nunua.Cart;

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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import ke.co.greencredit.nunua.Checkout.Checkout;
import ke.co.greencredit.nunua.General.AppConstants;
import ke.co.greencredit.nunua.General.CheckConnection;
import ke.co.greencredit.nunua.General.Const;
import ke.co.greencredit.nunua.General.DataManager;
import ke.co.greencredit.nunua.General.ImageLoader;
import ke.co.greencredit.nunua.General.JSONHandler;
import ke.co.greencredit.nunua.General.ServiceHandler;
import ke.co.greencredit.nunua.Home.Main;
import ke.co.greencredit.nunua.R;

public class Cart extends AppCompatActivity implements View.OnClickListener {
    ListView cartItems;
    TextView stepIndicator;
    android.support.v7.widget.Toolbar toolbar;
    ImageLoader imageLoader;
    AppConstants mAppConstants = new AppConstants();
    DataManager dataManager;
    Context  mContext;
    ProgressDialog progressDialog;
    SharedPreferences cartPrefs;
    JSONHandler jsonHandler;
    ServiceHandler serviceHandler;
    TextView tvSubTotals;
    String cartId;
    CheckConnection checkConnection;
    Button btnCheckOut;
    SharedPreferences sharedPreferences;
    AlertDialog.Builder builder1;

    Intent intent;
    int price = 0;
    String uuid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        mContext = getApplicationContext();
        sharedPreferences = getSharedPreferences(AppConstants.PREFS_NAME, Context.MODE_PRIVATE);
        builder1 = new AlertDialog.Builder(this);
        dataManager = new DataManager(mContext,1,"");
        imageLoader = new ImageLoader(mContext);
        progressDialog = new ProgressDialog(this);
        serviceHandler = new ServiceHandler();
        cartPrefs = getSharedPreferences(AppConstants.PREFS_NAME, MODE_PRIVATE);
        jsonHandler = new JSONHandler();
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        restoreActionBar();
        checkConnection = new CheckConnection(mContext);

        //Display text on the app bar
        stepIndicator = (TextView) toolbar.findViewById(R.id.txtStep);
        stepIndicator.setText("Cart");

        cartItems = (ListView) findViewById(R.id.cartItems);
        tvSubTotals= (TextView)findViewById(R.id.subtotal);
        btnCheckOut = (Button)findViewById(R.id.btnCheckOut);
        btnCheckOut.setOnClickListener(this);
        cartId = cartPrefs.getString("cartuuid", "");
        uuid = cartPrefs.getString("uuid", "");
        populateCart();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnCheckOut:
                if (dataManager.getItemsCount(cartId) == 0) {
                    sharedPreferences.edit().putBoolean("CartActive", false).apply();
                }
                    if(cartPrefs.getBoolean("CartActive",false)){

                        String cartid = cartPrefs.getString("cartuuid", "");
                        String json = dataManager.getCartJSON(cartid).toString();
                        String total = dataManager.getCartBP(cartid)+"";

                        if(checkConnection.isConnected()){
                            if(sharedPreferences.getBoolean("CartActive", false)) {

                                new CartSubmit().execute(uuid, json, total);
                            }else{
                                Toast.makeText(mContext,"No cart to submit",Toast.LENGTH_SHORT).show();
                            }

                        }else {
                            Toast.makeText(getApplicationContext(),"Check data connection.",Toast.LENGTH_SHORT).show();
                        }
                    }else{

                        Toast.makeText(getApplicationContext(),"No active cart",Toast.LENGTH_SHORT).show();
                    }
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setHomeAsUpIndicator(R.mipmap.left);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    public void populateCart(){
        if(sharedPreferences.getBoolean("CartActive", false)){
            CartAdapter cartAdapter = new CartAdapter(getApplicationContext(), Cart.this,tvSubTotals);
            cartItems.setAdapter(cartAdapter);
            //here

            price = dataManager.getCartBP(cartId);//price

            String totalPrice = "Total: ksh " + price + ".00";
            tvSubTotals.setText(totalPrice);

        }
        else{
            Toast.makeText(mContext,"No active cart",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cart, menu);
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
    private class CartSubmit extends AsyncTask<String,String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(progressDialog != null){
                progressDialog.setTitle("Cart Checkout");
                progressDialog.setMessage("Please wait as we make the order for you...");
                progressDialog.setCancelable(false);
                progressDialog.show();
            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(progressDialog != null){
                progressDialog.dismiss();
            }
            if(!s.equals(null))
                if(!s.equals("")){

                    String resp = jsonHandler.HandleRespCart(s,mContext);

                    if(resp == null){
                        Toast.makeText(getApplicationContext(), "Error submitting the cart.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    switch (resp){
                        case "cartactive":
                            buildDialog("Cart","There is already an active cart. Check it out?",0);
                            break;
                        case "delivery_on":
                            buildDialog2("Delivery in progress","Please wait until the order is COMPLETED to make another order.",0);
                            break;

                        default:
                            dataManager.deleteCart(cartId);

                            cartPrefs.edit().putInt("PrevCartStatus",1).apply();
                            cartPrefs.edit().putString("PaymentRef", resp).apply();

                            cartPrefs.edit().putInt("cartprice", price).apply();

                            Toast.makeText(getApplicationContext(), "Cart Success: Payment reference: " + resp, Toast.LENGTH_SHORT).show();

                            intent = new Intent(Cart.this, Checkout.class);
                            startActivity(intent);
                            sharedPreferences.edit().putBoolean("CartActive",false).apply();
                            finish();
                            break;
                    }

                }
        }


        @Override
        protected String doInBackground(String... strings) {
            String json = URLEncoder.encode(strings[1]);

            String finalU = Const.URL_API + "Cart/insert?uid=" + strings[0] + "&products=" + json + "&total=" + strings[2];
            Log.d("log message", finalU);
            String resp = serviceHandler.makeServiceCall(finalU,1);
            return resp;
        }
        void buildDialog2(String title, String message, int type) {
            switch (type) {
                case 0:
                    builder1.setMessage(message);
                    builder1.setTitle(title);
                    builder1.setCancelable(true);
                    builder1.setPositiveButton("Go back",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent mainIntent = new Intent(Cart.this, Main.class);
                                    startActivity(mainIntent);
                                    dialog.cancel();
                                    finish();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();

                    break;
                default:
                    break;
            }

        }
        void buildDialog(String title, String message, int type) {
            switch (type) {
                case 0:
                    builder1.setMessage(message);
                    builder1.setTitle(title);
                    builder1.setCancelable(true);
                    builder1.setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent mainIntent = new Intent(Cart.this, Checkout.class);
                                    startActivity(mainIntent);
                                    dialog.cancel();
                                    finish();
                                }
                            });
                    builder1.setNegativeButton("No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //dismiss cart and resubmit
                                    String servercartid = cartPrefs.getString("servercartid","");
                                    new CancelCart().execute(servercartid);
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();

                    break;
                default:
                    break;
            }

        }
    }



    private class CancelCart extends AsyncTask<String,String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(progressDialog != null){
                progressDialog.setTitle("Cart");
                progressDialog.setMessage("Cancelling previous active cart...");
                progressDialog.setCancelable(false);
                progressDialog.show();
            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(progressDialog != null){
                progressDialog.dismiss();
            }
            if(!s.equals(null))
                if(!s.equals("")){

                    String resp = jsonHandler.HandleResp(s);

                    if(resp.equals("false")){//not matching
                        Toast.makeText(getApplicationContext(), "Could not cancel active cart", Toast.LENGTH_SHORT).show();
                    }else{
                        //resubmit cart

                        if(cartPrefs.getBoolean("CartActive",false)){
                            String cartid = cartPrefs.getString("cartuuid", "");
                            String json = dataManager.getCartJSON(cartid).toString();
                            String total = dataManager.getCartBP(cartid)+"";

                            if(checkConnection.isConnected()){
                                if(sharedPreferences.getBoolean("CartActive", false)) {
                                    new CartSubmit().execute(uuid, json, total);
                                }else{
                                    Toast.makeText(mContext,"No cart to submit",Toast.LENGTH_SHORT).show();
                                }

                            }else {
                                Toast.makeText(getApplicationContext(),"Check data connection.",Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(getApplicationContext(),"No active cart",Toast.LENGTH_SHORT).show();
                        }

                    }
                }
        }


        @Override
        protected String doInBackground(String... strings) {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("uid", uuid));
            nameValuePairs.add(new BasicNameValuePair("cartId", strings[0]));

            String finalU = Const.URL_API + "Cart/invalidate";
            Log.d("log message", finalU);
            String resp = serviceHandler.makeServiceCall(finalU,2,nameValuePairs);
            Log.d("log invalidate ", resp);
            return resp;
        }
        void buildDialog(String title, String message, int type) {
            switch (type) {
                case 0:
                    builder1.setMessage(message);
                    builder1.setTitle(title);
                    builder1.setCancelable(true);
                    builder1.setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent mainIntent = new Intent(Cart.this, Checkout.class);
                                    startActivity(mainIntent);
                                    dialog.cancel();
                                    finish();
                                }
                            });
                    builder1.setNegativeButton("No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //dismiss cart and resubmit

                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();

                    break;
                default:
                    break;
            }

        }
    }
}
