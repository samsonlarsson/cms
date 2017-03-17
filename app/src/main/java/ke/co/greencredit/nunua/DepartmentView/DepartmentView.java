package ke.co.greencredit.nunua.DepartmentView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import ke.co.greencredit.nunua.General.AppConstants;
import ke.co.greencredit.nunua.General.CheckConnection;
import ke.co.greencredit.nunua.General.Const;
import ke.co.greencredit.nunua.General.JSONHandler;
import ke.co.greencredit.nunua.General.ServiceHandler;
import ke.co.greencredit.nunua.R;
import ke.co.greencredit.nunua.StoreNavigation.Department;

public class DepartmentView extends AppCompatActivity {
    TextView title;
    ListView departmentList;
    JSONHandler jsonHandler = new JSONHandler();
    AppConstants appConstants = new AppConstants();
    ServiceHandler serviceHandler = new ServiceHandler();
    ProgressDialog progressDialog;
    Context context;
    CheckConnection checkConnection;
    SharedPreferences sharedPreferences;
    String storeName, storeId;
    ArrayList<Department> departments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);

        context = getApplicationContext();
        setSupportActionBar(toolbar);
        checkConnection = new CheckConnection(context);
        progressDialog = new ProgressDialog(this);

        departmentList = (ListView) findViewById(R.id.departmentsList);

        sharedPreferences = getSharedPreferences(AppConstants.PREFS_NAME, Context.MODE_PRIVATE);


        title = (TextView) findViewById(R.id.txtStep);

        if (getIntent() != null) {
            storeName = getIntent().getStringExtra("storeName");
            storeId = getIntent().getStringExtra("storeId");
//            Log.d("log message >>>> ",storeName);
            title.setText(storeName);
        }

        restoreActionBar();
        if (checkConnection.isConnected()) {
            new FetchDepartments().execute(sharedPreferences.getString("uuid", ""), storeId);

        } else {
            Toast.makeText(context, "No internet connection.", Toast.LENGTH_SHORT).show();
            return;
        }



    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setHomeAsUpIndicator(R.mipmap.left);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private class FetchDepartments extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            if (progressDialog != null) {
                progressDialog.setTitle("Categories");
                progressDialog.setMessage("Loading store departments...");
                progressDialog.setCancelable(false);
                progressDialog.show();

            }


//            Toast.makeText(getApplicationContext(), "Sending data...", Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();

            if (!s.equals(null))
                if (!s.equals("")) {
                    departments = jsonHandler.HandleDepartment(s);
                    if (departments.size() != 0) {
                        DepartmentViewAdapter departmentViewAdapter = new DepartmentViewAdapter(context, departments, storeId);
                        departmentList.setAdapter(departmentViewAdapter);
                    } else {
                        Toast.makeText(context, "No departments", Toast.LENGTH_SHORT).show();
                    }

                }
        }


        @Override
        protected String doInBackground(String... strings) {

//            String url = Const.URL_API + "Products/category?storeId=" + strings[0] + "&category=" + strings[1];
            String url = Const.URL_API + "Stores/departments/" + strings[1] + "?uid=" + strings[0];

            String resp = serviceHandler.makeServiceCall(url, 1, null);
            return resp;
        }
    }

}
