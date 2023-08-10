package detrack.zaryansgroup.com.detrack.activity.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import detrack.zaryansgroup.com.detrack.activity.Model.RegisterCustomerModel.RegisterdCustomerModel;
import detrack.zaryansgroup.com.detrack.R;
import detrack.zaryansgroup.com.detrack.activity.SQLlite.ZEDTrackDB;
import detrack.zaryansgroup.com.detrack.activity.utilites.Utility;

/**
 * Created by 6520 on 2/3/2016.
 */
public class CustomerDilogListAdapter extends BaseAdapter {
    Context context;
    List<RegisterdCustomerModel> list;
    ArrayList<RegisterdCustomerModel> newlist;

    LayoutInflater inflater;
    RegisterdCustomerModel model;
    ZEDTrackDB db;



    public CustomerDilogListAdapter(Context context, ArrayList<RegisterdCustomerModel> list) {
        this.context = context;
        this.list = list;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        newlist = new ArrayList<>(list);
        db = new ZEDTrackDB(context);

    }

    public void rSearchCustomers(ArrayList<RegisterdCustomerModel> rFilteredNameList) {
        this.list = rFilteredNameList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.customer_list,null);
        TextView customerTV = (TextView) view.findViewById(R.id.customer_name);
        TextView customerAddress = (TextView) view.findViewById(R.id.customer_address);
        LinearLayout llCustomerBg = view.findViewById(R.id.llCustomerBg);
        model = list.get(position);

        boolean checkIfCustomerVisited = db.rCheckIfCustomerVisitedByToday(model.getCustomer_id(), Utility.getCurrentDate());

        if(checkIfCustomerVisited==true)
        {

            llCustomerBg.setBackgroundColor(Color.parseColor("#58D68D"));
            customerTV.setTextColor(Color.WHITE);
            customerAddress.setTextColor(Color.WHITE);


        }else{
            
            customerTV.setTextColor(Color.BLACK);
            customerAddress.setTextColor(Color.BLACK);
        }


        customerTV.setText(model.getName());
        //todo title with name
        customerAddress.setText(model.getAddress());


        return view;
    }

}

