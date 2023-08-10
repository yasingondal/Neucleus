package detrack.zaryansgroup.com.detrack.activity.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import detrack.zaryansgroup.com.detrack.activity.Model.DeliveryInfo;
import detrack.zaryansgroup.com.detrack.activity.SQLlite.ZEDTrackDB;
import detrack.zaryansgroup.com.detrack.activity.utilites.Utility;
import detrack.zaryansgroup.com.detrack.R;
import timber.log.Timber;

/**
 * Created by 6520 on 4/12/2016.
 */
public class NewOrderListAdapter extends BaseAdapter {
    Context context;
    ArrayList<DeliveryInfo> list;
    LayoutInflater inflater;
    DeliveryInfo model;
    boolean check;
    String distName,SubDistName;

    ZEDTrackDB db;

    public NewOrderListAdapter(Context context, ArrayList<DeliveryInfo> list) {
        this.context = context;
        this.list = list;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        check=false;
        db = new ZEDTrackDB(context);
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

        LinearLayout llSubDistri = view.findViewById(R.id.llSubDistri);

        TextView distributorTV = view.findViewById(R.id.distributorTV);
        TextView subDistributorTV = view.findViewById(R.id.subDistributorTV);

        TextView StatusTV = view.findViewById(R.id.statusTV);
        TextView dileryTimeTV = view.findViewById(R.id.dilevery_collection_time);
        TextView deliverTo= view.findViewById(R.id.deliverToperson);
        TextView isSync= view.findViewById(R.id.syncTV);
        TextView date= view.findViewById(R.id.dateTV);
        CheckBox cb= view.findViewById(R.id.checkBox);

        model = list.get(position);


        Timber.d("The Distributor is "+list.get(position).getDistributorId());
        Timber.d("The SubDistributor is "+list.get(position).getSubDistributorId());


        distName = db.getDistributorName(model.getDistributorId());
        distributorTV.setText(distName);


        if(model.getSubDistributorId()>0){
            SubDistName = db.getSubDistributorName(model.getSubDistributorId());
            subDistributorTV.setText(SubDistName);
        }else{
            llSubDistri.setVisibility(View.GONE);
        }


        if(check){
            cb.setVisibility(View.VISIBLE);
           if(model.isCBChecked()){
              cb.setChecked(true);
           }else if(!model.isCBChecked()) {
             cb.setChecked(false);
           }
        }

        if(model.getDelivery_status().equals("Rejected")){
            StatusTV.setTextColor(Color.RED);
        }
        else if(model.getDelivery_status().equals("Delivered")){
            StatusTV.setTextColor(Color.parseColor("#728C00"));
        }
        StatusTV.setText(model.getDelivery_status());
        date.setText(model.getDelivery_date());
        //dileryTimeTV.setText(model.getDelivery_start_time()+" To "+model.getDelivery_end_time());
        deliverTo.setText(model.getDeliver_to_name());
        if(model.getIsPod_sync().toString().equals("1")){
            isSync.setVisibility(View.VISIBLE);
            isSync.setTextColor(Color.parseColor("#E55B3C"));
            isSync.setText("Order Not Send");
        }
        else if(model.getIsPod_sync().toString().equals("2")){
            isSync.setVisibility(View.VISIBLE);
            isSync.setTextColor(Color.parseColor("#728C00")); //green color
            isSync.setText("Order Sent");
        }
        Utility.logCatMsg("Feed item getted :" + model.getDelivery_status() + " " + model.getDeliver_to_name());

        Log.d("orderCoords","POD lat : "+model.getPod_lat()+"\n"+
                "POD lng: "+model.getPod_lng()+"\n"+
                "POB lat: "+model.getPob_lat()+"\n"+
                "POB lng: "+model.getPob_lng());

        return view;
    }
    public void isChecked(boolean check){
        this.check=check;
    }
}
