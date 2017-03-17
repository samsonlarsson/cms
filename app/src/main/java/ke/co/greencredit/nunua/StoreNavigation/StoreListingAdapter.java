package ke.co.greencredit.nunua.StoreNavigation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import ke.co.greencredit.nunua.General.AppConstants;
import ke.co.greencredit.nunua.General.ImageLoader;
import ke.co.greencredit.nunua.Home.Main;
import ke.co.greencredit.nunua.R;

/**
 * Created by Fab on 10/1/2015.
 */
public class StoreListingAdapter extends BaseAdapter {
    Context mContext;
    List<Store> storeArrayList;
    ArrayList<Department> departmentArrayList;
    ImageLoader imageLoader;
    AppConstants mAppConstants = new AppConstants();
    SharedPreferences sharedPreferences;

    public StoreListingAdapter(Context context, List<Store> storesList) {
        mContext = context;
        storeArrayList = storesList;
        imageLoader = new ImageLoader(mContext);
        sharedPreferences = mContext.getSharedPreferences(AppConstants.PREFS_NAME, Context.MODE_PRIVATE);


    }

    @Override
    public int getCount() {
        if (storeArrayList != null && storeArrayList.size() != 0) {
            return storeArrayList.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return storeArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Store store = storeArrayList.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.store_card, null);
        }

        //Set the store graphic
        ImageView storeImage = (ImageView) convertView.findViewById(R.id.graphic);
        imageLoader.DisplayImage(mAppConstants.imageURL+store.getStoreGraphic(),R.drawable.hourglass,storeImage);
//        storeImage.setImageResource(store.getStoreGraphic());
        departmentArrayList = store.getDepartments();

        final String storeName,id,graphic;
        storeName = store.getStoreName();
        id = store.getStoreId();
        graphic = store.getStoreGraphic();

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(mContext, Main.class);

                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("dptlist", departmentArrayList);
                intent.putExtra("storename",storeName);
                intent.putExtra("storeid",id);
                sharedPreferences.edit().putString("currstoreid",id).apply();
                sharedPreferences.edit().putString("currstorename",storeName).apply();
                intent.putExtra("storegraphic",graphic);
                mContext.startActivity(intent);

            }
        });

        return convertView;
    }
}
