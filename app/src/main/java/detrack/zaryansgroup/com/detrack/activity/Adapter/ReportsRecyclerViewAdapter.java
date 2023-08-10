package detrack.zaryansgroup.com.detrack.activity.Adapter;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import detrack.zaryansgroup.com.detrack.activity.activites.EmptyBottlesReport;
import detrack.zaryansgroup.com.detrack.activity.activites.LedgerActivity;
import detrack.zaryansgroup.com.detrack.activity.activites.SalesReportActivity;
import detrack.zaryansgroup.com.detrack.activity.Model.ReportModel;
import detrack.zaryansgroup.com.detrack.R;

public class ReportsRecyclerViewAdapter extends RecyclerView.Adapter<ReportsRecyclerViewAdapter.MyViewHolder> {

    Context context;
    List<ReportModel> reportModelList;
    LayoutInflater inflater;

    public ReportsRecyclerViewAdapter(Context context, List<ReportModel> reportModelList) {
        this.context = context;
        this.reportModelList = reportModelList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.report_custom_layout, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final ReportModel model = reportModelList.get(position);

        holder.ivReport.setImageResource(model.getReportImageId());
        holder.tvReportName.setText(model.getReportTitle());
        holder.reportRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkReportName(model);
            }
        });
    }

    @Override
    public int getItemCount() {
        return reportModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView ivReport;
        TextView tvReportName;
        RelativeLayout reportRelativeLayout;
        public MyViewHolder(View itemView) {
            super(itemView);

            ivReport = itemView.findViewById(R.id.ivReport);
            tvReportName = itemView.findViewById(R.id.tvReportName);
            reportRelativeLayout = itemView.findViewById(R.id.reportRelativeLayout);
        }
    }

    private void checkReportName(ReportModel model){
        if(model.getReportTitle().equals("Customer Ledger")){
            Intent intent = new Intent(context, LedgerActivity.class);
            context.startActivity(intent);
        }
        else if (model.getReportTitle().equals("Sales Report")){
            Intent intent = new Intent(context, SalesReportActivity.class);
            context.startActivity(intent);
        }
        else if(model.getReportTitle().equals("Empty Bottles")){

            Intent intent = new Intent(context, EmptyBottlesReport.class);
            context.startActivity(intent);
        }
    }
}
