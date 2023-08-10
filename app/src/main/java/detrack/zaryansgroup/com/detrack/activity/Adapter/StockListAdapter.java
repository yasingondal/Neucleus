package detrack.zaryansgroup.com.detrack.activity.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import detrack.zaryansgroup.com.detrack.activity.Model.CompanyItemsModel.DeliveryItemModel;
import detrack.zaryansgroup.com.detrack.activity.SharedPreferences.SharedPrefs;
import detrack.zaryansgroup.com.detrack.activity.utilites.Utility;
import detrack.zaryansgroup.com.detrack.R;

/**
 * Created by 6520 on 3/31/2016.
 */
public class StockListAdapter extends BaseAdapter {
    Context context;
    ArrayList<DeliveryItemModel> list;
    LayoutInflater inflater;
    DeliveryItemModel model;
    EditText ctnPriceET, packPriceET, picesPriceET;
    TextView validationTV;

    public StockListAdapter(Context context, ArrayList<DeliveryItemModel> list) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {

//        View view = inflater.inflate(R.layout.item_list_row3,parent,false);
//        TextView tvItemName = view.findViewById(R.id.tvItemName);
//        final EditText etItemPrice = view.findViewById(R.id.etItemPrice);
//        final EditText etItemQty = view.findViewById(R.id.etItemQty);
//        final TextView tvItemTotalAmount = view.findViewById(R.id.tvItemTotalAmount);
//
//        tvItemTotalAmount.setText(list.get(position).getPcs_qty() * (int)list.get(position).getRetailPiecePrice() + "");
//        tvItemName.setText(list.get(position).getName());
//        etItemPrice.setText(String.valueOf((int)list.get(position).getRetailPiecePrice()));
//        if(list.get(position).getPcs_qty() != 0){
//            etItemQty.setText(String.valueOf(list.get(position).getPcs_qty()));
//        }
//
//        etItemQty.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                etItemQty.requestFocus();
//            }
//        });
//
//        etItemPrice.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if(count > 0){
//                    Log.d("itemprice change","true");
//                    list.get(position).setRetailPiecePrice(Float.parseFloat(etItemPrice.getText().toString()));
//                    int netValue = (int)list.get(position).getRetailPiecePrice() * list.get(position).getPcs_qty();
//                    tvItemTotalAmount.setText(String.valueOf(netValue));
//                }
//            }
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//
//
//        etItemQty.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//                if(count > 0){
//                    Log.d("itemQtyChange","true");
//                    list.get(position).setPcs_qty(Integer.parseInt(etItemQty.getText().toString()));
//                    int netValue = (int)list.get(position).getRetailPiecePrice() * list.get(position).getPcs_qty();
//                    tvItemTotalAmount.setText(String.valueOf(netValue));
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//        return view;

        //If layout is item_list_row2
        View view = inflater.inflate(R.layout.item_list_row2, null);
        TextView tvItemPrice = view.findViewById(R.id.tvItemPrice);
        TextView tvItemQty = view.findViewById(R.id.tvItemQty);
        TextView tvItemAmount = view.findViewById(R.id.tvItemAmount);
        TextView tvItemName = view.findViewById(R.id.tvItemName);
        TextView tvFoc = view.findViewById(R.id.tvFoc);
        LinearLayout customFocLinear = view.findViewById(R.id.customFocLinear);

        if(new SharedPrefs(context).getView().equals("secondView")){
            customFocLinear.setVisibility(View.GONE);
        }

        model = list.get(position);





        tvItemName.setText(model.getName());
        tvItemPrice.setText(String.valueOf((float) model.getWSCtnPrice()));
        tvItemQty.setText(String.valueOf(model.getPcs_qty()));
        tvItemAmount.setText(model.getPcs_qty() * (float)model.getWSCtnPrice() + "");

        return view;
    }

    private void SetPriceDialog(final int postion){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = inflater.inflate(R.layout.change_item_price_layout, null);
        ctnPriceET = dialogView.findViewById(R.id.ctnQtyET);
        packPriceET = dialogView.findViewById(R.id.pakQtyET);
        picesPriceET = dialogView.findViewById(R.id.piecesQtyET);
        validationTV = dialogView.findViewById(R.id.validation);
        ctnPriceET.setText(list.get(postion).getRetailCtnPrice() + "");
        packPriceET.setText(list.get(postion).getRetailPackPrice() + "");
        picesPriceET.setText(list.get(postion).getWSCtnPrice() + "");
        picesPriceET.setFocusable(true);
        builder.setView(dialogView)
                .setTitle("Update Item Prices")
                .setCancelable(true).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        })
                .setNegativeButton("Cancel", null);
        final AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    float crtnPrice = Float.parseFloat(ctnPriceET.getText().toString());
                    float packPrice = Float.parseFloat(packPriceET.getText().toString());
                    float picesPrice = Float.parseFloat(picesPriceET.getText().toString());
                    if (crtnPrice != 0 && packPrice != 0 && picesPrice != 0) {
                        list.get(postion).setCtn_qty(0);
                        list.get(postion).setPac_qty(0);
                        list.get(postion).setPcs_qty(0);
                        list.get(postion).setTotalwholeSalePrice(0);
                        list.get(postion).setTotalCostPrice(0);
                        list.get(postion).setTotalRetailPrice(0);
                        list.get(postion).setDelivered_Quantity(0);

                        list.get(postion).setItemTotalDeliverQtyInPieces(0);
                        list.get(postion).setFoc_percentage(0);
                        list.get(postion).setFoc_qty(0);
                        list.get(postion).setFoc_value(0);
                        list.get(postion).setItem_discount(0);

                        /////
                        list.get(postion).setRetailCtnPrice(crtnPrice);
                        list.get(postion).setRetailPackPrice(packPrice);
                        list.get(postion).setWSCtnPrice(picesPrice);
                        Utility.HideKeyBoard(v, context);
                        dialog.dismiss();
                    } else
                        validationTV.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    validationTV.setVisibility(View.VISIBLE);
                }

            }
        });

    }
}

