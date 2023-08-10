package detrack.zaryansgroup.com.detrack.activity.Adapter;

import android.content.Context;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import detrack.zaryansgroup.com.detrack.activity.Model.EmptyBottleModel;
import detrack.zaryansgroup.com.detrack.R;

public class BottleReportsRecyclerViewAdapter extends RecyclerView.Adapter<BottleReportsRecyclerViewAdapter.MyViewHolder> {

    ArrayList<EmptyBottleModel> bottlesList;
    Context context;
    LayoutInflater inflater;

    public BottleReportsRecyclerViewAdapter(Context context, ArrayList<EmptyBottleModel> bottlesList){

        this.context = context;
        this.bottlesList = bottlesList;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.bottle_report_custom_layout, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        EmptyBottleModel model = bottlesList.get(position);
        holder.tvCustomNo.setText(String.valueOf(position+1));
        holder.tvCustomProductName.setText(model.getProductName());
        holder.tvCustomBottleQty.setText(""+model.getNumberOfBottles());
        holder.tvCustomDeliveredQty.setText(""+model.getDeliveredQty());
    }

    @Override
    public int getItemCount() {
        return bottlesList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvCustomProductName, tvCustomBottleQty,tvCustomDeliveredQty,tvCustomNo;
        public MyViewHolder(View itemView) {
            super(itemView);

            tvCustomProductName = itemView.findViewById(R.id.tvCustomProductName);
            tvCustomBottleQty = itemView.findViewById(R.id.tvCustomBottleQty);
            tvCustomDeliveredQty = itemView.findViewById(R.id.tvCustomDeliveredQty);
            tvCustomNo = itemView.findViewById(R.id.tvCustomNo);
        }
    }
}
