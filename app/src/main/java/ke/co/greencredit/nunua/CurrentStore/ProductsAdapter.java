package ke.co.greencredit.nunua.CurrentStore;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ke.co.greencredit.nunua.Product.Product;
import ke.co.greencredit.nunua.R;

/**
 * Created by Fab on 10/2/2015.
 */
public class ProductsAdapter extends BaseAdapter {
    Context mContext;
    ArrayList<Product> productArrayList;

    public ProductsAdapter(Context context) {
        mContext = context;
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
        Product product = productArrayList.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.cell_item, null);
        }

        ImageView productImg = (ImageView) convertView.findViewById(R.id.imgItem);
        productImg.setImageResource(Integer.parseInt(product.getImage_path()));

        TextView productName = (TextView) convertView.findViewById(R.id.txtItemName);
        productName.setText(product.getName());

        TextView productPrice = (TextView) convertView.findViewById(R.id.txtPrice);
        productPrice.setText(product.getUnit_price());

        TextView productQuantity = (TextView) convertView.findViewById(R.id.txtQuantity);
        productQuantity.setText(product.getQuantity());

        return convertView;
    }
}
