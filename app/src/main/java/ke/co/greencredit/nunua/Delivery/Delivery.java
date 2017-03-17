package ke.co.greencredit.nunua.Delivery;

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
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ke.co.greencredit.nunua.General.AppConstants;
import ke.co.greencredit.nunua.General.Const;
import ke.co.greencredit.nunua.General.JSONHandler;
import ke.co.greencredit.nunua.General.ServiceHandler;
import ke.co.greencredit.nunua.Home.Main;
import ke.co.greencredit.nunua.R;

public class Delivery extends AppCompatActivity implements View.OnClickListener{
    Toolbar toolbar;
    TextView stepIndicator;
    ListView itemList;
    ProgressDialog progressDialog;
    JSONHandler jsonHandler;
    AppConstants mAppConstants = new AppConstants();
    ServiceHandler serviceHandler= new ServiceHandler();
    Context context;
    String uuid,orderId;
    SharedPreferences sharedPreferences;
    TextView tvDeliveryID, tvDate,tvTime,tvAmount,tvPendingAmount;
    //    Button aButton;
    AlertDialog.Builder builder1;


    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);
        builder1 = new AlertDialog.Builder(this);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        progressDialog = new ProgressDialog(this);
        jsonHandler = new JSONHandler();
        context = getApplicationContext();
        stepIndicator = (TextView) findViewById(R.id.txtStep);
        stepIndicator.setText("Delivery");
        sharedPreferences = getSharedPreferences(AppConstants.PREFS_NAME, MODE_PRIVATE);

        tvDeliveryID = (TextView)findViewById(R.id.txtDeliveryID);
        tvDate = (TextView)findViewById(R.id.txtDate);
        tvTime = (TextView)findViewById(R.id.txtTime);
        tvAmount = (TextView)findViewById(R.id.txtAmount);
        tvPendingAmount = (TextView)findViewById(R.id.txtPendingAmount);

        expListView = (ExpandableListView) findViewById(R.id.totalItems);

        orderId = sharedPreferences.getString("orderId","");


        restoreActionBar();
        uuid  = sharedPreferences.getString("uuid","");

        new FetchDelivery().execute(uuid);
    }

    @Override
    public void onClick(View v) {

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
        getMenuInflater().inflate(R.menu.menu_delivery, menu);
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

    void buildDialog(String title, String message, int type) {
        switch (type) {
            case 0:

                builder1.setMessage(message);
                builder1.setTitle(title);
                builder1.setCancelable(true);
                builder1.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent = new Intent(Delivery.this, Main.class);
                                startActivity(intent);
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

    public void setListViewHeightBasedOnChildren(ExpandableListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = listView.getPaddingTop() + listView.getPaddingBottom();
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            if (listItem instanceof ViewGroup) {
                listItem.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            }
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = 30 + totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    private class FetchDelivery extends AsyncTask<String, String, String> {
        String url;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setTitle("Orders");
            progressDialog.setMessage("Loading pending order...");
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            if (s != null)
                //TODO
                if (!s.equals("")) {
                    //json handler

                    TypeDelivery itemsList = jsonHandler.HandleDelivery(s, context);

                    if(itemsList != null){


                        listAdapter = new ExpandableListAdapter(context, itemsList, expListView, Delivery.this);

                        tvDeliveryID.setText(itemsList.getOrderID());
                        tvTime.setText(itemsList.getTime());
                        tvDate.setText(itemsList.getDate());

                        NumberFormat numberFormat = new DecimalFormat("#,###.00");

                        tvAmount.setText("ksh. " + numberFormat.format(Integer.parseInt(itemsList.getAmount())) + "(" + itemsList.getPaymentMethod() + ")");
                        if (!itemsList.getPendingAmount().equals("null")) {
                            tvPendingAmount.setText("ksh. " + numberFormat.format(Integer.parseInt(itemsList.getPendingAmount())));
                        } else {
                            tvPendingAmount.setText("ksh. " + numberFormat.format(Integer.parseInt(itemsList.getAmount())));
                        }

                        expListView.setAdapter(listAdapter);
                        setListViewHeightBasedOnChildren(expListView);

                    }else{
                        buildDialog("Cart","No pending cart.",0);
                    }
                }
        }

        @Override
        protected String doInBackground(String... strings) {

            url = Const.URL_API + "Orders/get";
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("uid", strings[0]));
            Log.d("log Message>>>", url);
            String resp = serviceHandler.makeServiceCall(url, 2, nameValuePairs);
            Log.d("log Message>>>", resp);
            return resp;
        }
    }
}
