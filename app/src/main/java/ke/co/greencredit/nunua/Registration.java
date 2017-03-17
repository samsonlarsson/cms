package ke.co.greencredit.nunua;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import ke.co.greencredit.nunua.General.AppConstants;
import ke.co.greencredit.nunua.General.Const;
import ke.co.greencredit.nunua.General.JSONHandler;
import ke.co.greencredit.nunua.General.ServiceHandler;

public class Registration extends AppCompatActivity implements View.OnClickListener {
    Toolbar toolbar;
    Button btnProceed;
    TextView stepIndicator;
    String bsName, kraPin, location, email, phoneNo, pass, pass2;
    EditText etBsName, etKraPin, etLocation, etEmail, etPhoneNo, etPass, etPass2;
    SharedPreferences sharedPreferences;
    AppConstants appConstants = new AppConstants();
    AlertDialog.Builder builder1;

    ProgressDialog progressDialog;
    JSONHandler jsonHandler = new JSONHandler();
    ServiceHandler serviceHandler = new ServiceHandler();
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_registration);

        builder1 = new AlertDialog.Builder(this);
        progressDialog = new ProgressDialog(this);
        sharedPreferences = getSharedPreferences(AppConstants.PREFS_NAME, Context.MODE_PRIVATE);
        context = getApplicationContext();
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        stepIndicator = (TextView) findViewById(R.id.txtStep);
        stepIndicator.setText("Registration");

        etLocation = (EditText) findViewById(R.id.editKraPin);
        etEmail = (EditText) findViewById(R.id.editEmail);
        etPhoneNo = (EditText) findViewById(R.id.editPhoneNumber);
        etPass = (EditText) findViewById(R.id.editPass);
        etPass2 = (EditText) findViewById(R.id.editConfPass);
        etBsName = (EditText) findViewById(R.id.editBusName);
        etKraPin = (EditText) findViewById(R.id.editKraPin);


        restoreActionBar();


        //Proceed button setup
        btnProceed = (Button) findViewById(R.id.btnProceed);
        btnProceed.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnProceed:
                bsName = etBsName.getText().toString();
                kraPin = etKraPin.getText().toString();
                location = etLocation.getText().toString();
                email = etEmail.getText().toString();
                phoneNo = etPhoneNo.getText().toString();
                pass = etPass.getText().toString();
                pass2 = etPass2.getText().toString();

                if (kraPin.equals("") || bsName.equals("") || location.equals("") || email.equals("") || phoneNo.equals("") || pass.equals("")) {
                    Toast.makeText(context, "Missing details", Toast.LENGTH_SHORT).show();
                }
                if (pass.equals(pass2)) {
                    new RegistrationTrial().execute(location, email, kraPin, bsName, phoneNo, pass);
                } else {
                    Toast.makeText(context, "Passwords not matching.", Toast.LENGTH_SHORT).show();
                }
        }
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

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setHomeAsUpIndicator(R.mipmap.left);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_registration, menu);
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

    private class RegistrationTrial extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            if (progressDialog != null) {
                progressDialog.setTitle("Registration");
                progressDialog.setMessage("Registration in progress. Please wait...");
                progressDialog.setCancelable(false);
                //
                progressDialog.show();

            }


//            Toast.makeText(getApplicationContext(), "Sending data...", Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            Log.d("log message >> ", s);

            if (!s.equals(null))
                if (!s.equals("")) {
                    Log.d("log message>>>>", s);
                    String resp = jsonHandler.HandleResp(s);

                    if (resp.equals("false")) {//not matching
                        Toast.makeText(context, "An error occurred during registration", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Registration successful", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(getApplicationContext(), RegistrationDone.class);
                        startActivity(intent);
                    }
                }
        }


        @Override
        protected String doInBackground(String... strings) {
            String params[] = new String[strings.length];
            for (int i = 0; i < strings.length; i++) {
                params[i] = URLEncoder.encode(strings[i]);
            }
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("password", params[5]));


            String url = Const.URL_API + "Register/reg?location=" + params[0] + "&email=" + params[1]
                    + "&kra_pin=" + params[2] + "&business_name=" + params[3] + "&phoneno1=" + params[4];


            Log.e("log Message>>>", url);


            String resp = serviceHandler.makeServiceCall(url, 2, nameValuePairs);
            return resp;
        }
    }

}
