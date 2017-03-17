package ke.co.greencredit.nunua.History;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import ke.co.greencredit.nunua.R;

/**
 * Created by Fab on 10/11/2015.
 */
public class HistoryAdapter extends BaseAdapter{
    Context mContext;
    ArrayList<TypeCartHistory> itemsArrayList;

    public HistoryAdapter(Context context, ArrayList<TypeCartHistory> historyArrayList) {
        mContext = context;
        itemsArrayList = historyArrayList;

    }

    @Override
    public int getCount() {
        if ( itemsArrayList != null && itemsArrayList.size() != 0){
            return itemsArrayList.size();
        }else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return itemsArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TypeCartHistory history = itemsArrayList.get(position);

        if (convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.history_cell_item, null);
        }

        TextView cartId = (TextView) convertView.findViewById(R.id.historyCartID);
        TextView orderId = (TextView) convertView.findViewById(R.id.historyOrderID);
        TextView orderStatus = (TextView) convertView.findViewById(R.id.historyOrderStatus);
        TextView totalAmount = (TextView) convertView.findViewById(R.id.historyTotalAmount);
        TextView pendingAmount = (TextView) convertView.findViewById(R.id.historyPendingAmount);

        cartId.setText(history.getCartId());
        orderId.setText(history.getOrder_id());
        orderStatus.setText(history.getOrder_status());
        totalAmount.setText(history.getTotal_amount());
        pendingAmount.setText(history.getAmount_left());

        return convertView;
    }
}
