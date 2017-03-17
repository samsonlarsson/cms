package ke.co.greencredit.nunua.Home;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import ke.co.greencredit.nunua.General.AppConstants;
import ke.co.greencredit.nunua.General.CheckConnection;
import ke.co.greencredit.nunua.General.ImageLoader;
import ke.co.greencredit.nunua.General.JSONHandler;
import ke.co.greencredit.nunua.General.ServiceHandler;
import ke.co.greencredit.nunua.Product.Product;
import ke.co.greencredit.nunua.Product.ProductView;
import ke.co.greencredit.nunua.R;

public class Search extends AppCompatActivity implements View.OnClickListener, TextWatcher {
    TextView cancelSearch;
    TextView editSearch;
    JSONHandler jsonHandler;
    ServiceHandler serviceHandler;
    CheckConnection checkConnection;
    ProgressDialog progressDialog = null;
    Context appContext;

    AppConstants mAppConstants = new AppConstants();
    String url;
    SharedPreferences sharedPreferences;

    String uuid;
    ListView resultsList;
    PopularItemsAdapter popularItems;
    SearchResultsAdapter searchResultsAdapter;
    AppConstants appConstants = new AppConstants();
    ImageLoader imageLoader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        appContext = getApplicationContext();
        checkConnection = new CheckConnection(appContext);
        progressDialog = new ProgressDialog(Search.this);
        serviceHandler = new ServiceHandler();
        jsonHandler = new JSONHandler();
        sharedPreferences = getSharedPreferences(mAppConstants.PREFS_NAME, Context.MODE_PRIVATE);
        uuid = sharedPreferences.getString("uuid", "");


        cancelSearch = (TextView) findViewById(R.id.cancel);
        cancelSearch.setOnClickListener(this);
        editSearch = (TextView) findViewById(R.id.edit_searchStores);
        editSearch.addTextChangedListener(this);

        resultsList = (ListView) findViewById(R.id.search_results);

    }


    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String toSearch = editSearch.getText().toString();
        if (!toSearch.equals("") && toSearch.length()  > 2) {
            new SearchStores().execute(uuid,toSearch);
//            Toast.makeText(getApplicationContext(),toSearch,Toast.LENGTH_SHORT).show();
        } else if(toSearch.equals("")) {
            Toast.makeText(appContext, "Search for?", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    private class SearchStores extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s != null)
                if (!s.equals("")) {
                    //json handler
                    Log.d("JSON", s);
                    //// TODO: 5/2/16  
//                    List<Product> itemsList = jsonHandler.handleItems(s);
//
//                    if (itemsList.size() == 0) {
//                        Toast.makeText(appContext, "No items", Toast.LENGTH_SHORT).show();
//                    } else {
//                        searchResultsAdapter = new SearchResultsAdapter(appContext,itemsList);
//                        resultsList.setAdapter(searchResultsAdapter);
//                    }
                }
        }

        @Override
        protected String doInBackground(String... strings) {

            url = mAppConstants.baseURL + "Products/name/"+ URLEncoder.encode(strings[1]);
            Log.d("log Message>>>", url);
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("uid", strings[0]));
            String resp = serviceHandler.makeServiceCall(url, 2, nameValuePairs);
            return resp;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel:
                onBackPressed();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
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

    public class SearchResultsAdapter extends BaseAdapter {
        Context mContext;
        List<Product> productArrayList;

        public SearchResultsAdapter(Context context,List<Product> searchResults) {
            mContext = context;
            productArrayList = new ArrayList<>();
            productArrayList = searchResults;
            imageLoader = new ImageLoader(mContext);
        }

        @Override
        public int getCount() {
            if (productArrayList != null && productArrayList.size() != 0) {
                return productArrayList.size();
            } else {
                return 0;
            }
        }

        @Override
        public Object getItem(int position) {
            return productArrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final Product product = productArrayList.get(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.list_view_item, null);
            }

            // TODO
            // Set product image
            //ImageView productImg = (ImageView) convertView.findViewById(R.id.pImage);

            // Product Name
            TextView productName = (TextView) convertView.findViewById(R.id.product_name);
            productName.setText(product.getName());

            // Product Price
            TextView productPrice = (TextView) convertView.findViewById(R.id.price);
            productPrice.setText("Ksh. " + product.getUnit_price());
            ImageView imageView = (ImageView)convertView.findViewById(R.id.pImage);
            imageLoader.DisplayImage(appConstants.imageURL+product.getImage_path(),R.drawable.hourglass,imageView);
            // Product Quantity
            TextView productQuantity = (TextView) convertView.findViewById(R.id.quantity);
            productQuantity.setText(product.getUnit_measure());

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ProductView.class);
                    ArrayList<Product> arr = new ArrayList<Product>();
                    arr.add(product);
                    intent.putExtra("product", arr);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                }
            });
            return convertView;
        }
    }
}
