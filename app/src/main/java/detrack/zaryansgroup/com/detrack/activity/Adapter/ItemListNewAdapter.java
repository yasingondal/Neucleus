package detrack.zaryansgroup.com.detrack.activity.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import detrack.zaryansgroup.com.detrack.activity.SharedPreferences.SharedPrefs;
import detrack.zaryansgroup.com.detrack.activity.activites.SelectCustomerActivity;
import detrack.zaryansgroup.com.detrack.activity.Model.CompanyItemsModel.DeliveryItemModel;
import detrack.zaryansgroup.com.detrack.activity.utilites.Utility;
import detrack.zaryansgroup.com.detrack.R;

public class ItemListNewAdapter extends BaseAdapter {

    Context context;
    ArrayList<DeliveryItemModel> list;
    LayoutInflater inflater;
    DeliveryItemModel model;
    String currency;


    public ItemListNewAdapter(Context context, ArrayList<DeliveryItemModel> list) {

        this.context = context;
        this.list = list;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        currency =  new SharedPrefs(context).getCurrency();
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

    public int getListPosition(DeliveryItemModel model) {
        int position = -1;
        for (int i = 0; i < list.size(); i++) {
            if (model.getItem_id() == list.get(i).getItem_id()) {
                position = i;
                break;
            }
        }
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = inflater.inflate(R.layout.item_custom_layout, null);
        TextView tvProductName = view.findViewById(R.id.tvProductName);
        TextView tvPcsQty = view.findViewById(R.id.tvPcsQty);
        TextView tvPckQty = view.findViewById(R.id.tvPckQty);
        TextView tvCtvQty = view.findViewById(R.id.tvCtnQty);
        TextView tvPcsPrice = view.findViewById(R.id.tvPcsPrice);
        TextView tvPckPrice = view.findViewById(R.id.tvPckPrice);
        TextView tvCtnPrice = view.findViewById(R.id.tvCtnPrice);
        TextView tvTotalAmount = view.findViewById(R.id.tvTotalAmount);
        TextView tvFOC = view.findViewById(R.id.tvFOC);

        DeliveryItemModel model = list.get(position);

        tvProductName.setText(model.getName());
        tvPcsQty.setText(String.valueOf(model.getPcs_qty()));
        tvPckQty.setText(String.valueOf(model.getPac_qty()));
        tvCtvQty.setText(String.valueOf(model.getCtn_qty()));
        tvPcsPrice.setText(String.valueOf((int) model.getWSCtnPrice()));
        tvPckPrice.setText(String.valueOf((int) model.getRetailPackPrice()));
        tvCtnPrice.setText(String.valueOf((int) model.getRetailCtnPrice()));


        int totalAmount = 0;
        int pcsTotal = model.getPcs_qty() * (int) model.getWSCtnPrice();
        int pckTotal = model.getPac_qty() * (int) model.getRetailPackPrice();
        int ctnTotal = model.getCtn_qty() * (int) model.getRetailCtnPrice();
        if (SelectCustomerActivity.addOrder.equals("false")) {
            totalAmount = (pckTotal + pcsTotal + ctnTotal) + model.getFoc_value();
        } else {
            totalAmount = (pckTotal + pcsTotal + ctnTotal) - model.getFoc_value();

        }
        model.setNetTotalRetailPrice((float) totalAmount);
        tvTotalAmount.setText(String.valueOf(totalAmount));

        Log.d("adapterFocValue", model.getFoc_value() + " \t" + model.getFoc_qty());
        try {
            if (model.getFocType().equals("Value")) {
                tvFOC.setText(model.getFoc_value() + currency);
                Utility.logCatMsg("value");
            } else if (model.getFocType().equals("Qty")) {
                tvFOC.setText(model.getFoc_qty() + " qty");
            } else if (model.getFocType().equals("Percentage")) {
                tvFOC.setText(model.getFoc_percentage() + "% = " + model.getFoc_value() + currency);
            }
        } catch (NullPointerException ex) {
            ex.printStackTrace();

            if (model.getFoc_value() > 0) {
                tvFOC.setText(model.getFoc_value() + currency);
                Utility.logCatMsg("value");
            } else if (model.getFoc_qty() > 0) {
                tvFOC.setText(model.getFoc_qty() + " qty");
            } else if (model.getFoc_percentage() > 0) {
                tvFOC.setText(model.getFoc_percentage() + "% = " + model.getFoc_value() + currency);
            }


        }


        return view;
    }
}
