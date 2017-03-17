package ke.co.greencredit.nunua;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import ke.co.greencredit.nunua.General.AppConstants;

public class RegistrationStepThree extends AppCompatActivity implements View.OnClickListener{
    Button btnProceed;
    ImageView backBtn;
    Toolbar toolbar;
    TextView stepIndicator;
    SharedPreferences sharedPreferences;
    AppConstants mAppConstants  = new AppConstants();
    EditText editLocation,editAddress,editCode, editTown,editPhone1,editPhone2;
    String location,address,code,town,phone1,phone2;

    AlertDialog.Builder builder1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_step_three);
        sharedPreferences = getSharedPreferences(mAppConstants.PREFS_NAME, Context.MODE_PRIVATE);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        builder1  = new AlertDialog.Builder(this);


        stepIndicator = (TextView) findViewById(R.id.txtStep);
        stepIndicator.setText("Step Three");
        sharedPreferences.edit().putInt("regat", 3).apply();

        location = sharedPreferences.getString("location","");
        address = sharedPreferences.getString("address","");
        code = sharedPreferences.getString("code","");
        town = sharedPreferences.getString("town","");
        phone1 = sharedPreferences.getString("phone1","");
        phone2 = sharedPreferences.getString("phone2","");


        restoreActionBar();
        //edit texts setup
        editLocation = (EditText)findViewById(R.id.editLocation);
        editAddress = (EditText)findViewById(R.id.editPostal);
        editCode = (EditText)findViewById(R.id.editCode);
        editTown = (EditText)findViewById(R.id.editTown);
        editPhone1 = (EditText)findViewById(R.id.editTel1);
        editPhone2 = (EditText)findViewById(R.id.editTel2);

        editLocation.setText(location);
        editAddress.setText(address);
        editCode.setText(code);
        editTown.setText(town);
        editPhone2.setText(phone2);
        editPhone1.setText(phone1);


        //Proceed button setup
        btnProceed = (Button) findViewById(R.id.btnProceed);
        btnProceed.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnProceed:

                location = editLocation.getText().toString();
                address = editAddress.getText().toString();
                code = editCode.getText().toString();
                town = editTown.getText().toString();

                phone1 = editPhone1.getText().toString();
                phone2 = editPhone2.getText().toString();
                if(location.equals("") || address.equals("") || town.equals("")){
                    buildDialog("Registration","Some required details are missing.",0);
                    return;
                }else{
                    if(phone1.equals("") && phone2.equals("")){
                        buildDialog("Registration","Atleast one phone number must be provided",0);
                        return;
                    }
                    if(!phone1.equals("")){
                        boolean isNumeric = mAppConstants.isNumeric(phone1);
                        if(!isNumeric){
                            buildDialog("Registration","Invalid phone number 1 provided",0);
                            return;
                        }
                    }
                    if(!phone2.equals("")){
                        boolean isNumeric = mAppConstants.isNumeric(phone2);
                        if(!isNumeric){
                            buildDialog("Registration","Invalid phone number 2 provided",0);
                            return;
                        }
                    }
                }

                sharedPreferences.edit().putString("location",location).apply();
                sharedPreferences.edit().putString("address",address).apply();
                sharedPreferences.edit().putString("code",code).apply();
                sharedPreferences.edit().putString("town",town).apply();
                sharedPreferences.edit().putString("phone1",phone1).apply();
                sharedPreferences.edit().putString("phone2",phone2).apply();
                Intent intent = new Intent(RegistrationStepThree.this, RegistrationStepFour.class);
                startActivity(intent);
                break;
        }
    }
    void buildDialog(String title, String message,int type){
        switch (type){
            case 0:

                builder1.setMessage(message);
                builder1.setTitle(title);
                builder1.setCancelable(true);
                builder1.setPositiveButton("Retry",
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
        getMenuInflater().inflate(R.menu.menu_registration_step_three, menu);
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
