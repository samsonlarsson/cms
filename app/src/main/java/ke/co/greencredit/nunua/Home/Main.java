package ke.co.greencredit.nunua.Home;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ke.co.greencredit.nunua.About;
import ke.co.greencredit.nunua.Cart.Cart;
import ke.co.greencredit.nunua.Checkout.Checkout;
import ke.co.greencredit.nunua.Delivery.Delivery;
import ke.co.greencredit.nunua.General.AppConstants;
import ke.co.greencredit.nunua.General.CheckConnection;
import ke.co.greencredit.nunua.General.Const;
import ke.co.greencredit.nunua.General.DataManager;
import ke.co.greencredit.nunua.General.JSONHandler;
import ke.co.greencredit.nunua.General.ServiceHandler;
import ke.co.greencredit.nunua.History.History;
import ke.co.greencredit.nunua.LogIn;
import ke.co.greencredit.nunua.Loyalty.Loyalty;
import ke.co.greencredit.nunua.R;
import ke.co.greencredit.nunua.StoreNavigation.Department;
import ke.co.greencredit.nunua.StoreNavigation.StoreDepartment;
import ke.co.greencredit.nunua.StoreNavigation.StoreListing;

public class Main extends AppCompatActivity implements View.OnClickListener {
    public DrawerLayout mDrawerLayout;
    public ListView mDrawerList;
    public ActionBarDrawerToggle mDrawerToggle;
    Toolbar toolbar;
    TextView tvCounter;
    Intent intent;
    boolean logged;
    Context context;
    CheckConnection checkConnection;
    SharedPreferences settings;
    String cartUiid, uuid;
    DataManager dataManager;
    String uname, location;
    TextView tvName;
    TextView tvLocation;
    ImageView searchIcon;
    ImageView cartIcon;
    String cartat;
    LinearLayout curStore;
    LinearLayout curDept;
    AlertDialog.Builder builder1;
    SharedPreferences sharedPreferences;
    ArrayList<Department> dpArrayList = null;
    boolean toReturn = false;
    boolean cartactive;
    RecyclerView homeRecView;
    AppConstants mAppConstants = new AppConstants();
    ProgressDialog progressDialog;
    ServiceHandler serviceHandler;
    JSONHandler jsonHandler;
    String url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkConnection = new CheckConnection(getApplicationContext());
        setSupportActionBar(toolbar);
        context = getApplicationContext();
        progressDialog = new ProgressDialog(this);
        serviceHandler = new ServiceHandler();

        dataManager = new DataManager(context, 1, "");
        tvLocation = (TextView) findViewById(R.id.textView2);
        tvName = (TextView) findViewById(R.id.textView3);
        settings = getSharedPreferences(AppConstants.PREFS_NAME, Context.MODE_PRIVATE);
        sharedPreferences = context.getSharedPreferences(AppConstants.PREFS_NAME, Context.MODE_PRIVATE);

        logged = settings.getBoolean("logged", false);
        cartactive = settings.getBoolean("CartActive", false);
        jsonHandler = new JSONHandler();

        builder1 = new AlertDialog.Builder(this);


//        headerBar.setVisibility(View.VISIBLE);

