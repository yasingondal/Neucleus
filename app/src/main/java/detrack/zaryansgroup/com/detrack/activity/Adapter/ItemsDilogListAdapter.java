package detrack.zaryansgroup.com.detrack.activity.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import detrack.zaryansgroup.com.detrack.R;
import detrack.zaryansgroup.com.detrack.activity.Model.CompanyItemsModel.DeliveryItemModel;
import detrack.zaryansgroup.com.detrack.activity.SharedPreferences.SharedPrefs;
import timber.log.Timber;

/**
 * Created by 6520 on 2/3/2016.
 */
public class ItemsDilogListAdapter extends BaseAdapter {
    Context context;
    ArrayList<DeliveryItemModel> list;
    LayoutInflater inflater;
    DeliveryItemModel model;
    private ItemDialogCallbacks hItemDialogCallbacks;


    public ItemsDilogListAdapter(Context context, ArrayList<DeliveryItemModel> list) {
        this.context = context;
        this.list = list;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


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
        View view = inflater.inflate(R.layout.items_search_list, null);
        TextView item_nameTV = view.findViewById(R.id.item_name);
        TextView tvItemCode = view.findViewById(R.id.tvItemCode);


        //todo point1
        view.setOnClickListener(v -> {

            hItemDialogCallbacks.hOnItemClicked(
                    position,
                    list.get(position)
            );

        });

        TextView tvItemDisplayPrice = view.findViewById(R.id.tvItemDisplayPrice);
        model = list.get(position);
        item_nameTV.setText(model.getItemDetail());
        tvItemCode.setText(model.getSKU());

        if (new SharedPrefs(context).getView().equalsIgnoreCase("secondView")) {

            if(new SharedPrefs(context).getDesignation().equalsIgnoreCase("Order Booker")){
                tvItemDisplayPrice.setText("--");
            }else{
                tvItemDisplayPrice.setText(String.valueOf(model.getRetailPiecePrice()));
            }


        } else {
            if(new SharedPrefs(context).getDesignation().equalsIgnoreCase("Order Booker")){
                tvItemDisplayPrice.setText("--");
            }else{
                tvItemDisplayPrice.setText(String.valueOf(model.getRetailPiecePrice()));
            }

        }


        if(new SharedPrefs(context).getDesignation().equalsIgnoreCase("Order Booker")){
            tvItemDisplayPrice.setText("--");
        }else{
            tvItemDisplayPrice.setText(String.valueOf(model.getRetailPiecePrice()));
        }


        return view;
    }

    public void hSetItemDialogCallback(ItemDialogCallbacks itemDialogCallbacks) {
        hItemDialogCallbacks = itemDialogCallbacks;
    }

    public interface ItemDialogCallbacks {
        public void hOnItemClicked(int position, DeliveryItemModel deliveryItemModel);
    }

}

