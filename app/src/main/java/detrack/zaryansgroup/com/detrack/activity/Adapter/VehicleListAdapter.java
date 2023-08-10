package detrack.zaryansgroup.com.detrack.activity.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;

import detrack.zaryansgroup.com.detrack.activity.Model.VehiclesModel.VehicleModel;
import detrack.zaryansgroup.com.detrack.R;

/**
 * Created by 6520 on 2/3/2016.
 */
public class VehicleListAdapter extends BaseAdapter {
    Context context;
    ArrayList<VehicleModel> list;
    LayoutInflater inflater;



    public VehicleListAdapter(Context context, ArrayList<VehicleModel> list) {
        this.context = context;
        this.list = list;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = inflater.inflate(R.layout.vehicle_row_layout,null);
        TextView regNo = (TextView) view.findViewById(R.id.title);
        RadioButton radioButton = (RadioButton) view.findViewById(R.id.radioButton);
        if(list.get(position).getIsSelected()){
            radioButton.setChecked(true);
        }
        regNo.setText(list.get(position).getRegNo());
        return view;
    }
}

