package detrack.zaryansgroup.com.detrack.activity.Adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import detrack.zaryansgroup.com.detrack.activity.Model.SalesModel;
import detrack.zaryansgroup.com.detrack.R;

public class SalesReportRecyclerViewAdapter extends RecyclerView.Adapter<SalesReportRecyclerViewAdapter.MyViewHolder> {

    List<SalesModel> salesModelList;
    Context context;
    LayoutInflater inflater;

    public SalesReportRecyclerViewAdapter(Context context, List<SalesModel> salesModelList){
        this.context = context;
        this.salesModelList = salesModelList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.sales_report_custom_layout,parent,false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        SalesModel model = salesModelList.get(position);
        holder.tvSalesDate.setText(model.getSalesDate());
        holder.tvSalesDescription.setText(model.getSalesDescription());
        holder.tvSalesMode.setText(model.getSalesMode());
        holder.tvInvNo.setText(String.valueOf(model.getSalesInvNo()));
        holder.tvSalesAmount.setText(String.valueOf(model.getSalesAmount()));
        holder.tvSalesQty.setText(String.valueOf(model.getSalesQty()));
    }

    @Override
    public int getItemCount() {
        return salesModelList.size();
    }

    public class MyViewHolder  extends RecyclerView.ViewHolder{
        TextView tvSalesDate,tvInvNo,tvSalesQty,tvSalesAmount,tvSalesDescription,tvSalesMode;
        public MyViewHolder(View itemView) {
            super(itemView);

            tvSalesDate = itemView.findViewById(R.id.tvSalesDate);
            tvInvNo = itemView.findViewById(R.id.tvInvNo);
            tvSalesQty = itemView.findViewById(R.id.tvSalesQty);
            tvSalesAmount = itemView.findViewById(R.id.tvSalesAmount);
            tvSalesDescription = itemView.findViewById(R.id.tvSalesDescription);
            tvSalesMode = itemView.findViewById(R.id.tvSalesMode);
        }
    }
}
