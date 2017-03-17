package ke.co.greencredit.nunua;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ke.co.greencredit.nunua.General.AppConstants;
import ke.co.greencredit.nunua.General.CheckConnection;
import ke.co.greencredit.nunua.General.Const;
import ke.co.greencredit.nunua.General.JSONHandler;
import ke.co.greencredit.nunua.General.ServiceHandler;
import ke.co.greencredit.nunua.Home.Main;
import ke.co.greencredit.nunua.app.AppController;

public class LogIn extends AppCompatActivity implements View.OnClickListener{


    Button signIn;
    TextView btnRegister;
    String sUsername, sPassword,url;
    TextView tvUsename , tvPassword;
    JSONHandler jsonHandler;
    ServiceHandler serviceHandler;
    CheckConnection checkConnection;
    ProgressDialog progressDialog = null;

    Context appContext;
    AppConstants mAppConstants  = new AppConstants();
    SharedPreferences settings;
    AlertDialog.Builder builder1 ;
    Intent intent;

    //nfc
    NfcAdapter adapter;
    PendingIntent pendingIntent;
    IntentFilter writeTagFilters[];
    boolean writeMode;
    Tag mytag;
    SharedPreferences  sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        //nfc
        appContext = getApplicationContext();
        sharedPreferences = getSharedPreferences(mAppConstants.PREFS_NAME,MODE_PRIVATE);

//        NfcManager manager = (NfcManager) appContext.getSystemService(Context.NFC_SERVICE);
//        adapter = manager.getDefaultAdapter();
//        if (adapter != null && adapter.isEnabled()) {
//
//        }else{
//            Toast.makeText(appContext,"NFC not enabled",Toast.LENGTH_SHORT).show();
//        }

//        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
//        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
//        tagDetected.addCategory(Intent.CATEGORY_DEFAULT);
//        writeTagFilters = new IntentFilter[] { tagDetected };



        builder1  = new AlertDialog.Builder(this);

        checkConnection = new CheckConnection(appContext);
        progressDialog = new ProgressDialog(LogIn.this);
        settings = getSharedPreferences(mAppConstants.PREFS_NAME, Context.MODE_PRIVATE);

        //assign
        sUsername = "";
        sPassword = "";

        tvUsename = (TextView)findViewById(R.id.editText);
        tvPassword = (TextView)findViewById(R.id.editText2);

        btnRegister = (TextView)findViewById(R.id.txtRegister);
        btnRegister.setOnClickListener(this);
        signIn = (Button) findViewById(R.id.btnLogIn);
        btnRegister = (Button) findViewById(R.id.btnLogIn);
        signIn.setOnClickListener(this);
        serviceHandler = new ServiceHandler();
        jsonHandler = new JSONHandler();

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btnLogIn:
                sUsername = tvUsename.getText().toString();
                sPassword = tvPassword.getText().toString();

