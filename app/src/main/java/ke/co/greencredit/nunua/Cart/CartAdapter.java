package ke.co.greencredit.nunua.Cart;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import ke.co.greencredit.nunua.General.AppConstants;
import ke.co.greencredit.nunua.General.Const;
import ke.co.greencredit.nunua.General.DataManager;
import ke.co.greencredit.nunua.General.DatabaseHelper;
import ke.co.greencredit.nunua.Product.Product;
import ke.co.greencredit.nunua.R;
import ke.co.greencredit.nunua.app.AppController;

/**
 * Created by Fab on 10/2/2015.
 */
public class CartAdapter extends BaseAdapter {
    Context mContext;
    Activity mActivity;
    CartItem cartItem;
    List<TypeCart> cartItemArrayList;
    DataManager dataManager;
    DatabaseHelper databaseHelper;

    AppConstants mAppConstants = new AppConstants();
    SharedPreferences sharedPreferences;
    String cartId;
    com.android.volley.toolbox.ImageLoader imageLoader;

    TextView tvPrice;
    int price = 0;
    ContentValues contentValues;

    public CartAdapter(Context context, Activity activity,TextView tv) {
        mContext = context;
        mActivity = activity;
        cartItem = new CartItem();
        dataManager = new DataManager(mContext,1,"");
        databaseHelper = new DatabaseHelper(mContext);
        tvPrice = tv;

        String prefsName = AppConstants.PREFS_NAME;
        sharedPreferences = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE);


        cartId = sharedPreferences.getString("cartuuid", "");
        cartItemArrayList = dataManager.getAllCartItems(cartId);
        contentValues  = new ContentValues();
    }

    @Override
    public int getCount() {
        if (cartItemArrayList != null && cartItemArrayList.size() != 0) {
            return cartItemArrayList.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return cartItemArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TypeCart cart = cartItemArrayList.get(position);
        final Product product = cart.getCartItem();

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.cart_item, null);
        }


        TextView pName = (TextView) convertView.findViewById(R.id.product_name);
        pName.setText(product.getName());

        TextView pStore = (TextView) convertView.findViewById(R.id.store_name);
        pStore.setText(product.getCategory());

        TextView pQuantity = (TextView) convertView.findViewById(R.id.txt_amount);
//        pQuantity.setText(cartItem.getpQuantity());
        pQuantity.setText(product.getQuantity());

        TextView pPrice = (TextView) convertView.findViewById(R.id.price);
        int amount = Integer.parseInt(product.getUnit_price()) * Integer.parseInt(product.getQuantity());
        pPrice.setText("ksh "+amount+".00");


        NetworkImageView pImage = (NetworkImageView) convertView.findViewById(R.id.pImage);

//        pImage.setImageResource(product.getImage_path());
        imageLoader = AppController.getInstance().getImageLoader();

        pImage.setImageUrl(Const.URL_API + "Render/product/" + product.getId(), imageLoader);


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(mActivity);

                //Remove the dialog title
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                //setting custom layout for dialog
                dialog.setContentView(R.layout.custom_dialog);
                //adding text
                //Add the setters for the dialog labels and image

                final TextView tvQuantity = (TextView) dialog.findViewById(R.id.txtQuantity);
                tvQuantity.setText(product.getQuantity());
                final NetworkImageView imgProduce = (NetworkImageView) dialog.findViewById(R.id.img_produce);


                imgProduce.setImageUrl(Const.URL_API + "Render/product/" + product.getId(), imageLoader);

                final TextView tvProductName = (TextView) dialog.findViewById(R.id.txt_Title);
                final TextView tvPrice = (TextView) dialog.findViewById(R.id.txt_produce_price);
                tvProductName.setText(product.getName());
                tvPrice.setText("Price: ksh " + product.getUnit_price() + ".00");

                //Add and subtract item quantity
                Button btnAdd = (Button) dialog.findViewById(R.id.btnAdd);
                btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int curQuantity = Integer.parseInt(tvQuantity.getText().toString());
                        curQuantity++;
                        String finalValue = String.valueOf(curQuantity);
//                        String.valueOf(curQuantity);

                        tvQuantity.setText(finalValue);

                    }
                });

                Button btnSub = (Button) dialog.findViewById(R.id.btnSub);
                btnSub.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int curQuantity = Integer.parseInt(tvQuantity.getText().toString());
                        if (curQuantity != 0) {
                            curQuantity--;
                        }
                        String finalValue = String.valueOf(curQuantity);
//                        String.valueOf(curQuantity);
                        tvQuantity.setText(finalValue);
                    }
                });

                //Button actions
                Button btnRemove = (Button) dialog.findViewById(R.id.btnRemove);
                Button btnDone = (Button) dialog.findViewById(R.id.btnDone);
                btnDone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int q;
                        q = Integer.parseInt(tvQuantity.getText().toString());
                        TypeCart typeCart = new TypeCart(cartId, "active", product);
                        dataManager.AddCartItem(typeCart, q, true);
                        notifyDataSetChanged();
                        Intent intent = new Intent(mContext, Cart.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        if (dataManager.getItemsCount(cartId) == 0) {
                            sharedPreferences.edit().putBoolean("CartActive", false).apply();
                        }

                        dialog.dismiss();
                        mContext.startActivity(intent);
                    }
                });
                btnRemove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        boolean deleted = dataManager.deleteItem(product.getId());

                        if (deleted) {
                            cartItemArrayList = dataManager.getAllCartItems(cartId);
                            Intent intent = new Intent(mContext, Cart.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            mContext.startActivity(intent);

                            Toast.makeText(mContext, "Cart item removed.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mContext, "Could not remove cart item.", Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        return convertView;
    }

}
