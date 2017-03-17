package ke.co.greencredit.nunua.DepartmentView;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

import ke.co.greencredit.nunua.Departments.DepartmentActivity;
import ke.co.greencredit.nunua.General.Const;
import ke.co.greencredit.nunua.R;
import ke.co.greencredit.nunua.StoreNavigation.Department;
import ke.co.greencredit.nunua.app.AppController;

/**
 * Created by Fab on 5/17/2016.
 */
public class DepartmentViewAdapter extends BaseAdapter {
    Context mContext;
    ArrayList<Department> departmentArrayList;
    NetworkImageView networkImageView;
    ImageLoader imageLoader;
    String storeId1;

    public DepartmentViewAdapter(Context context, ArrayList<Department> departmentsList, String storeId) {
        mContext = context;
        departmentArrayList = departmentsList;
        imageLoader = AppController.getInstance().getImageLoader();
        storeId1 = storeId;
    }

    @Override
    public int getCount() {
        if (departmentArrayList != null && departmentArrayList.size() != 0) {
            return departmentArrayList.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return departmentArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Department department = departmentArrayList.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.dept_item, null);
        }

        final TextView deptName = (TextView) convertView.findViewById(R.id.dptName);
        networkImageView = (NetworkImageView) convertView.findViewById(R.id.graphicDpt);
        deptName.setText(department.getDeptName());
        networkImageView.setImageUrl(Const.URL_API + "Render/department/" + department.getDeptID(), imageLoader);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DepartmentActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("dptname", department.getDeptName());
                intent.putExtra("storeId", storeId1);
                intent.putExtra("dptId", department.getDeptID());

                mContext.startActivity(intent);
            }
        });

        return convertView;
    }
}
