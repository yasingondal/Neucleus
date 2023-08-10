package detrack.zaryansgroup.com.detrack.activity.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import detrack.zaryansgroup.com.detrack.activity.Model.DropDownModel;
import detrack.zaryansgroup.com.detrack.R;

/**
 * Created by 6520 on 3/31/2016.
 */
public class DropDownListAdapter extends BaseAdapter {
    Context context;
    ArrayList<DropDownModel> list;
    LayoutInflater inflater;
    DropDownModel model;

    public DropDownListAdapter(Context context, ArrayList<DropDownModel> list) {
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
        View view = inflater.inflate(R.layout.drop_down_listlayout,null);
        TextView Name = (TextView) view.findViewById(R.id.NameTV);

        model = list.get(position);
        Name.setText(model.getName()+"");

        return view;
    }
}

