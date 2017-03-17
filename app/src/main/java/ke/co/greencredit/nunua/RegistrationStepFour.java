package ke.co.greencredit.nunua;

import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import ke.co.greencredit.nunua.General.AppConstants;
import ke.co.greencredit.nunua.General.Const;
import ke.co.greencredit.nunua.General.JSONHandler;
import ke.co.greencredit.nunua.General.ServiceHandler;

public class RegistrationStepFour extends AppCompatActivity implements View.OnClickListener{
    TextView stepIndicator;
    Button btnDone;
    Button btnAdd;
    ImageView backBtn;
    Toolbar toolbar;
    SharedPreferences sharedPreferences;
    AppConstants mAppConstants = new AppConstants();
    RadioButton rdMarried, rdSingle;
    String status;

    ProgressDialog progressDialog;
    JSONHandler jsonHandler;
    ServiceHandler serviceHandler;
    Context mAppContext;
    JSONArray jsonChildren;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_step_four);
        sharedPreferences = getSharedPreferences(mAppConstants.PREFS_NAME, Context.MODE_PRIVATE);
        progressDialog = new ProgressDialog(RegistrationStepFour.this);
        jsonHandler =new JSONHandler();
        serviceHandler = new ServiceHandler();
        mAppContext  = getApplicationContext();


        stepIndicator = (TextView) findViewById(R.id.txtStep);
        stepIndicator.setText("Step Four");
        sharedPreferences.edit().putInt("regat",4).apply();

        //Toolbar setup
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        restoreActionBar();
        //radio buttons setup
        rdMarried = (RadioButton)findViewById(R.id.radioButton);
        rdSingle = (RadioButton)findViewById(R.id.radioButton2);

        //Proceed button setup
        btnDone = (Button) findViewById(R.id.btnDone);
        btnDone.setOnClickListener(this);

        btnAdd = (Button) findViewById(R.id.btnAddChild);
        btnAdd.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.btnDone:
                if(rdMarried.isChecked()){
                    status = "married";
                }
                if(rdSingle.isChecked()){
                    status = "single";
                }
                sharedPreferences.edit().putString("status",status).apply();


                String bsname = sharedPreferences.getString("bsname","");
                String krapin = sharedPreferences.getString("krapin","");
                String fullname = sharedPreferences.getString("fullname","");
                String idno = sharedPreferences.getString("idno","");
                String email = sharedPreferences.getString("email","");
                String pass = sharedPreferences.getString("password","");


                String location = sharedPreferences.getString("location", "");
                String box = sharedPreferences.getString("address","");
                String code = sharedPreferences.getString("code","");
                String town = sharedPreferences.getString("town","");
                String tel_no1 = sharedPreferences.getString("phone1","");
                String tel_no2 = sharedPreferences.getString("phone2","");



                new RegistrationTrial().execute(location,box,code,town,email,"",idno,fullname,krapin,bsname,tel_no1,tel_no2
                        ,status,"1111");

//                intent = new Intent(RegistrationStepFour.this, RegistrationDone.class);
//                startActivity(intent);
                break;
            case R.id.btnAddChild:
                intent = new Intent(RegistrationStepFour.this, ChildRegistration.class);
                startActivity(intent);
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
    protected void onResume() {
        super.onResume();
        String jsonChild = sharedPreferences.getString("childjson", "");
        if (!jsonChild.equals("")) {
            try {
                jsonChildren = new JSONArray(jsonChild);
//                Toast.makeText(getApplicationContext(),jsonChild.length()+"",Toast.LENGTH_SHORT).show();
//TODO add view for no  of children
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_registration_step_four, menu);
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

    private class RegistrationTrial extends AsyncTask<String,String, String> {


        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            if(progressDialog!=null){
                progressDialog.setTitle("Vendor Registration");
                progressDialog.setMessage("Registration in progress. Please wait...");
                progressDialog.setCancelable(false);
                //
                progressDialog.show();

            }


            Toast.makeText(getApplicationContext(), "Sending data...", Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if(progressDialog!=null){
                progressDialog.dismiss();
            }

            if(!s.equals(null))
                if(!s.equals("")){
                    Log.d("log message>>>>",s);
                    String resp = jsonHandler.HandleResp(s);

                    if(resp.equals("false")){//not matching
                        Toast.makeText(mAppContext,"An error occurred during registration",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(mAppContext,"Registration successful",Toast.LENGTH_SHORT).show();

                        sharedPreferences.edit().putString("uuid", resp).apply();
                        sharedPreferences.edit().putBoolean("logged", true).apply();
                        sharedPreferences.edit().putString("bsname", "").apply();
                        sharedPreferences.edit().putString("krapin", "").apply();
                        sharedPreferences.edit().putString("fullname", "").apply();
                        sharedPreferences.edit().putString("idno", "").apply();
                        sharedPreferences.edit().putString("email", "").apply();
                        sharedPreferences.edit().putString("password", "").apply();
                        sharedPreferences.edit().putString("address", "").apply();
                        sharedPreferences.edit().putString("code", "").apply();
                        sharedPreferences.edit().putString("town", "").apply();
                        sharedPreferences.edit().putString("phone1", "").apply();
                        sharedPreferences.edit().putString("phone2", "").apply();
                        sharedPreferences.edit().putString("childjson","").apply();

                        sharedPreferences.edit().putInt("regat", 1).apply();
                        Intent intent= new Intent(getApplicationContext(), RegistrationDone.class);
                        startActivity(intent);
                    }
                }
        }


        @Override
        protected String doInBackground(String... strings) {
            String params[] = new String[strings.length];
            for (int i =0; i < strings.length ; i++){
                params[i] = URLEncoder.encode(strings[i]);
            }
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("password", params[13]));
            String jsonChildren = sharedPreferences.getString("childjson","");
            jsonChildren = URLEncoder.encode(jsonChildren);

            String url = Const.URL_API + "Register/reg?location=" + params[0] + "&box=" +
                    params[1]+"&code="+params[2]+"&town="+params[3]+"&email="+params[4]+"&dob="+params[5]+"&id_no="+params[6]+"&full_name="+params[7]
                    +"&kra_pin="+params[8]+"&business_name="+params[9]+"&phoneno1="+params[10]+"&phoneno2="+params[11]+"&marital_status="+params[12]
                    +"&children="+jsonChildren;


            Log.e("log Message>>>", url);
            String resp = serviceHandler.makeServiceCall(url,2,nameValuePairs);
            return resp;
        }
    }
}