        if (settings.getBoolean("logged", false)) {
            handleLogged();
            if (uuid.equals("")) {
                Toast.makeText(getApplicationContext(), "Log in", Toast.LENGTH_SHORT).show();
                Intent mainIntent = new Intent(Main.this, LogIn.class);
                startActivity(mainIntent);
                finish();
                return;
            }


            manageToolBar();

            //fetch data
            if (checkConnection.isConnected()) {
                new LoadStores().execute(uuid);

            } else {
                Toast.makeText(context, "Check connection", Toast.LENGTH_SHORT).show();
            }


        } else {
            Toast.makeText(context, "Log in", Toast.LENGTH_SHORT).show();
            Intent mainIntent = new Intent(Main.this, LogIn.class);
            startActivity(mainIntent);
            return;
        }

//        reqPopular();
//        new FetchMainScreenDetails().execute(uuid);

    }

    void manageToolBar() {

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        toolbar = (Toolbar) findViewById(R.id.home_toolbar);

        tvCounter = (TextView) toolbar.findViewById(R.id.cart_counter);
        tvCounter.setOnClickListener(this);

        cartUiid = settings.getString("cartuuid", "");
        boolean iscartActive = settings.getBoolean("CartActive", false);
        if (cartUiid.equals("") || !iscartActive) {
            tvCounter.setText("0");

        } else {
            tvCounter.setText(dataManager.getItemsCount(cartUiid) + "");

        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        restoreActionBar();

        addDrawerItems();
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        getSupportActionBar().setHomeButtonEnabled(true);

        cartIcon = (ImageView) toolbar.findViewById(R.id.ic_cart);
        cartIcon.setOnClickListener(this);

        searchIcon = (ImageView) toolbar.findViewById(R.id.ic_search);
        searchIcon.setOnClickListener(this);
    }

    void handleLogged() {
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        uuid = settings.getString("uuid", "");

        uname = settings.getString("uname", "");
        location = settings.getString("location", "");

        cartat = settings.getString("cartat", "");
        tvLocation.setText(location);
        tvName.setText(uname);
    }

    void ManageTab() {
        final ViewPager viewPager = (ViewPager) findViewById(R.id.home_viewpager);
        setupViewPager(viewPager);

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.home_tabs);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                switch (tab.getPosition()) {
                    case 0:

//                        tabLayout.setupWithViewPager(viewPager);


                        break;
                    case 1:
//                        tabLayout.setupWithViewPager(viewPager);

                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }

        });
    }

    private void reqPopular() {
//        showProgressDialog();
        progressDialog.setTitle("Home");
        progressDialog.setMessage("Fetching stores data...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                Const.URL_API + "Products/popular", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                Toast.makeText(context, response, Toast.LENGTH_SHORT).show();

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {

            /**
             * Passing some request headers
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("uid", uuid);
                return params;
            }

        };

    }

    //Initializing the view page adapter
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

//        homeRecView.setLayoutManager(new LinearLayoutManager(this));
        //adapter


//        homeRecyclerAdapter = new HomeRecyclerAdapter(getApplicationContext(), itemsList);



        adapter.addFrag(new PopularFragment(), "POPULAR");

        StoresFragment storesFragment = new StoresFragment();

//        storesFragment.setArguments();
        adapter.addFrag(new StoresFragment(), "STORES");
        viewPager.setAdapter(adapter);
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);

        // finally change the color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            actionBar.setHomeButtonEnabled(true);

            Window window = Main.this.getWindow();

            // clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            // only for gingerbread and newer versions
            window.setStatusBarColor(Main.this.getResources().getColor(R.color.status_bar));
        }
    }

    public void addDrawerItems() {
        NavMenuAdapter navMenuAdapter = new NavMenuAdapter(getApplicationContext());
        mDrawerList.setAdapter(navMenuAdapter);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        if (mDrawerToggle != null) {
            mDrawerToggle.syncState();
        }


    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ic_cart:
                if (cartat.equals("")) {
                    intent = new Intent(Main.this, Cart.class);
                    startActivity(intent);
                } else {
                    intent = new Intent(Main.this, Checkout.class);
                    startActivity(intent);
                }

                break;
            case R.id.cart_counter:
                intent = new Intent(Main.this, Cart.class);
                startActivity(intent);
                break;
            case R.id.ic_search:
                intent = new Intent(Main.this, Search.class);
                startActivity(intent);
                break;
            case R.id.curStoreContainer:
                curStore.setBackgroundColor(getResources().getColor(R.color.yellow));
                intent = new Intent(Main.this, StoreListing.class);
                startActivity(intent);

                break;
            case R.id.curDeptContainer:
                curDept.setBackgroundColor(getResources().getColor(R.color.yellow));
                intent = new Intent(Main.this, StoreDepartment.class);
                //TODO

                if (dpArrayList != null) {

                    intent.putExtra("dptlist", dpArrayList);

                } else {
                    String id = settings.getString("currstoreid", "");
                    if (id == "") {
                        //TODO
                        buildDialog("Departments", "Select store to load departments", 1);
                        return;
                    }
                    Log.d("log message Main", id);
                    intent.putExtra("storeId", id);
                }


                startActivity(intent);
                break;
        }
    }

    void buildDialog(String title, String message, int type) {
        builder1.setMessage(message);
        builder1.setTitle(title);
        builder1.setCancelable(true);
        AlertDialog alert11;

        switch (type) {
            case 0:
                builder1.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                sharedPreferences.edit().putBoolean("logged", false).apply();
                                Intent mainIntent = new Intent(Main.this, LogIn.class);
                                startActivity(mainIntent);
                                finish();
                                dialog.cancel();

                            }
                        });
                builder1.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                toReturn = false;
                                dialog.cancel();
                            }
                        });

                alert11 = builder1.create();
                alert11.show();

                break;
            case 1:
                builder1.setPositiveButton("Continue",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent mainIntent = new Intent(Main.this, StoreListing.class);
                                startActivity(mainIntent);
                                dialog.cancel();
                                finish();
                            }
                        });

                builder1.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
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

    private class LoadStores extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(Main.this, "Stores", "Loading available stores...", true, true);

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();

            if (!s.equals(null))
                if (!s.equals("")) {

                    settings.edit().putString("storearray", s).apply();
                    ManageTab();
//                    arrayList = jsonHandler.HandleStores(s,mContext);


//                    if(arrayList.size() == 0){//not matching
//                        Toast.makeText(mContext,"Could not load stores.",Toast.LENGTH_SHORT).show();
//                    }else{

//                        StoreListingAdapter storeListingAdapter = new StoreListingAdapter(mContext,arrayList);
// TODO: 5/16/16
//                        storeListings.setAdapter(storeListingAdapter);
//                    }
                }
        }

        @Override
        protected String doInBackground(String... strings) {
            String param0 = URLEncoder.encode(strings[0]);
//            String param1 = URLEncoder.encode(strings[1]);

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("uid", param0));


            url = Const.URL_API + "Stores/get";
            Log.d("log Message>>>", url);
            String resp = serviceHandler.makeServiceCall(url, 2, nameValuePairs);
            Log.d("log Message>>>", resp);
            return resp;
        }
    }

    public class NavMenuAdapter extends BaseAdapter {
        Context mContext;
        ArrayList<NavItems> navItemsArrayList;

        public NavMenuAdapter(Context context) {

            mContext = context;
            navItemsArrayList = new ArrayList<NavItems>();
            String menuItems[] = {"Home", "Account", "Delivery", "History", "Loyalty Points", "About", "Log Out"};
            int menuIcons[] = {R.drawable.home, R.drawable.user, R.drawable.truck, R.drawable.calendar, R.drawable.badge, R.drawable.info, R.drawable.logout};

            for (int i = 0; i < menuItems.length; i++) {
                //Set the item names
                NavItems navItems = new NavItems();
                navItems.setItemName(menuItems[i]);

                //Set the item icons
                navItems.setItemIcon(menuIcons[i]);

                navItemsArrayList.add(navItems);
            }
        }

        @Override
        public int getCount() {
            if (navItemsArrayList != null && navItemsArrayList.size() != 0) {
                return navItemsArrayList.size();
            } else {
                return 0;
            }
        }

        @Override
        public Object getItem(int position) {
            return navItemsArrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            NavItems navItems = navItemsArrayList.get(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.nav_item, null);
            }

            //Set the images
            ImageView navIcon = (ImageView) convertView.findViewById(R.id.nav_icon);
            navIcon.setImageResource(navItems.getItemIcon());

            TextView navItem = (TextView) convertView.findViewById(R.id.txtNavItem);
            navItem.setText(navItems.getItemName());

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDrawerLayout.closeDrawers();
                    switch (position) {
                        case 0:
                            Toast.makeText(mContext, "Home", Toast.LENGTH_SHORT).show();
                            break;
                        case 1:
                            Toast.makeText(mContext, "Unavailable at the moment", Toast.LENGTH_SHORT).show();
                            break;
                        case 2:
                            Toast.makeText(mContext, "Delivery", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Main.this, Delivery.class);
                            startActivity(intent);
                            break;

                        case 3:
                            Toast.makeText(mContext, "History", Toast.LENGTH_SHORT).show();
                            Intent intent2 = new Intent(Main.this, History.class);
                            startActivity(intent2);
                            break;
                        case 4:
                            Toast.makeText(mContext, "Loyalty Points", Toast.LENGTH_SHORT).show();
                            Intent intent3 = new Intent(Main.this, Loyalty.class);
//                            startActivity(intent3);
                            Toast.makeText(mContext, "Unavailable at the moment", Toast.LENGTH_SHORT).show();


                            break;
                        case 5:
                            Toast.makeText(mContext, "About", Toast.LENGTH_SHORT).show();
                            Intent intent1 = new Intent(Main.this, About.class);
                            startActivity(intent1);
                            break;
                        case 6:
                            buildDialog("User", "Are you sure you want to log out?", 0);
                            break;
                    }
                }
            });

            return convertView;
        }


    }


}
