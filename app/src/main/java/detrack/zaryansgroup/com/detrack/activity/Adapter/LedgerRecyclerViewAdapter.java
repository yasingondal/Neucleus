package detrack.zaryansgroup.com.detrack.activity.Adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import detrack.zaryansgroup.com.detrack.activity.Model.LedgerModel;
import detrack.zaryansgroup.com.detrack.R;

public class LedgerRecyclerViewAdapter extends RecyclerView.Adapter<LedgerRecyclerViewAdapter.MyViewHolder>{

    Context context;
    List<LedgerModel> ledgerModelList;
    LayoutInflater inflater;

    public LedgerRecyclerViewAdapter(Context context, List<LedgerModel> ledgerModelList){

        this.context = context;
        this.ledgerModelList = ledgerModelList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.ledger_custom_layout,parent,false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        LedgerModel model = ledgerModelList.get(position);
        if(model.getDescription().equals("Opening Balance")){

            holder.tvCustomSr.setText("");
            holder.tvCustomDate.setText(model.getDate());
            holder.tvCustomDescription.setText(model.getDescription());
            holder.tvCustomDebit.setText("OB");
            holder.tvCustomCredit.setText("");
            holder.tvCustomBalance.setText(String.valueOf(model.getBalance()));

        }
        else{
            holder.tvCustomSr.setText(String.valueOf(model.getNo()));
            holder.tvCustomDate.setText(model.getDate());
            holder.tvCustomDescription.setText(model.getDescription());
            holder.tvCustomDebit.setText(String.valueOf(model.getDebit()));
            holder.tvCustomCredit.setText(String.valueOf(model.getCredit()));
            holder.tvCustomBalance.setText(String.valueOf(model.getBalance()));
        }

    }

    @Override
    public int getItemCount() {
        return ledgerModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvCustomSr,tvCustomDate,tvCustomDescription,tvCustomDebit,tvCustomCredit,tvCustomBalance;
        public MyViewHolder(View itemView) {
            super(itemView);

            tvCustomSr = itemView.findViewById(R.id.tvCustomSr);
            tvCustomDate = itemView.findViewById(R.id.tvCustomDate);
            tvCustomDescription = itemView.findViewById(R.id.tvCustomDescription);
            tvCustomDebit = itemView.findViewById(R.id.tvCustomDebit);
            tvCustomCredit = itemView.findViewById(R.id.tvCustomCredit);
            tvCustomBalance = itemView.findViewById(R.id.tvCustomBalance);
        }
    }
}
