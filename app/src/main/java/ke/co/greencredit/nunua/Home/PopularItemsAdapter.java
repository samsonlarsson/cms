package ke.co.greencredit.nunua.Home;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ke.co.greencredit.nunua.General.AppConstants;
import ke.co.greencredit.nunua.General.ImageLoader;
import ke.co.greencredit.nunua.Product.Product;
import ke.co.greencredit.nunua.Product.ProductView;
import ke.co.greencredit.nunua.R;

public class PopularItemsAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<Product> productArrayList;
    ImageLoader imageLoader;
    AppConstants mAppConstants = new AppConstants();



    public  PopularItemsAdapter(Context context, ArrayList<Product> products){
        mContext = context;
        productArrayList = products;
        imageLoader = new ImageLoader(mContext);

    }

    @Override
    public int getCount() {
        if (productArrayList !=null && productArrayList.size() != 0){
            return productArrayList.size();
        }else {
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
        if (convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.cell_item, null);
        }

        ImageView productImg = (ImageView) convertView.findViewById(R.id.imgItem);
        if(product.getName().equals("Tomato")){
            productImg.setImageResource(R.drawable.badge);
        }else{
            imageLoader.DisplayImage(mAppConstants.imageURL+product.getImage_path(),R.drawable.hourglass,productImg);
        }

        TextView productName = (TextView) convertView.findViewById(R.id.txtItemName);
        productName.setText(product.getName());

        TextView productPrice = (TextView) convertView.findViewById(R.id.txtPrice);
        productPrice.setText("ksh. " + product.getUnit_price() + ".00 / "+product.getUnit_measure());

        TextView productQuantity = (TextView) convertView.findViewById(R.id.txtQuantity);
//        productQuantity.setText(product.getUnit_measure());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ProductView.class);
                ArrayList<Product> productArray = new ArrayList<Product>();
                productArray.add(product);
                intent.putExtra("product", productArray);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);

            }
        });
        return convertView;
    }
}