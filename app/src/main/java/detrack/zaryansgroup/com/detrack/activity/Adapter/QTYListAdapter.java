package detrack.zaryansgroup.com.detrack.activity.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import detrack.zaryansgroup.com.detrack.activity.Model.CompanyItemsModel.DeliveryItemModel;
import detrack.zaryansgroup.com.detrack.R;
import detrack.zaryansgroup.com.detrack.activity.SharedPreferences.SharedPrefs;

/**
 * Created by 6520 on 2/3/2016.
 */
public class QTYListAdapter extends BaseAdapter {
    Context context;
    ArrayList<DeliveryItemModel> list;
    LayoutInflater inflater;
    DeliveryItemModel model;
    String IsNew;
    String deliveryStatus;

    public QTYListAdapter(Context context, ArrayList<DeliveryItemModel> list, String IsNew, String deliveryStatus) {
        this.context = context;
        this.list = list;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.IsNew = IsNew;
        this.deliveryStatus = deliveryStatus;

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

        View view = inflater.inflate(R.layout.item_list_row, null);
        TextView foctv = view.findViewById(R.id.focTV);
        TextView item_name = view.findViewById(R.id.item_name);
        TextView ctn_qty = view.findViewById(R.id.ctn_qty);
        TextView pack_qty = view.findViewById(R.id.pack_qty);
        TextView pices_qty = view.findViewById(R.id.pieces_qty);

        TextView deliver_ctn_qty = view.findViewById(R.id.deliver_ctn_qty);
        TextView deliver_pack_qty = view.findViewById(R.id.deliver_pack_qty);
        TextView deliver_pices_qty = view.findViewById(R.id.deliver_pieces_qty);

        TextView reject_ctn_qty = view.findViewById(R.id.rej_ctn_qty);
        TextView reject_pack_qty = view.findViewById(R.id.reject_pack_qty);
        TextView reject_pices_qty = view.findViewById(R.id.reject_pieces_qty);

        TextView item_carton_Retail_price = view.findViewById(R.id.item_carton_price);
        TextView item_pack_Retail_price = view.findViewById(R.id.item_pack_price);
        TextView item_pieces_Retail_price = view.findViewById(R.id.item_piece_price);
        TextView item_ctn_price_total = view.findViewById(R.id.ctn_total);
        TextView item_pack_price_total = view.findViewById(R.id.pack_total);
        TextView item_pcs_price_total = view.findViewById(R.id.pcs_total);
        TextView price_total = view.findViewById(R.id.price_total);
        TextView itemDiscount = view.findViewById(R.id.discountTV);
        TextView itemPriceTotal = view.findViewById(R.id.totalPriceTV);
        TextView itemGstValue = view.findViewById(R.id.gstTV);
        TextView ItemNetTotal = view.findViewById(R.id.NetTotalTV);

        model = list.get(position);
        ctn_qty.setText(String.valueOf(model.getCtn_qty()));
        pack_qty.setText(String.valueOf(model.getPac_qty()));
        pices_qty.setText(String.valueOf(model.getPcs_qty()));

        if (deliveryStatus.equals("Booking")) {

            deliver_ctn_qty.setText("0");
            deliver_pack_qty.setText("0");
            deliver_pices_qty.setText("0");

            reject_ctn_qty.setText("0");
            reject_pack_qty.setText("0");
            reject_pices_qty.setText("0");

            item_ctn_price_total.setText(String.valueOf(model.getCtn_qty() * model.getRetailCtnPrice()));
            item_pack_price_total.setText(String.valueOf(model.getPac_qty() * model.getRetailPackPrice()));
            item_pcs_price_total.setText(String.valueOf(model.getPcs_qty() * model.getWSCtnPrice()));

        } else {
            deliver_ctn_qty.setText(String.valueOf(model.getDeliver_ctn_qty()));
            deliver_pack_qty.setText(String.valueOf(model.getDeliver_pac_qty()));
            deliver_pices_qty.setText(String.valueOf(model.getDeliver_pcs_qty()));

            reject_ctn_qty.setText(String.valueOf(model.getReject_ctn_qty()));
            reject_pack_qty.setText(String.valueOf(model.getReject_pac_qty()));
            reject_pices_qty.setText(String.valueOf(model.getReject_pcs_qty()));

            item_ctn_price_total.setText(String.valueOf(model.getDeliver_ctn_qty() * model.getRetailCtnPrice()));
            item_pack_price_total.setText(String.valueOf(model.getDeliver_pac_qty() * model.getRetailPackPrice()));
            item_pcs_price_total.setText(String.valueOf(model.getDeliver_pcs_qty() * model.getWSCtnPrice()));
        }
        price_total.setText(String.valueOf(model.getTotalRetailPrice()));
        foctv.setText(String.valueOf(model.getFoc()));
        if (model.getFoc_qty() > 0) {
            foctv.setText(model.getFoc_qty() + " qty");
        }
        if (model.getFoc_value() > 0) {
            foctv.setText(model.getFoc_value() + " "+new SharedPrefs(context).getCurrency());
        }
        if (model.getFoc_percentage() > 0) {
            foctv.setText(model.getFoc_percentage() + "% = " + model.getFoc_value() + " "+new SharedPrefs(context).getCurrency());
        }
        itemDiscount.setText(String.valueOf(model.getItem_discount()));
        itemPriceTotal.setText((model.getTotalRetailPrice() - model.getItem_discount() + ""));
        itemGstValue.setText(String.valueOf(model.getItemGstValue()));
        ItemNetTotal.setText(String.valueOf(model.getNetTotalRetailPrice()));
        item_name.setText(model.getName().toString());
        item_carton_Retail_price.setText(String.valueOf(model.getRetailCtnPrice()));
        item_pack_Retail_price.setText(String.valueOf(model.getRetailPackPrice()));
        item_pieces_Retail_price.setText(String.valueOf(model.getWSCtnPrice()));

        return view;
    }
}

