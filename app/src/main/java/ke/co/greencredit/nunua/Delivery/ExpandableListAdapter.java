package ke.co.greencredit.nunua.Delivery;

/**
 * Created by eorenge on 5/4/16.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ke.co.greencredit.nunua.General.AppConstants;
import ke.co.greencredit.nunua.General.Const;
import ke.co.greencredit.nunua.General.JSONHandler;
import ke.co.greencredit.nunua.General.ServiceHandler;
import ke.co.greencredit.nunua.R;

public class ExpandableListAdapter extends BaseExpandableListAdapter implements View.OnClickListener {

    List<DeliveryStoreDetails> cartItems;
    TypeDelivery typeDelivery;
    AlertDialog.Builder builder1;
    String orderId = "";
    String storeId = "";
    String uuid = "";
    JSONHandler jsonHandler = new JSONHandler();
    ServiceHandler serviceHandler = new ServiceHandler();
    Activity parent;
    ProgressDialog progressDialog;
    SharedPreferences settings;
    private Context _context;
    private ExpandableListView expLstView;


    public ExpandableListAdapter(Context context, TypeDelivery typeDelivery, ExpandableListView expandableListView, Activity activity) {
        this._context = context;

        this.cartItems = typeDelivery.getDeliveryStoreDetails();
        this.typeDelivery = typeDelivery;
        this.expLstView = expandableListView;
        builder1 = new AlertDialog.Builder(activity);
        this.parent = activity;
        settings = _context.getSharedPreferences(AppConstants.PREFS_NAME, Context.MODE_PRIVATE);
        uuid = settings.getString("uuid", "");
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.cartItems.get(groupPosition).getArrayPendigItems()
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {


        final String itemName = cartItems.get(groupPosition).getArrayPendigItems().get(childPosition).getProductName();
        final String quantity = cartItems.get(groupPosition).getArrayPendigItems().get(childPosition).getProductQuantity();
        final String unitPrice = cartItems.get(groupPosition).getArrayPendigItems().get(childPosition).getUnitPrice();
        final String unitMeasure = cartItems.get(groupPosition).getArrayPendigItems().get(childPosition).getUnitMeasure();
        final String unitQuantity = cartItems.get(groupPosition).getArrayPendigItems().get(childPosition).getUnitQuantity();


        RelativeLayout layout;


        LayoutInflater mInflater = (LayoutInflater) this._context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        layout = (RelativeLayout) mInflater.inflate(R.layout.list_item2, null);
        //check length of children
        int length = getChildrenCount(groupPosition);
        TextView txtListChild = (TextView) layout.findViewById(R.id.lblListItem);
        TextView txtQuantity = (TextView) layout.findViewById(R.id.lblListItemQuantity);
        TextView txtCost = (TextView) layout.findViewById(R.id.lblListItemCost);

        if (childPosition == 0) {
            txtListChild.setText("Item");
            txtQuantity.setText("Quantity");
            txtCost.setText("Price");
//            return layout;
        }

        if (childPosition == (length - 1)) {
            Button btnApproveDelivery = new Button(_context);
            btnApproveDelivery.setText("Approve");
            btnApproveDelivery.setTag(groupPosition + "");
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            lp.addRule(RelativeLayout.BELOW, R.id.lblListItem);
            lp.setMargins(0, 20, 0, 0);

            layout.addView(btnApproveDelivery, lp);
            btnApproveDelivery.setOnClickListener(this);
            //current group position

        }


        txtListChild.setText(itemName);
        txtQuantity.setText(quantity);
        txtCost.setText("kes: " + unitPrice);


        return layout;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.cartItems.get(groupPosition).getArrayPendigItems()
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.cartItems.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.cartItems.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = cartItems.get(groupPosition).getStoreName();
        String storeID = cartItems.get(groupPosition).getStoreId();
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setText(headerTitle);

        expLstView.expandGroup(groupPosition);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public void onClick(View v) {
        int pos = Integer.parseInt(v.getTag() + "");
        DeliveryStoreDetails currStore = cartItems.get(pos);
        orderId = typeDelivery.getOrderID();
        storeId = currStore.getStoreId();

        //init approve

        buildDialog("Order", "If the item(s) purchased is(are) as required and in condition, then approve the delivery.", 0);


    }


    void buildDialog(String title, String message, int type) {
        builder1.setMessage(message);
        builder1.setTitle(title);
        builder1.setCancelable(true);
        AlertDialog alert11;

        switch (type) {
            case 0:
                builder1.setPositiveButton("Approve",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                new ordersApprove().execute(orderId, storeId, uuid);
                            }
                        });
//                builder1.setNegativeButton("Reject",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//
//                            }
//                        });

                alert11 = builder1.create();
                alert11.show();

                break;
            default:

                break;
        }

    }

    private class ordersApprove extends AsyncTask<String, String, String> {
        String url;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(parent, "Orders", "Approving delivered order...", false, true);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            if (s != null)
                if (!s.equals("")) {
                    //json handler

                    String resp = jsonHandler.HandleResp4(s);
                    if (resp.equals("failed")) {
                        Toast.makeText(_context, "Failed to approve order.", Toast.LENGTH_SHORT).show();
                    } else if (resp.equals("Success")) {
                        Toast.makeText(_context, "Order successfully approved.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(parent, Delivery.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        _context.startActivity(intent);

                    } else {
                        Toast.makeText(_context, "Failed to approve order.", Toast.LENGTH_SHORT).show();
                    }
                }


        }

        @Override
        protected String doInBackground(String... strings) {

            url = Const.URL_API + "Orders/approve?orderId=" + strings[0] + "&storeId=" + strings[1] + "&uid=" + strings[2];
            Log.d("log Message>>>", url);

            String resp = serviceHandler.makeServiceCall(url, 1, null);
            Log.d("log Message>>>", resp);


            return resp;
        }
    }
}
