package detrack.zaryansgroup.com.detrack.activity.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

import detrack.zaryansgroup.com.detrack.R;
import detrack.zaryansgroup.com.detrack.activity.Model.CompanyItemsModel.DeliveryItemModel;
import detrack.zaryansgroup.com.detrack.activity.SharedPreferences.SharedPrefs;
import detrack.zaryansgroup.com.detrack.activity.activites.SelectCustomerActivity;
import detrack.zaryansgroup.com.detrack.activity.utilites.Utility;

public class OrderItemAdapterTest extends RecyclerView.Adapter<OrderItemAdapterTest.MyViewHolder> {

    Context context;
    ArrayList<DeliveryItemModel> orderList;
    String currency;

    private OnItemClick onItemClick;
    private OnItemLongClick onItemLongClick;
    public interface OnItemClick{
        void onClick(int position, View view);
    }

    public interface OnItemLongClick{
        void onLongClick(int position, View view);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvProductName,tvPcsQty,tvPckQty,tvCtvQty,
                tvPcsPrice,tvPckPrice,tvCtnPrice,tvTotalAmount, tvFOC;
        public CardView maincard;



        public MyViewHolder(View view) {
            super(view);

            tvProductName = view.findViewById(R.id.tvProductName);
            tvPcsQty = view.findViewById(R.id.tvPcsQty);
            tvPckQty = view.findViewById(R.id.tvPckQty);
            tvCtvQty = view.findViewById(R.id.tvCtnQty);
            tvPcsPrice = view.findViewById(R.id.tvPcsPrice);
            tvPckPrice = view.findViewById(R.id.tvPckPrice);
            tvCtnPrice = view.findViewById(R.id.tvCtnPrice);
            tvTotalAmount = view.findViewById(R.id.tvTotalAmount);
            tvFOC = view.findViewById(R.id.tvFOC);
            maincard = view.findViewById(R.id.maincard);

        }
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public void setOnItemLongClick(OnItemLongClick onItemLongClick) {
        this.onItemLongClick = onItemLongClick;
    }

    public OrderItemAdapterTest(Context context, ArrayList<DeliveryItemModel> orderlist ) {

        this.context = context;
        this.orderList = orderlist;
        currency =  new SharedPrefs(context).getCurrency();


    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_custom_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {



        DeliveryItemModel model = orderList.get(position);

        holder.tvProductName.setText(model.getItemDetail());
        holder.tvPcsQty.setText(String.valueOf(model.getPcs_qty()));
        holder.tvPckQty.setText(String.valueOf(model.getPac_qty()));
        holder.tvCtvQty.setText(String.valueOf(model.getCtn_qty()));
        holder.tvPcsPrice.setText(String.valueOf((int) model.getWSCtnPrice()));
        holder.tvPckPrice.setText(String.valueOf((int) model.getRetailPackPrice()));



        holder.maincard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClick != null)
                onItemClick.onClick(position,v);
            }
        });

        holder.maincard.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(onItemLongClick !=null)
                onItemLongClick.onLongClick(position,v);
                return true;
            }
        });


        int totalAmount = 0;
        int pcsTotal = model.getPcs_qty() * (int) model.getWSCtnPrice();
        Log.d("amount=>",String.valueOf(pcsTotal));
        int pckTotal = model.getPac_qty() * (int) model.getRetailPackPrice();
        Log.d("qty=>",String.valueOf(pckTotal));
        int ctnTotal = model.getCtn_qty() * (int) model.getRetailCtnPrice();
        Log.d("overall=>",String.valueOf(ctnTotal));


        if(model.getTaxCode().equalsIgnoreCase("3rd")){
            ctnTotal = model.getCtn_qty() * (int) model.getRetailPiecePrice();
            holder.tvCtnPrice.setText(String.valueOf((int) model.getRetailPiecePrice()));
        }else {
            holder.tvCtnPrice.setText(String.valueOf((int) model.getRetailCtnPrice()));
        }

        if (SelectCustomerActivity.addOrder.equals("false")) {
            totalAmount = (pckTotal + pcsTotal + ctnTotal) + model.getFoc_value();
        } else {
            totalAmount = (pckTotal + pcsTotal + ctnTotal) - model.getFoc_value();

        }
        model.setNetTotalRetailPrice((float) totalAmount);
        holder.tvTotalAmount.setText(String.valueOf(totalAmount));

        Log.d("adapterFocValue", model.getFoc_value() + " \t" + model.getFoc_qty());
        try {
            if (model.getFocType().equals("Value")) {
                holder.tvFOC.setText(model.getFoc_value() + currency);
                Utility.logCatMsg("value");
            } else if (model.getFocType().equals("Qty")) {
                holder.tvFOC.setText(model.getFoc_qty() + " qty");
            } else if (model.getFocType().equals("Percentage")) {
                holder.tvFOC.setText(model.getFoc_percentage() + "% = " + model.getFoc_value() + currency);
            }
        } catch (NullPointerException ex) {
            ex.printStackTrace();

            if (model.getFoc_value() > 0) {
                holder.tvFOC.setText(model.getFoc_value() + currency);
                Utility.logCatMsg("value");
            } else if (model.getFoc_qty() > 0) {
                holder.tvFOC.setText(model.getFoc_qty() + " qty");
            } else if (model.getFoc_percentage() > 0) {
                holder.tvFOC.setText(model.getFoc_percentage() + "% = " + model.getFoc_value() + currency);
            }


        }


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
