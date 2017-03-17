package ke.co.greencredit.nunua.Departments;

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
import java.util.Collections;
import java.util.List;

import ke.co.greencredit.nunua.General.Const;
import ke.co.greencredit.nunua.Home.HomeRecyclerAdapter;
import ke.co.greencredit.nunua.Product.Product;
import ke.co.greencredit.nunua.Product.ProductView;
import ke.co.greencredit.nunua.R;
import ke.co.greencredit.nunua.app.AppController;

/**
 * Created by Fab on 5/17/2016.
 */
public class DepartmentRecyclerAdapter extends RecyclerView.Adapter<DepartmentRecyclerAdapter.DepartmentViewHolder> {
    Context mContext;
    List<Product> departmentItems = Collections.emptyList();
    ImageLoader imageLoader;

    public DepartmentRecyclerAdapter(List<Product> storeItems, Context mContext, RecyclerView recyclerView) {
        this.departmentItems = storeItems;
        this.mContext = mContext;
        this.imageLoader = AppController.getInstance().getImageLoader();

        manageLayoutEvents(recyclerView);
    }

    @Override
    public DepartmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_item, parent, false);
        DepartmentViewHolder holder = new DepartmentViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(DepartmentViewHolder holder, int position) {
        holder.prodName.setText(departmentItems.get(position).getName());
        holder.prodPrice.setText("Ksh. " + departmentItems.get(position).getUnit_price());
        holder.prodUnitQuantity.setText("Per " + departmentItems.get(position).getUnit_measure());

        holder.prodImg.setImageUrl(Const.URL_API + "Render/product/" + departmentItems.get(position).getId(), imageLoader);

    }

    @Override
    public int getItemCount() {
        return departmentItems.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void manageLayoutEvents(RecyclerView recyclerView) {
        recyclerView.addOnItemTouchListener(
                new HomeRecyclerAdapter.RecyclerTouchListener(mContext, recyclerView, new HomeRecyclerAdapter.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {

                        Intent intent = new Intent(mContext, ProductView.class);
                        ArrayList<Product> productArray = new ArrayList<Product>();
                        productArray.add(departmentItems.get(position));
                        intent.putExtra("product", productArray);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);
                    }

                    @Override
                    public void onLongClick(View view, int position) {
//                        Toast.makeText(mContext, "Short press on item for more options", Toast.LENGTH_LONG).show();
                    }
                }));
    }

    static class DepartmentViewHolder extends RecyclerView.ViewHolder {
        NetworkImageView prodImg;
        TextView prodName;
        TextView prodPrice;
        TextView prodUnitQuantity;

        public DepartmentViewHolder(View itemView) {
            super(itemView);
            prodImg = (NetworkImageView) itemView.findViewById(R.id.imgItem);
            prodName = (TextView) itemView.findViewById(R.id.txtItemName);
            prodPrice = (TextView) itemView.findViewById(R.id.txtPrice);
            prodUnitQuantity = (TextView) itemView.findViewById(R.id.txtQuantity);

        }
    }
}