                if ((sUsername.equals("") || (sPassword.equals("")))) {
                    Toast.makeText(appContext, "Username or password missing", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (checkConnection.isConnected()) {

                    new loginTrial().execute(sUsername, sPassword);
//                    resqLoginObj(sUsername,sPassword);

                } else {
                    Toast.makeText(appContext, "Check your internet connection", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.txtRegister:
                int at = settings.getInt("regat",1);
                if(at != 1){
                    //previous registration instance
                    buildDialog("Registration","Do you want  to continue with your previous registration?",0 );


                }else{
                    settings.edit().putString("childjson","").apply();
                    intent= new Intent(getApplicationContext(), Registration.class);
                    startActivity(intent);
                }


                break;

        }
    }

    void buildDialog(String title, String message,int type){

        builder1.setMessage(message);
        builder1.setTitle(title);
        builder1.setCancelable(true);
        AlertDialog alert11;

        switch (type) {
            case 0:
                builder1.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                int at = settings.getInt("regat",1);

                                 switch (at){
                                case 1:
                                intent= new Intent(getApplicationContext(), Registration.class);
                                startActivity(intent);
                                break;
                                case 2:
                                intent= new Intent(getApplicationContext(), RegistrationStepTwo.class);
                                startActivity(intent);
                                break;
                                case 3:
                                intent= new Intent(getApplicationContext(), RegistrationStepThree.class);
                                startActivity(intent);
                                break;
                                case 4:
                                intent= new Intent(getApplicationContext(), RegistrationStepFour.class);
                                startActivity(intent);
                                break;

                            }
                                dialog.cancel();
                            }
                        });
                builder1.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
//                                toReturn = false;
                                intent = new Intent(getApplicationContext(), Registration.class);
                                startActivity(intent);
                                dialog.cancel();
                            }
                        });

                alert11 = builder1.create();
                alert11.show();

                break;
            case 1:
                builder1.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
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

    private void resqLoginObj(final String token, final String secret) {
//        showProgressDialog();
        progressDialog.setTitle("LogIn");
        progressDialog.setMessage("Please wait as we log you in...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                "http://0c868c0e.ngrok.io/login", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                Toast.makeText(appContext, response, Toast.LENGTH_SHORT).show();

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(appContext, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {

            /**
             * Passing some request headers
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", token);
                params.put("phoneNumber", "");
                params.put("idNumber", "");
                params.put("uid", "");
                params.put("password", secret);

                return params;
            }

        };
        ;

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
//                Const.URL_API, null,
                "http://0c868c0e.ngrok.io/login", null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        Toast.makeText(appContext, response.toString(), Toast.LENGTH_SHORT).show();
//                        Log.d(TAG, response.toString());
//                        msgResponse.setText(response.toString());
//                        hideProgressDialog();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
//                VolleyLog.d(TAG, "Error: " + error.getMessage());
//                hideProgressDialog();
                Toast.makeText(appContext, error.toString(), Toast.LENGTH_SHORT).show();

            }
        }) {

            /**
             * Passing some request headers
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", token);
                params.put("phoneNumber", "");
                params.put("idNumber", "");
                params.put("uid", "");
                params.put("password", secret);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq,
                "json");

        // Cancelling request
        // ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_obj);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_log_in, menu);
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

    @Override
    public void onBackPressed() {

    }

    private void write(String text, Tag tag) throws IOException, FormatException {

        NdefRecord[] records = {createRecord(text)};
        NdefMessage message = new NdefMessage(records);
        // Get an instance of Ndef for the tag.
        Ndef ndef = Ndef.get(tag);
        // Enable I/O
        if (!ndef.isConnected()) {
            ndef.connect();
        }
        // Write the message
        ndef.writeNdefMessage(message);
        // Close the connection
        ndef.close();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (adapter != null && adapter.isEnabled()) {
            WriteModeOff();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null && adapter.isEnabled()) {
            WriteModeOn();
        }
    }

    private void WriteModeOn() {
        writeMode = true;
        adapter.enableForegroundDispatch(this, pendingIntent, writeTagFilters, null);
    }

    private void WriteModeOff() {
        writeMode = false;
        adapter.disableForegroundDispatch(this);
    }

    public void readFromTag(Intent intent) {

        Ndef ndef = Ndef.get(mytag);


        try {
            if (!ndef.isConnected()) {
                ndef.connect();
            }


            Parcelable[] messages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

            if (messages != null) {
                NdefMessage[] ndefMessages = new NdefMessage[messages.length];
                for (int i = 0; i < messages.length; i++) {
                    ndefMessages[i] = (NdefMessage) messages[i];
                    NdefRecord record = ndefMessages[i].getRecords()[0];
                    byte[] payload = record.getPayload();
                    String text = new String(payload);

                    Log.d("NDF_LOG", text);
                }
                NdefRecord record = ndefMessages[0].getRecords()[0];
                byte[] payload = record.getPayload();
                String text = new String(payload);
                new NfcUUIDVerification().execute(text);
                ndef.close();

            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Cannot Read From Tag.", Toast.LENGTH_LONG).show();
        }
    }

    private NdefRecord createRecord(String text) throws UnsupportedEncodingException {
        String lang = "en";
        byte[] textBytes = text.getBytes();
        byte[] langBytes = lang.getBytes("US-ASCII");
        int langLength = langBytes.length;
        int textLength = textBytes.length;
        byte[] payload = new byte[1 + langLength + textLength];

        // set status byte (see NDEF spec for actual bits)
        payload[0] = (byte) langLength;

        // copy langbytes and textbytes into payload
        System.arraycopy(langBytes, 0, payload, 1, langLength);
        System.arraycopy(textBytes, 0, payload, 1 + langLength, textLength);

        NdefRecord recordNFC = new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], payload);

        return recordNFC;
    }

//    @Override
//    protected void onNewIntent(Intent intent){
//        return;
//        if(NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())){
//            mytag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
////			detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
//
//            readFromTag(intent);
//
//            Toast.makeText(this, "Tag Detected", Toast.LENGTH_SHORT).show();
//        }
//    }

    private class NfcUUIDVerification extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setTitle("NFC-Login");
            progressDialog.setMessage("Please wait as we log you in...");
            progressDialog.setCancelable(false);
            progressDialog.show();


            Toast.makeText(getApplicationContext(), "Checking details..", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            if (!s.equals(null))
                if (!s.equals("")) {
                    String resp = jsonHandler.HandleResp2(s, getApplicationContext());

                    if (resp.equals("false")) {//not matching
                        Toast.makeText(appContext, "Wrong token provided.", Toast.LENGTH_SHORT).show();
                    } else {

                        settings.edit().putString("uuid", resp).apply();
                        settings.edit().putBoolean("logged", true).apply();
                        Intent intent = new Intent(getApplicationContext(), Main.class);
                        startActivity(intent);
                    }
                }
        }

        @Override
        protected String doInBackground(String... strings) {

            String param0 = URLEncoder.encode(strings[0].trim());

            url = mAppConstants.baseURL + "Login/nfc/" + param0;
            Log.d("log Message>>>", url);
            Log.d("log Message>>>", strings[0]);
            String resp = serviceHandler.makeServiceCall(url, 1);
            return resp;
        }
    }

    private class loginTrial extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setTitle("LogIn");
            progressDialog.setMessage("Please wait as we log you in...");
            progressDialog.setCancelable(false);
            progressDialog.show();


//            Toast.makeText(getApplicationContext(), "Checking details..", Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            if (!s.equals(null))
                if (!s.equals("")) {
                    Log.d("Log message >>> ", s);
                    String resp = jsonHandler.HandleResp2(s, getApplicationContext());

                    if (resp.equals("false")) {//not matching
                        //replaced with dialog
//                        Toast.makeText(appContext,"Username or password is wrong.",Toast.LENGTH_SHORT).show();
                        buildDialog("LogIn", "Wrong email or password", 1);
                    } else {

                        settings.edit().putString("uuid", resp).apply();
                        settings.edit().putBoolean("logged", true).apply();
                        Intent intent = new Intent(getApplicationContext(), Main.class);
                        startActivity(intent);
                        finish();
                    }
                }
        }

        @Override
        protected String doInBackground(String... strings) {

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("email", strings[0]));
            nameValuePairs.add(new BasicNameValuePair("password", strings[1]));


            url = Const.URL_API + "Login/authenticate";
            Log.d("log Message>>>", url);
            String resp = serviceHandler.makeServiceCall(url, 2, nameValuePairs);
            return resp;
        }
    }
}