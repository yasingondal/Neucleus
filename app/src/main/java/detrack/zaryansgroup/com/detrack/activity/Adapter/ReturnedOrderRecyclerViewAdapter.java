package detrack.zaryansgroup.com.detrack.activity.Adapter;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import detrack.zaryansgroup.com.detrack.activity.activites.ReturnedOrderDetailActivity;
import detrack.zaryansgroup.com.detrack.activity.Model.RegisterCustomerModel.RegisterdCustomerModel;
import detrack.zaryansgroup.com.detrack.R;

public class ReturnedOrderRecyclerViewAdapter extends RecyclerView.Adapter<ReturnedOrderRecyclerViewAdapter.MyViewHolder> {

    List<RegisterdCustomerModel> customerModelList;
    Context context;
    LayoutInflater inflater;

    public ReturnedOrderRecyclerViewAdapter(Context context,  List<RegisterdCustomerModel> customerModelList){
        this.context = context;
        this.customerModelList = customerModelList;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.returned_order_custom_layout, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        final RegisterdCustomerModel model = customerModelList.get(position);
        holder.tvCustomName.setText("Name: "+model.getName());
        holder.tvCustomInv.setText("Invoice Number: "+model.getServerOrderId());
        holder.tvCustomDate.setText("Date: "+model.getDate());
        holder.tvCustomTotal.setText("Net Total: "+model.getNetTotal());
        holder.tvCustomSalesMode.setText("Sales Mode: "+model.getCashMode());
        holder.returnLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ReturnedOrderDetailActivity.class);
                intent.putExtra("orderMaster", model);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return customerModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvCustomName, tvCustomInv, tvCustomDate, tvCustomTotal, tvCustomSalesMode;
        LinearLayout returnLinear;
        public MyViewHolder(View itemView) {
            super(itemView);

            tvCustomName = itemView.findViewById(R.id.tvCustomName);
            tvCustomInv = itemView.findViewById(R.id.tvCustomInv);
            tvCustomDate = itemView.findViewById(R.id.tvCustomDate);
            tvCustomTotal = itemView.findViewById(R.id.tvCustomTotal);
            tvCustomSalesMode = itemView.findViewById(R.id.tvCustomSalesMode);
            returnLinear = itemView.findViewById(R.id.returnLinear);
        }
    }
}
