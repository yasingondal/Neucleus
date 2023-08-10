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
import java.util.List;

import detrack.zaryansgroup.com.detrack.R;
import detrack.zaryansgroup.com.detrack.activity.Model.CompanyItemsModel.DeliveryItemModel;
import detrack.zaryansgroup.com.detrack.activity.SharedPreferences.SharedPrefs;

public class items_list_in_an_order_adapter extends RecyclerView.Adapter<items_list_in_an_order_adapter.itemsviewholder> {

    List<DeliveryItemModel> selectedItemList;
    Context context;

    SharedPrefs sharedPrefs;

    public items_list_in_an_order_adapter(List<DeliveryItemModel> selectedItemList, Context context) {
        this.selectedItemList = selectedItemList;
        this.context = context;
        sharedPrefs = new SharedPrefs(context);
    }


    @NonNull
    @Override
    public itemsviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new itemsviewholder(
                LayoutInflater.from(context).inflate(
                        R.layout.pod_single_row_item,
                        parent,
                        false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull itemsviewholder holder, int position) {


        DeliveryItemModel model = selectedItemList.get(position);



        holder.tvItemDescription.setText(model.getName().toLowerCase());
        holder.tvItemPcs.setText(String.valueOf(model.getCtn_qty()));

        if(new SharedPrefs(context).getDesignation().equalsIgnoreCase("Order Booker")){
            holder.tvPeritem.setText("--");
            holder.tvItemnettotal.setText("--");
        }else{
            holder.tvPeritem.setText(String.valueOf(model.getPrice()));
            holder.tvItemnettotal.setText(String.valueOf(model.getTotalRetailPrice()));
        }

    }

    @Override
    public int getItemCount() {
        return selectedItemList.size();
    }

    class itemsviewholder extends RecyclerView.ViewHolder{

        TextView tvItemDescription,tvItemPcs,tvPeritem,tvItemnettotal;

       public itemsviewholder(@NonNull View itemView) {
           super(itemView);

           tvItemDescription = itemView.findViewById(R.id.tvpodItemDescription);
           tvItemPcs = itemView.findViewById(R.id.tvpodItemPcs);
           tvPeritem = itemView.findViewById(R.id.tvpodPeritem);
           tvItemnettotal = itemView.findViewById(R.id.tvpodItemnettotal);
       }
   }

}
