package ke.co.greencredit.nunua.Home;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;
import java.util.List;

import ke.co.greencredit.nunua.DepartmentView.DepartmentView;
import ke.co.greencredit.nunua.General.AppConstants;
import ke.co.greencredit.nunua.General.Const;
import ke.co.greencredit.nunua.Product.Product;
import ke.co.greencredit.nunua.Product.ProductView;
import ke.co.greencredit.nunua.R;
import ke.co.greencredit.nunua.app.AppController;

/**
 * Created by eorenge on 4/29/16.
 */
public class HomeRecyclerAdapter  extends RecyclerView.Adapter<HomeRecyclerAdapter.MyViewHolder> {
    AppConstants mAppConstants = new AppConstants();
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    private List<Product> productsArray;
    private Context mContext;


    public HomeRecyclerAdapter(Context context, List<Product> products) {
        mContext = context;
        this.productsArray = products;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cell_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Product product = productsArray.get(position);
        holder.productName.setText(product.getName());
        holder.productPrice.setText("Ksh. " + product.getUnit_price());
        holder.productQuantity.setText("Per " + product.getUnit_measure());
        holder.thumbnail.setImageUrl(Const.URL_API + "Render/product/" + product.getId(), imageLoader);

    }

    @Override
    public int getItemCount() {
        return productsArray.size();
    }

    public void manageLayoutEvents(RecyclerView recyclerView) {
        recyclerView.addOnItemTouchListener(
                new HomeRecyclerAdapter.RecyclerTouchListener(mContext, recyclerView, new HomeRecyclerAdapter.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {

                        Intent intent = new Intent(mContext, ProductView.class);
                        ArrayList<Product> productArray = new ArrayList<Product>();
                        productArray.add(productsArray.get(position));
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


    public void viewStore(TextView button, final String storeId, final String name) {

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, DepartmentView.class);
                intent.putExtra("storeId", storeId);
                intent.putExtra("storeName", name);
                mContext.startActivity(intent);
            }
        });
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private HomeRecyclerAdapter.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final HomeRecyclerAdapter.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public NetworkImageView thumbnail;
        public TextView productName;
        public TextView productQuantity;
        public TextView productPrice;

        public MyViewHolder(View view) {
            super(view);
            thumbnail = (NetworkImageView) view.findViewById(R.id.imgItem);
            productName = (TextView) view.findViewById(R.id.txtItemName);
            productPrice = (TextView) view.findViewById(R.id.txtPrice);
            productQuantity = (TextView) view.findViewById(R.id.txtQuantity);

        }
    }
}
