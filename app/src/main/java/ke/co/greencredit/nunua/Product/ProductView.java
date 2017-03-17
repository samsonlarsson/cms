package ke.co.greencredit.nunua.Product;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;
import java.util.UUID;

import ke.co.greencredit.nunua.Cart.TypeCart;
import ke.co.greencredit.nunua.General.AppConstants;
import ke.co.greencredit.nunua.General.Const;
import ke.co.greencredit.nunua.General.DataManager;
import ke.co.greencredit.nunua.Home.Main;
import ke.co.greencredit.nunua.R;
import ke.co.greencredit.nunua.app.AppController;

public class ProductView extends AppCompatActivity implements  View.OnClickListener{
    TextView cancelBtn;
    TextView tvQuantity;
    Button btnAdd, btnSub,btnAddToCart;
    int intCurr;
    String stCurr;
    Product currentProduct;
    DataManager dataManager;
    Context mContext;
    SharedPreferences settings;
    AppConstants appConstants = new AppConstants();
    com.android.volley.toolbox.ImageLoader imageLoader;
    String uuid,cartUuid;

    TextView tvProductName,tvProductPrice,tvProductUnit;
    NetworkImageView productImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_product_view);

        mContext = getApplicationContext();

        if(getIntent().getSerializableExtra("product")!=null){
            ArrayList<Product> productArrayList = (ArrayList<Product>)getIntent().getSerializableExtra("product");
            currentProduct = productArrayList.get(0);
            //            Toast.makeText(getApplicationContext(),currentProduct.getName(),Toast.LENGTH_SHORT).show();

            tvProductName = (TextView)findViewById(R.id.txtProductName);
            tvProductPrice = (TextView)findViewById(R.id.txtProductPrice);
            tvProductUnit = (TextView)findViewById(R.id.txtProductUnit);

            tvProductName.setText(currentProduct.getName());
            tvProductPrice.setText("Ksh. "+currentProduct.getUnit_price()+".00 / "+currentProduct.getUnit_measure());
            tvProductUnit.setText(currentProduct.getCategory());
            productImage = (NetworkImageView)findViewById(R.id.img_product);
            imageLoader = AppController.getInstance().getImageLoader();
            productImage.setImageUrl(Const.URL_API + "Render/product/" + currentProduct.getId(), imageLoader);
        }

        settings = getSharedPreferences(AppConstants.PREFS_NAME, MODE_PRIVATE);
        btnAdd = (Button)findViewById(R.id.btnAdd);
        btnSub = (Button)findViewById(R.id.btnSub);
        btnSub = (Button)findViewById(R.id.btnSub);
        btnAddToCart = (Button)findViewById(R.id.btnAddToCart);
        tvQuantity = (TextView)findViewById(R.id.txtQuantity);


        cancelBtn = (TextView) findViewById(R.id.txt_cancel);
        cancelBtn.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
        btnSub.setOnClickListener(this);
        btnAddToCart.setOnClickListener(this);

        dataManager = new DataManager(mContext,1,"");
        if(settings.getBoolean("CartActive",false)){
            cartUuid = settings.getString("cartuuid","");
            if(cartUuid.equals("")){
                cartUuid = UUID.randomUUID().toString();
                settings.edit().putString("cartuuid",cartUuid).apply();
            }
        }else {
            cartUuid = UUID.randomUUID().toString();
            settings.edit().putString("cartuuid",cartUuid).apply();
            settings.edit().putBoolean("CartActive",true).apply();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txt_cancel:
                Intent intent = new Intent(ProductView.this, Main.class);
                startActivity(intent);
                finish();
                break;
            case R.id.btnAdd:
                stCurr = tvQuantity.getText().toString();
                intCurr = Integer.parseInt(stCurr);
                intCurr = intCurr + 1;
                tvQuantity.setText(intCurr+"");
                break;
            case R.id.btnSub:
                stCurr = tvQuantity.getText().toString();
                intCurr = Integer.parseInt(stCurr);
                if(intCurr != 0){
                    intCurr = intCurr - 1;
                    tvQuantity.setText(intCurr+"");
                }
                break;
            case R.id.btnAddToCart:
                int qAvaila = Integer.parseInt(currentProduct.getQuantity());

                stCurr = tvQuantity.getText().toString();
                intCurr = Integer.parseInt(stCurr);
                if(intCurr>qAvaila){
                    Toast.makeText(getApplicationContext(),"Only "+qAvaila+" items are available in stock.",Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    if(intCurr > 0){
                        handleCart();
                    }else {
                        Toast.makeText(getApplicationContext(),"0 items selected.",Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }
    void handleCart(){
        TypeCart typeCart = new TypeCart(cartUuid,"active",currentProduct);
//        Log.d("log message >> ", "curent product store id = " + currentProduct.getStore_id());

        boolean added = dataManager.AddCartItem(typeCart,intCurr,false);
        if(!added){
            Toast.makeText(getApplicationContext(),"Could not add item.",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getApplicationContext(),"Item added.",Toast.LENGTH_SHORT).show();
        }
        Intent intent = new Intent(ProductView.this, Main.class);
        startActivity(intent);
        finish();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_product_view, menu);
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
