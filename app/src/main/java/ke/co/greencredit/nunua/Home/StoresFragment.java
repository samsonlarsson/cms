package ke.co.greencredit.nunua.Home;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ke.co.greencredit.nunua.General.Const;
import ke.co.greencredit.nunua.General.JSONHandler;
import ke.co.greencredit.nunua.General.ServiceHandler;
import ke.co.greencredit.nunua.R;
import ke.co.greencredit.nunua.StoreNavigation.Store;

/**
 * Created by Fab on 5/9/2016.
 */
public class StoresFragment extends Fragment {
    Context mContext;

    ProgressDialog progressDialog;
    JSONHandler jsonHandler;
    List<Store> arrayList;
    String url, uid;

    ServiceHandler serviceHandler;
    SharedPreferences settings;
    ArrayList<Store> storeArrayList;
    RecyclerView departmentsListRecycler;

    public StoresFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.stores_fragment, container, false);

        departmentsListRecycler = (RecyclerView) view.findViewById(R.id.departmentsList);


        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        settings = mContext.getSharedPreferences(Const.PREFS_NAME, Context.MODE_PRIVATE);

        jsonHandler = new JSONHandler();
        serviceHandler = new ServiceHandler();
        uid = settings.getString("uuid", "");

        String arr = settings.getString("storearray", "");

        storeArrayList = jsonHandler.HandleStores(arr, mContext);


//        new LoadStores().execute(uid);


        StoreRecyclerAdapter adapter = new StoreRecyclerAdapter(storeArrayList, mContext, departmentsListRecycler);
        departmentsListRecycler.setAdapter(adapter);
        departmentsListRecycler.setLayoutManager(new LinearLayoutManager(mContext));

        //Animations when populating
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(1000);
        itemAnimator.setRemoveDuration(1000);
        departmentsListRecycler.setItemAnimator(itemAnimator);


    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }


    @Override
    public void onResume() {
        super.onResume();
//        Toast.makeText(mContext,"Resume",Toast.LENGTH_SHORT).show();
    }


}
