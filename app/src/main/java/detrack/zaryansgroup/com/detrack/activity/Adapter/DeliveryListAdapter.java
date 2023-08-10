package detrack.zaryansgroup.com.detrack.activity.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

import detrack.zaryansgroup.com.detrack.activity.Model.DeliveryInfo;
import detrack.zaryansgroup.com.detrack.activity.utilites.Utility;
import detrack.zaryansgroup.com.detrack.R;

/**
 * Created by 6520 on 2/2/2016.
 */
public class DeliveryListAdapter extends BaseAdapter {
    Context context;
    ArrayList<DeliveryInfo> list;
    LayoutInflater inflater;
    DeliveryInfo model;
    boolean check;

    public DeliveryListAdapter(Context context, ArrayList<DeliveryInfo> list) {
        this.context = context;
        this.list = list;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        check=false;
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
        View view = inflater.inflate(R.layout.itemlayout,null);
        TextView addressTV = view.findViewById(R.id.addressTV);
        TextView StatusTV = view.findViewById(R.id.statusTV);
        TextView dileryTimeTV = view.findViewById(R.id.dilevery_collection_time);
        TextView deliverTo= view.findViewById(R.id.deliverToperson);
        TextView isSync= view.findViewById(R.id.syncTV);
        TextView date= view.findViewById(R.id.dateTV);
        TextView isNewOrderTV= view.findViewById(R.id.isNewOrderTV);
        CheckBox cb= view.findViewById(R.id.checkBox);
        model = list.get(position);

        if(check){
            cb.setVisibility(View.VISIBLE);
            if(model.isCBChecked()){
                cb.setChecked(true);
            }else if(!model.isCBChecked()) {
                cb.setChecked(false);
            }
        }

        addressTV.setText(model.getDelivery_address());
        if(model.getDelivery_status().equals("Rejected")){
            StatusTV.setTextColor(Color.RED);
        }
        else if(model.getDelivery_status().equals("Delivered")){
            StatusTV.setTextColor(Color.parseColor("#728C00"));
        }
        StatusTV.setText(model.getDelivery_status());
        date.setText(model.getDelivery_date().substring(0,10));
        dileryTimeTV.setText(model.getDelivery_start_time()+" To "+model.getDelivery_end_time());
        deliverTo.setText(model.getDeliver_to_name());
        if(model.getIsOrderRead()==0)
            isNewOrderTV.setVisibility(View.VISIBLE);
        if(model.getIsNewUpdate()==1){
            isNewOrderTV.setVisibility(View.VISIBLE);
            isNewOrderTV.setText("New Update");
        }
        if(model.getIsPod_sync().toString().equals("1")){
            isSync.setVisibility(View.VISIBLE);
            isSync.setTextColor(Color.parseColor("#E55B3C"));
            isSync.setText("POD Not Send");
        }
        else if(model.getIsPod_sync().toString().equals("2")){
            isSync.setVisibility(View.VISIBLE);
            isSync.setTextColor(Color.parseColor("#728C00")); //green color
            isSync.setText("POD Sent");
        }
        Utility.logCatMsg("Feed item getted :" + model.getDelivery_status() + " " + model.getDeliver_to_name());

        return view;
    }
    public void isChecked(boolean check){
        this.check=check;
    }
    public boolean IsCheck(){
        if(check){
            DeliveryInfo check_model=new DeliveryInfo();
            for(int i=0;i<list.size();i++){
                check_model=list.get(i);
            if(check_model.isCBChecked()){
                return true;
            }
            }
        }return false;
    }
}
