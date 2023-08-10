package detrack.zaryansgroup.com.detrack.activity.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

import detrack.zaryansgroup.com.detrack.R;
import detrack.zaryansgroup.com.detrack.activity.Model.CompanyItemsModel.DeliveryItemModel;
import detrack.zaryansgroup.com.detrack.activity.Model.DeliveryInfo;
import detrack.zaryansgroup.com.detrack.activity.Model.OrderItemsListModel;
import detrack.zaryansgroup.com.detrack.activity.Model.RegisterCustomerModel.RegisterdCustomerModel;
import detrack.zaryansgroup.com.detrack.activity.SharedPreferences.SharedPrefs;

public class Undelivered_Adapter extends RecyclerView.Adapter<Undelivered_Adapter.MyViewHolder> {

    Context context;
    ArrayList<DeliveryInfo> orderList;
    ArrayList<RegisterdCustomerModel> customerList;
    ArrayList<OrderItemsListModel>mOrderItems;
    FewItemAdapter fewItemAdapter;
    String currency;



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtnameC, txtaddressC, txtphoneC,orderAmount,txtdate,txtorderid;
        public RecyclerView recyclerView;

        public MyViewHolder(View view) {
            super(view);
            txtnameC = view.findViewById(R.id.txtcustomername);
            txtaddressC = view.findViewById(R.id.txtaddress);
            txtphoneC = view.findViewById(R.id.txtphone);
            recyclerView = view.findViewById(R.id.recy_fewitem);
            orderAmount = view.findViewById(R.id.orderAmount);
            txtdate = view.findViewById(R.id.txtdate);
            txtorderid = view.findViewById(R.id.txtorderid);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));


        }
    }

    public Undelivered_Adapter(Context context, ArrayList<DeliveryInfo> orderList,
                               ArrayList<RegisterdCustomerModel> mCustomerList,
                               ArrayList<OrderItemsListModel>mOrderItems ) {

        this.context = context;
        this.orderList = orderList;
        this.customerList = mCustomerList;
        this.mOrderItems = mOrderItems;
        currency = new SharedPrefs(context).getCurrency();


    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.assignorder_holder, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {


        OrderItemsListModel orderItemsListModel =  mOrderItems.get(position);

        DeliveryInfo deliveryInfo = orderList.get(position);

        fewItemAdapter = new FewItemAdapter(context,orderItemsListModel.getmOrderItems());
        holder.recyclerView.setAdapter(fewItemAdapter);

        RegisterdCustomerModel registerdCustomerModel = customerList.get(position);
//        Log.d("nameofcust","="+registerdCustomerModel.getName())

        if(registerdCustomerModel != null)  holder.txtnameC.setText(registerdCustomerModel.getName());
        if(registerdCustomerModel != null)  holder.txtaddressC.setText(registerdCustomerModel.getAddress());
        Log.d("inadpterData","="+registerdCustomerModel.getName());

        if(deliveryInfo != null)  holder.orderAmount.setText(""+currency+" "+(Float.parseFloat(deliveryInfo.getTotal_Bill()) -
                Float.parseFloat(deliveryInfo.getDiscount())));
        if(deliveryInfo != null) holder.txtdate.setText(deliveryInfo.getDelivery_date().substring(0,10));
        holder.txtorderid.setText("#"+deliveryInfo.getOrderNumber());


    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }
}
