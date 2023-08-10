package detrack.zaryansgroup.com.detrack.activity.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

import detrack.zaryansgroup.com.detrack.R;
import detrack.zaryansgroup.com.detrack.activity.Model.CompanyItemsModel.DeliveryItemModel;
import detrack.zaryansgroup.com.detrack.activity.Model.DeliveryInfo;
import detrack.zaryansgroup.com.detrack.activity.Model.OrderItemsListModel;
import detrack.zaryansgroup.com.detrack.activity.Model.RegisterCustomerModel.RegisterdCustomerModel;

public class FewItemAdapter extends RecyclerView.Adapter<FewItemAdapter.MyViewHolder> {

    Context context;
    ArrayList<DeliveryItemModel> orderList;
    int limit = 2;




    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtname, txtqty;



        public MyViewHolder(View view) {
            super(view);
            txtname = view.findViewById(R.id.itemname);
            txtqty = view.findViewById(R.id.itemqty);


        }
    }

    public FewItemAdapter(Context context, ArrayList<DeliveryItemModel> orderlist ) {

        this.context = context;
        this.orderList = orderlist;


    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fewitem_holder, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        DeliveryItemModel deliveryItemModel = orderList.get(position);
        holder.txtname.setText(deliveryItemModel.getName());
        holder.txtqty.setText(String.valueOf(deliveryItemModel.getFoc_qty()));


    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {


        if(orderList.size() > limit){
            return limit;
        }
        else
        {
            return orderList.size();
        }


    }
}
