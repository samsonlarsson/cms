package ke.co.greencredit.nunua.StoreNavigation;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import ke.co.greencredit.nunua.Home.Main;
import ke.co.greencredit.nunua.R;

/**
 * Created by Fab on 10/2/2015.
 */
public class DepartmentsAdapter extends BaseAdapter {
    Context mContext;
    ArrayList<Department> departmentArrayList;

    public DepartmentsAdapter(Context context, ArrayList<Department> departmentsList) {
        mContext = context;
        departmentArrayList = departmentsList;

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

//

        final TextView deptName = (TextView) convertView.findViewById(R.id.dptName);
        deptName.setText(department.getDeptName());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, Main.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("dptname",department.getDeptName());
                mContext.startActivity(intent);
            }
        });

        return convertView;
    }
}
