package ke.co.greencredit.nunua.Departments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ke.co.greencredit.nunua.General.AppConstants;
import ke.co.greencredit.nunua.General.CheckConnection;
import ke.co.greencredit.nunua.General.Const;
import ke.co.greencredit.nunua.General.JSONHandler;
import ke.co.greencredit.nunua.General.ServiceHandler;
import ke.co.greencredit.nunua.Product.Product;
import ke.co.greencredit.nunua.R;

public class DepartmentActivity extends AppCompatActivity {
    RecyclerView productsList;
    JSONHandler jsonHandler = new JSONHandler();
    //    ArrayList<>
    ServiceHandler serviceHandler = new ServiceHandler();
    ProgressDialog progressDialog;
    String dptName, storeId, dptId, uuid;
    SharedPreferences settings;
    CheckConnection checkConnection;
    List<Product> arrayList;
    Context context;
    TextView stepIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department);
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        context = getApplicationContext();
        progressDialog = new ProgressDialog(this);
        setSupportActionBar(toolbar);
        settings = getSharedPreferences(AppConstants.PREFS_NAME, MODE_PRIVATE);
        checkConnection = new CheckConnection(context);

        stepIndicator = (TextView) findViewById(R.id.txtStep);

        productsList = (RecyclerView) findViewById(R.id.productList);
        productsList.setLayoutManager(new GridLayoutManager(DepartmentActivity.this, 3));

        if (getIntent() != null) {
            storeId = getIntent().getStringExtra("storeId");
            dptId = getIntent().getStringExtra("dptId");
            dptName = getIntent().getStringExtra("dptname");
            stepIndicator.setText(dptName);

        } else {
            Toast.makeText(context, "Invalid request", Toast.LENGTH_SHORT).show();

            return;
        }

        uuid = settings.getString("uuid", "");
        if (checkConnection.isConnected()) {
            new LoadProducts().execute(storeId, dptId, uuid);
        } else {
            Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
            return;
        }

        restoreActionBar();
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setHomeAsUpIndicator(R.mipmap.left);
        actionBar.setDisplayHomeAsUpEnabled(true);



        // finally change the color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            actionBar.setHomeButtonEnabled(true);

            Window window = DepartmentActivity.this.getWindow();

            // clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            // only for gingerbread and newer versions
            window.setStatusBarColor(DepartmentActivity.this.getResources().getColor(R.color.status_bar));
        }
    }

    //Populate with fake items
    public List<DepartmentItem> fill_with_products() {
        List<DepartmentItem> data = new ArrayList<>();
        data.add(new DepartmentItem("Mango", "Ksh. 200", "10"));
        data.add(new DepartmentItem("Potato", "Ksh. 200", "10"));
        return data;
    }

    private class LoadProducts extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setTitle("Department");
            progressDialog.setMessage("Fetching products...");
            progressDialog.setCancelable(true);
            progressDialog.show();


//            Toast.makeText(getApplicationContext(), "Fetching stores...", Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
//            Toast.makeText(appContext,"here", Toast.LENGTH_SHORT);

            if (!s.equals(null))
                if (!s.equals("")) {
                    arrayList = jsonHandler.handleDepartmentItems(s);
                    if (arrayList.size() == 0) {//not matching
                        Toast.makeText(context, "No products", Toast.LENGTH_SHORT).show();
                    } else {

                        DepartmentRecyclerAdapter adapter = new DepartmentRecyclerAdapter(arrayList, context, productsList);
                        productsList.setAdapter(adapter);

                        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
                        itemAnimator.setAddDuration(1000);
                        itemAnimator.setRemoveDuration(1000);
                        productsList.setItemAnimator(itemAnimator);


                    }

                }
        }

        @Override
        protected String doInBackground(String... strings) {
            String url = Const.URL_API + "Products/category/" + strings[0] + "/" + strings[1] + "?uid=" + strings[2];
            Log.d("log Message>>>", url);
            String resp = serviceHandler.makeServiceCall(url, 1, null);

            return resp;
        }
    }
}
