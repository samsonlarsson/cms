package ke.co.greencredit.nunua;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ke.co.greencredit.nunua.General.AppConstants;

public class ChildRegistration extends AppCompatActivity implements View.OnClickListener {
    android.support.v7.widget.Toolbar toolbar;
    TextView stepIndicator;
    TextView tvChildName,tvSchool,tvAddress;
    String stJson;
    SharedPreferences sharedPreferences;
    AppConstants appConstants = new AppConstants();
    Button btnAdd,btnDone;
    RadioButton rdMale,rdFemale;
    String cName,cSchool,sAge,cGender;
    Context mContext;

    JSONObject objChild;
    JSONArray jsonChildren;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_registration);

        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        mContext = getApplicationContext();

        sharedPreferences = getSharedPreferences(appConstants.PREFS_NAME,MODE_PRIVATE);

        restoreActionBar();

        stepIndicator = (TextView) findViewById(R.id.txtStep);
        stepIndicator.setText("Child Registration");
        btnAdd = (Button)findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);
        tvChildName = (TextView)findViewById(R.id.editFullName);
        tvSchool = (TextView)findViewById(R.id.editSchool);
        tvAddress = (TextView)findViewById(R.id.editAddress);
        btnDone = (Button)findViewById(R.id.btnDone);
        btnDone.setOnClickListener(this);

        stJson = sharedPreferences.getString("childjson","");
        if(!stJson.equals("")){
            try {
                jsonChildren = new JSONArray(stJson);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            jsonChildren = new JSONArray();
        }

        rdFemale = (RadioButton)findViewById(R.id.radioButton2);
        rdMale = (RadioButton)findViewById(R.id.radioButton);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnAdd:
                if(rdMale.isChecked()){
                    cGender = "male";
                }else{
                    cGender = "female";
                }
                cName = tvChildName.getText().toString();
                cSchool = tvSchool.getText().toString();
                sAge = tvAddress.getText().toString();

                if(cName.equals("") || cSchool.equals("") || sAge.equals("")){
                    Toast.makeText(mContext,"Some details are missing",Toast.LENGTH_SHORT).show();
                    return;
                }

                objChild = new JSONObject();
                try {
                    objChild.put("fullname",cName);
                    objChild.put("gender",cGender);
                    objChild.put("yob",sAge);
                    objChild.put("school",cSchool);

                    jsonChildren.put(objChild);

                } catch (JSONException e) {
                    e.printStackTrace();
                    return;
                }

                Toast.makeText(mContext,"Child inserted",Toast.LENGTH_SHORT).show();
                tvAddress.setText("");
                tvSchool.setText("");
                tvChildName.setText("");
                break;
            case R.id.btnDone:
                intent = new Intent(ChildRegistration.this, RegistrationStepFour.class);
                startActivity(intent);
                break;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        String jsonChild = sharedPreferences.getString("childjson","");
        if(!jsonChild.equals("")){
            try {
                jsonChildren = new JSONArray(jsonChild);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(jsonChildren==null){
            return;
        }

        if(jsonChildren.length() != 0){
            sharedPreferences.edit().putString("childjson",jsonChildren.toString()).apply();
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
        getMenuInflater().inflate(R.menu.menu_child_registration, menu);
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
}
