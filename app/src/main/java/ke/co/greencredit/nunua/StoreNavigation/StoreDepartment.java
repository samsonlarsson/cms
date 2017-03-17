package ke.co.greencredit.nunua.StoreNavigation;

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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import ke.co.greencredit.nunua.General.AppConstants;
import ke.co.greencredit.nunua.General.JSONHandler;
import ke.co.greencredit.nunua.General.ServiceHandler;
import ke.co.greencredit.nunua.Home.Main;
import ke.co.greencredit.nunua.R;

public class StoreDepartment extends AppCompatActivity {
    ListView deptList;
    Toolbar toolbar;
    TextView appBarText;
    ArrayList<Department> departmentArrayList;
    Context appContext;
    AlertDialog.Builder builder1;
    Intent intent;
    ProgressDialog progressDialog;
    AppConstants mAppConstants = new AppConstants();
    ServiceHandler serviceHandler = new ServiceHandler();
    String uuid;
    SharedPreferences sharedPreferences;
    ArrayList<Department> arrayList;
    JSONHandler jsonHandler = new JSONHandler();
//    ArrayList<>
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_department);
        appContext = getApplicationContext();
        builder1  = new AlertDialog.Builder(this);
        progressDialog = new ProgressDialog(this);
        sharedPreferences = getSharedPreferences(AppConstants.PREFS_NAME, MODE_PRIVATE);
        deptList = (ListView) findViewById(R.id.store_departments);

        uuid = sharedPreferences.getString("uuid","");
        if(getIntent().getSerializableExtra("dptlist")!=null){
            departmentArrayList = (ArrayList<Department>)getIntent().getSerializableExtra("dptlist");
        }else if(getIntent().getExtras().containsKey("storeId")){
            String storeID = getIntent().getExtras().getString("storeId");
            new LoadDepartments().execute(uuid,storeID);
        }else{

            buildDialog("Department", "No store has been selected.", 0);
        }

//        Toast.makeText(appContext,"arr size"+departmentArrayList.size(),Toast.LENGTH_SHORT).show();

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        restoreActionBar();

        appBarText = (TextView) toolbar.findViewById(R.id.txtStep);
        appBarText.setText("Departments");


        DepartmentsAdapter departmentsAdapter = new DepartmentsAdapter(getApplicationContext(),departmentArrayList);
        deptList.setAdapter(departmentsAdapter);
        //**** dont forget to assign this eric
//        departmentArrayList =

    }
    void buildDialog(String title, String message,int type){
        switch (type){
            case 0:

                builder1.setMessage(message);
                builder1.setTitle(title);
                builder1.setCancelable(true);
                builder1.setPositiveButton("Go Back",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                intent = new Intent(StoreDepartment.this, Main.class);
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

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setHomeAsUpIndicator(R.mipmap.left);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_store_department, menu);
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
    private class LoadDepartments extends AsyncTask<String,String, String> {


        String url;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setTitle("Stores");
            progressDialog.setMessage("Loading available departments...");
            progressDialog.setCancelable(true);
            progressDialog.show();


//            Toast.makeText(getApplicationContext(), "Fetching stores...", Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
//            Toast.makeText(appContext,"here", Toast.LENGTH_SHORT);

            if(!s.equals(null))
                if(!s.equals("")){
//                    Log.d("log message deartments",s);


                    arrayList = jsonHandler.HandleDepartment(s);
                    if(arrayList != null){
                        if(arrayList.size() == 0){//not matching
                            Toast.makeText(appContext, "Could not load departments.", Toast.LENGTH_SHORT).show();
                        }else{
                            DepartmentsAdapter departmentsAdapter = new DepartmentsAdapter(getApplicationContext(),arrayList);
                            deptList.setAdapter(departmentsAdapter);
                        }
                    }else{
                        Toast.makeText(appContext, "Unable to reload departments", Toast.LENGTH_SHORT).show();
                    }

                }
        }

        @Override
        protected String doInBackground(String... strings) {
            String param0 = URLEncoder.encode(strings[0]);
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("uid", param0));
            url = mAppConstants.baseURL + "Stores/departments/"+strings[1];
//            Toast.makeText(getApplicationContext(),url,Toast.LENGTH_LONG).show();
            Log.d("log Message>>>", url);
            String resp = serviceHandler.makeServiceCall(url,2,nameValuePairs);
            Log.d("log Message>>>", resp);
            return resp;
        }
    }

}
