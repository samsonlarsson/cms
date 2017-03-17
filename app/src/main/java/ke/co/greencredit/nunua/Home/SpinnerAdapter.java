package ke.co.greencredit.nunua.Home;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;
import java.util.List;

import ke.co.greencredit.nunua.R;

/**
 * Created by eorenge on 5/4/16.
 */
public class SpinnerAdapter extends BaseAdapter {
    Context mContext;
    List<SpinnerElement> lstSpinner;
    String item;
    ImageLoader imageLoader;

    public SpinnerAdapter(Context context, List<SpinnerElement> spinnerItems, ImageLoader imageLoader0){
        mContext = context;
        lstSpinner = spinnerItems;
        imageLoader = imageLoader0;
    }

    @Override
    public int getCount() {
        if ( lstSpinner != null && lstSpinner.size() != 0){
            return lstSpinner.size();
        }else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return lstSpinner.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null){
            SpinnerElement spinnerElement = lstSpinner.get(position);
            convertView = LayoutInflater.from(mContext).inflate(R.layout.custom_spinner, null);
            NetworkImageView banner = (NetworkImageView)convertView.findViewById(R.id.banner);
            TextView tvDesc = (TextView)convertView.findViewById(R.id.tvDescription);
//            banner.setImageUrl();
            banner.setImageResource(spinnerElement.getImageID());
            tvDesc.setText(spinnerElement.getDescription());
        }
        return convertView;
    }
}

