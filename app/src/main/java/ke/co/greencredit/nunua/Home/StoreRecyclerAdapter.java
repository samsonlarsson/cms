package ke.co.greencredit.nunua.Home;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

import ke.co.greencredit.nunua.DepartmentView.DepartmentView;
import ke.co.greencredit.nunua.General.Const;
import ke.co.greencredit.nunua.R;
import ke.co.greencredit.nunua.StoreNavigation.Store;
import ke.co.greencredit.nunua.app.AppController;

/**
 * Created by Fab on 5/17/2016.
 */
public class StoreRecyclerAdapter extends RecyclerView.Adapter<StoreRecyclerAdapter.StoreViewHolder>  {
    Context mContext;
    ArrayList<Store> storeItems;


    public StoreRecyclerAdapter(ArrayList<Store> storeItems, Context mContext, RecyclerView recyclerView) {
        this.storeItems = storeItems;
        this.mContext = mContext;

        manageEvents(recyclerView);
    }

    @Override
    public StoreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.stores_list_item, parent, false);
        StoreViewHolder holder = new StoreViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(StoreViewHolder holder, int position) {
        holder.deptName.setText(storeItems.get(position).getStoreName());
        String id = storeItems.get(position).getStoreId();
        ImageLoader mImageLoader = AppController.getInstance().getImageLoader();

        holder.deptImage.setImageUrl(Const.URL_API + "Render/store/" + id, mImageLoader);

    }


    @Override
    public int getItemCount() {
        return storeItems.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    void manageEvents(RecyclerView recycler) {
        recycler.addOnItemTouchListener(new HomeRecyclerAdapter.RecyclerTouchListener(mContext, recycler, new HomeRecyclerAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(mContext, DepartmentView.class);
                String storeId = storeItems.get(position).getStoreId();
                String storeName = storeItems.get(position).getStoreName();
                intent.putExtra("storeId", storeId);
                intent.putExtra("storeName", storeName);
                mContext.startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    static class StoreViewHolder extends RecyclerView.ViewHolder {
        NetworkImageView deptImage;
        TextView deptName;

        public StoreViewHolder(View itemView) {
            super(itemView);
            deptImage = (NetworkImageView) itemView.findViewById(R.id.deptImg);
            deptName = (TextView) itemView.findViewById(R.id.dptName);
        }
    }
}
