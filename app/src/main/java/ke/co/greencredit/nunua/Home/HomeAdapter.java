package ke.co.greencredit.nunua.Home;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ke.co.greencredit.nunua.Product.Product;
import ke.co.greencredit.nunua.R;

/**
 * Created by Fab on 10/10/2015.
 */
public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {
    Context context;
    HomeRecyclerAdapter homeRecyclerAdapter;
    private List<PopularItem> homeData;
    private List<Product> popularList;

    HomeAdapter(List<PopularItem> data, Context mContext) {
        this.homeData = data;
        this.context = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.home_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PopularItem popularItem = homeData.get(position);
        this.popularList = popularItem.getStorePopularItems();
        holder.tvName.setText(popularItem.getStoreName());
        homeRecyclerAdapter = new HomeRecyclerAdapter(context, this.popularList);
        holder.popularGrid.setLayoutManager(new GridLayoutManager(context, 3));
        holder.popularGrid.setAdapter(homeRecyclerAdapter);
        homeRecyclerAdapter.manageLayoutEvents(holder.popularGrid);

        int size = popularItem.getStorePopularItems().size();
        if (size >= 6) {
            homeRecyclerAdapter.viewStore(holder.btnViewMore, popularItem.getStoreId(), popularItem.getStoreName());
        } else {
            holder.btnViewMore.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return homeData.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        RecyclerView popularGrid;
        TextView btnViewMore;

        ViewHolder(View v) {
            super(v);
            tvName = (TextView) v.findViewById(R.id.tvStore);
            popularGrid = (RecyclerView) v.findViewById(R.id.popular_grid);
            btnViewMore = (TextView) v.findViewById(R.id.btnViewMore);
        }
    }

}

