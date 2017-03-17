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
import android.widget.TextView;

import ke.co.greencredit.nunua.General.AppConstants;

public class RegistrationStepTwo extends AppCompatActivity implements View.OnClickListener{

    Button btnProceed;
    Toolbar toolbar;
    TextView stepIndicator;
    SharedPreferences sharedPreferences;
    AppConstants appConstants = new AppConstants();
    EditText etFullName,etEmail, etPassword , etPass2,etIdNo;
    String fullName, email, password, pass2,idno;
    AlertDialog.Builder builder1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_step_two);
        sharedPreferences = getSharedPreferences(appConstants.PREFS_NAME, Context.MODE_PRIVATE);
        builder1  = new AlertDialog.Builder(this);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        stepIndicator = (TextView) findViewById(R.id.txtStep);
        stepIndicator.setText("Step Two");
        sharedPreferences.edit().putInt("regat",2).apply();


        fullName = sharedPreferences.getString("fullname","");
        email = sharedPreferences.getString("email","");
        password = sharedPreferences.getString("password","");
        idno = sharedPreferences.getString("idno","");

        restoreActionBar();
        //textview setup
        etFullName = (EditText)findViewById(R.id.editFullName);
        etEmail = (EditText)findViewById(R.id.editEmail);
        etPassword = (EditText)findViewById(R.id.editPass);
        etPass2 = (EditText)findViewById(R.id.editConfPass);


        etIdNo = (EditText)findViewById(R.id.editIdNo);

        etFullName.setText(fullName);
        etEmail.setText(email);
        etPassword.setText(password);
        etIdNo.setText(idno);
        etPass2.setText(password);

        etPassword.setText("1111");
        etPass2.setText("1111");//TODO

        //Proceed button setup
        btnProceed = (Button) findViewById(R.id.btnProceed);
        btnProceed.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnProceed:
                fullName = etFullName.getText().toString();
                email = etEmail.getText().toString();
                password = etPassword.getText().toString();
                pass2 = etPass2.getText().toString();
                idno = etIdNo.getText().toString();
                if(fullName.equals("")||email.equals("") || password.equals("") || pass2.equals("") || idno.equals("")){
//                    if(fullName.equals("")||email.equals("") || password.equals("") || pass2.equals("") || idno.equals("")){
                    buildDialog("Registration", "Fill all the details marked as *",0);
                    return;
                }
                if(!appConstants.isEmailValid(email)){
                    buildDialog("Registration", "Email entered is not valid",0);
                    etEmail.setFocusable(true);

                    return;
                }

                if(!password.equals(pass2)){
                    buildDialog("Registration", "Passwords not matching",0);
                    etPassword.setFocusable(true);
                return;
                }
                if(!appConstants.isNumeric(idno)){
                    buildDialog("Registration", "Invalid Identification Number provided.",0);
                    etIdNo.setFocusable(true);
                }

                sharedPreferences.edit().putString("fullname",fullName).apply();
                sharedPreferences.edit().putString("email",email).apply();
                sharedPreferences.edit().putString("password",password).apply();
                sharedPreferences.edit().putString("idno",idno).apply();
                Intent intent = new Intent(RegistrationStepTwo.this, RegistrationStepThree.class);
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
        getMenuInflater().inflate(R.menu.menu_registration_step_two, menu);
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
