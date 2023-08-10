package detrack.zaryansgroup.com.detrack.activity.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import detrack.zaryansgroup.com.detrack.activity.Model.CompanyItemsModel.DeliveryItemModel;
import detrack.zaryansgroup.com.detrack.activity.Model.RegisterCustomerModel.RegisterdCustomerModel;
import detrack.zaryansgroup.com.detrack.activity.SQLlite.ZEDTrackDB;
import detrack.zaryansgroup.com.detrack.activity.utilites.Utility;
import detrack.zaryansgroup.com.detrack.R;

public class OrderDetailRecyclerViewAdapter extends RecyclerView.Adapter<OrderDetailRecyclerViewAdapter.MyViewHolder> {

    List<DeliveryItemModel> deliveryItemList;
    Context context;
    LayoutInflater inflater;
    String FocType = "Percentage", QtyType = "Piece";
    EditText focET, ctnQtyET, packQtyET, picesQtyET;
    RegisterdCustomerModel getMasterObject;
    int netTotal = 0;
    boolean pieceFlag = false, packFlag = false, ctnFlag = false;

    boolean updateClicked = false;

    public OrderDetailRecyclerViewAdapter(Context context, List<DeliveryItemModel> deliveryItemList, RegisterdCustomerModel getMasterObject) {

        this.context = context;
        this.deliveryItemList = deliveryItemList;
        inflater = LayoutInflater.from(context);

        this.getMasterObject = getMasterObject;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.returned_order_detail_custom_layout, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        final DeliveryItemModel model = deliveryItemList.get(position);
        holder.productNameTv.setText(model.getName());
        holder.productPricesTv.setText("Prices:\n Pcs: " + model.getWSCtnPrice() + "\tPack: " + model.getRetailPackPrice() + "\tCtn: " + model.getRetailCtnPrice());
        if (model.getFocType().equals("Qty")) {
            holder.productFocTv.setText("Foc: " + model.getFoc_qty());
        } else if (model.getFocType().equals("Value")) {
            holder.productFocTv.setText("Foc: " + model.getFoc_value());
        } else {
            holder.productFocTv.setText("Foc: " + model.getFoc_percentage() + "%");
        }

        holder.productDiscountTv.setText("Discount: " + model.getItem_discount());
        holder.productNetAmount.setText("Net Amount: " + model.getNetTotalRetailPrice());
        holder.productQtyTv.setText("Qty:\n Pcs: " + model.getPcs_qty() + "\tPack: " + model.getPac_qty() + "\t   Ctn: " + model.getCtn_qty());

        holder.btnUpateProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateClicked = true;
                SelectQuantityDiloge(model);

            }
        });

    }

    @Override
    public int getItemCount() {
        return deliveryItemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView productNameTv, productPricesTv, productQtyTv, productFocTv, productDiscountTv, productNetAmount;
        Button btnUpateProduct;

        public MyViewHolder(View itemView) {
            super(itemView);

            productNameTv = itemView.findViewById(R.id.productNameTv);
            productPricesTv = itemView.findViewById(R.id.productPricesTv);
            productQtyTv = itemView.findViewById(R.id.productQtyTv);
            productFocTv = itemView.findViewById(R.id.productFocTv);
            productDiscountTv = itemView.findViewById(R.id.productDiscountTv);
            productNetAmount = itemView.findViewById(R.id.productNetAmount);
            btnUpateProduct = itemView.findViewById(R.id.btnUpateProduct);

        }
    }


    private void SelectQuantityDiloge(final DeliveryItemModel model) {

        NumberPicker np;
        final RadioButton percentageRB;
        final EditText etPiecePrice, etPackPrice, etCtnPrice;


        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.select_qty_layout, null);
        np = view.findViewById(R.id.number_picker);

        RadioGroup focrg = view.findViewById(R.id.RGSelectFoc);
        percentageRB = view.findViewById(R.id.precentageRB);
        focET = view.findViewById(R.id.focET);
        ctnQtyET = view.findViewById(R.id.ctnQtyET);
        packQtyET = view.findViewById(R.id.pakQtyET);
        picesQtyET = view.findViewById(R.id.piecesQtyET);
        etCtnPrice = view.findViewById(R.id.etCtnPrice);
        etPackPrice = view.findViewById(R.id.etPackPrice);
        etPiecePrice = view.findViewById(R.id.etPiecePrice);

        picesQtyET.requestFocus();
        etPiecePrice.setText(String.valueOf(model.getWSCtnPrice()));
        etPackPrice.setText(String.valueOf(model.getRetailPackPrice()));
        etCtnPrice.setText(String.valueOf(model.getRetailCtnPrice()));

        focET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (percentageRB.isChecked()) {
                    try {
                        int focvalue = Integer.parseInt(focET.getText().toString());
                        if (focvalue >= 100)
                            focET.setText("0");
                    } catch (Exception e) {
                        Utility.logCatMsg("Error in FocValidation" + e.getMessage());
                    }
                }
            }
        });

        
        focrg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.qtyRB) {
                    FocType = "Qty";
                    Utility.logCatMsg("Qty");
                } else if (checkedId == R.id.valueRB) {
                    FocType = "Value";
                    Utility.logCatMsg("Value");
                } else if (checkedId == R.id.precentageRB) {
                    FocType = "Percentage";
                    Utility.logCatMsg("percentage");
                    try {
                        int focvalue = Integer.parseInt(focET.getText().toString());
                        if (focvalue >= 100) {
                            focET.setText("0");
                        }
                    } catch (Exception e) {
                        Utility.logCatMsg("Error in FocValidation" + e.getMessage());
                    }
                }
            }
        });
        try {
            if (model.getFoc() > 0) {
                focET.setText(model.getFoc() + "");
            }
            if (model.getCtn_qty() > 0) {
                ctnQtyET.setText(model.getCtn_qty() + "");
            }
            if (model.getPac_qty() > 0) {
                packQtyET.setText(model.getPac_qty() + "");
            }
            if (model.getPcs_qty() > 0) {
                picesQtyET.setText(model.getPcs_qty() + "");
            }
        } catch (Exception e) {
            Utility.logCatMsg("Error in " + e.getMessage());
        }

        if (model.getFocType() != null) {
            if (model.getFocType().equals("Qty")) {
                RadioButton rb = view.findViewById(R.id.qtyRB);
                rb.setChecked(true);
            } else if (model.getFocType().equals("Value")) {
                RadioButton rb = view.findViewById(R.id.valueRB);
                rb.setChecked(true);
            } else if (model.getFocType().equals("Percentage")) {
                RadioButton rb = view.findViewById(R.id.precentageRB);
                rb.setChecked(true);
            }
        } else {
            model.setFocType("Percentage");
        }
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        View view1 = inflater.inflate(R.layout.dialog_custom_title, null);
        TextView tvCustomTitle = view1.findViewById(R.id.tvCustomTitle);
        tvCustomTitle.setText("Select Qty and FOC");

        alertDialogBuilder.setCustomTitle(view1);
        alertDialogBuilder
                .setCancelable(false)
                .setView(view)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        try {
                            float crtnPrice = Float.parseFloat(etCtnPrice.getText().toString());
                            float packPrice = Float.parseFloat(etPackPrice.getText().toString());
                            float picesPrice = Float.parseFloat(etPiecePrice.getText().toString());
                            if (crtnPrice != 0 && packPrice != 0) {
                                model.setRetailCtnPrice(crtnPrice);
                                model.setRetailPackPrice(packPrice);
                                model.setWSCtnPrice(picesPrice);
                                ///
                                setQtyValuesOfList(model, 0);
                                setFocValuesOfList(model, 0);
                                model.setSelectedValue("0");
                                model.setItem_discount(0);
                                model.setTotalRetailPrice(0);
                                model.setItemGstValue(0);
                                model.setNetTotalRetailPrice(0);
                                model.setIsSelected(false);
                                ///
                                dialog.dismiss();
                            }
                        } catch (Exception e) {
                        }

                        if (ctnQtyET.getText().length() == 0 && packQtyET.getText().length() == 0 && picesQtyET.getText().length() == 0) {
                            Utility.logCatMsg("Second Condition");
                            model.setIsSelected(false);
                            model.setSelectedValue("0");
                            model.setCtn_qty(0);
                            model.setPac_qty(0);
                            model.setPcs_qty(0);
                            model.setQtyType(QtyType);
                            model.setFocType(FocType);
                            setQtyValuesOfList(model, 0);
                        } else {
                            Utility.logCatMsg("third condition");
                            model.setIsSelected(true);
                            model.setSelectedValue(ctnQtyET.getText().toString());
                            model.setQtyType(QtyType);
                            model.setFocType(FocType);
                            setQtyValuesOfList(model, 1);
                        }

                        if (focET.getText().toString().equals("0") || focET.getText().length() == 0) {
                            model.setFoc(0);
                            setFocValuesOfList(model, 0);
                        } else {
                            setFocValuesOfList(model, 1);
                            model.setFoc(Integer.parseInt(focET.getText().toString()));
                        }
                        model.setNetTotalRetailPrice((model.getNetTotalRetailPrice() - model.getItem_discount()));
//                        netTotal += Integer.parseInt(getMasterObject.getNetTotal()) - (int)model.getNetTotalRetailPrice();
//                        getMasterObject.setNetTotal(String.valueOf(netTotal));
//                        getMasterObject.setTotalbill(String.valueOf(netTotal));
                        notifyDataSetChanged();
                        Utility.HideKeyBoard(view, context);
                        dialog.dismiss();

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();

                    }
                });
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        alertDialog.show();

        picesQtyET.addTextChangedListener(new TextWatcher() {
            int beforeValue = 0;
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(beforeValue == 0){
                    if(!picesQtyET.getText().toString().isEmpty())
                    beforeValue = Integer.parseInt(picesQtyET.getText().toString());
                }

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(i2 > 0){
                    if(beforeValue > Integer.parseInt(picesQtyET.getText().toString())){
                        Toast.makeText(context, "piece qty change", Toast.LENGTH_SHORT).show();
                        pieceFlag = true;
                    }
                    else if (beforeValue < Integer.parseInt(picesQtyET.getText().toString())){
                        Toast.makeText(context, "return qty can not be greater than original qty", Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                    }

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        packQtyET.addTextChangedListener(new TextWatcher() {
            int beforeValue = 0;
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(beforeValue == 0){
                    if(packQtyET.getText().toString().length() > 0){
                        beforeValue = Integer.parseInt(packQtyET.getText().toString());
                    }

                }

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(i2 > 0){
                    if(beforeValue > Integer.parseInt(packQtyET.getText().toString())){
                        Toast.makeText(context, "pack qty change", Toast.LENGTH_SHORT).show();
                        packFlag = true;
                    }
                    else if (beforeValue < Integer.parseInt(packQtyET.getText().toString())){
                        Toast.makeText(context, "return qty can not be greater than original qty", Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                    }

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        ctnQtyET.addTextChangedListener(new TextWatcher() {
            int beforeValue = 0;
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(beforeValue == 0){
                    if(ctnQtyET.getText().toString().length() > 0){
                        beforeValue = Integer.parseInt(ctnQtyET.getText().toString());
                    }

                }

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(i2 > 0){
                    if(beforeValue > Integer.parseInt(ctnQtyET.getText().toString())){
                        Toast.makeText(context, "ctn qty change", Toast.LENGTH_SHORT).show();
                        ctnFlag = true;
                    }
                    else if (beforeValue < Integer.parseInt(ctnQtyET.getText().toString())){
                        Toast.makeText(context, "return qty can not be greater than original qty", Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                    }

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void setQtyValuesOfList(DeliveryItemModel model, int flage) {
        model.setCtn_qty(0);
        model.setPac_qty(0);
        model.setPcs_qty(0);
        model.setTotalwholeSalePrice(0);
        model.setTotalCostPrice(0);
        model.setTotalRetailPrice(0);
        model.setDelivered_Quantity(model.getDeliver_pcs_qty());
        model.setItemTotalDeliverQtyInPieces(0);
        getMasterObject.setPercentage_discount("0");

        if (flage == 1) {
            int ctnQty = 0, packQty = 0, pieceQty = 0;
            if (!ctnQtyET.getText().toString().equals(""))
                ctnQty = Integer.parseInt(ctnQtyET.getText().toString());
            if (!packQtyET.getText().toString().equals(""))
                packQty = Integer.parseInt(packQtyET.getText().toString());
            if (!picesQtyET.getText().toString().equals(""))
                pieceQty = Integer.parseInt(picesQtyET.getText().toString());
            int ctnSize = model.getCtnSize();
            int packSize = model.getPackSize();

            model.setCtn_qty(ctnQty * -1);
            model.setPac_qty(packQty * -1);
            model.setPcs_qty(pieceQty * -1);

            model.setReject_pcs_qty(pieceQty * -1);
            model.setReject_pac_qty(packQty * -1);
            model.setReject_ctn_qty(ctnQty * -1);

            int totalQtyInPieces = ((ctnQty * ctnSize * packSize) + (packQty * packSize) + pieceQty) * -1;
            Utility.logCatMsg("Total Quantity in Pieces " + totalQtyInPieces);
            getMasterObject.setTotalQty(totalQtyInPieces);
            model.setReturn_Quantity(totalQtyInPieces);
            model.setItemTotalDeliverQtyInPieces(totalQtyInPieces);
            model.setItemTotalActualQtyInPieces(totalQtyInPieces);
            model.setDelivered_Quantity(totalQtyInPieces);
            Log.d("deliveredQty",String.valueOf(model.getDelivered_Quantity()));
            int pieceTotal = 0, packTotal = 0, ctnTotal = 0;
            if(pieceFlag){
                pieceTotal = Integer.parseInt(picesQtyET.getText().toString()) * (int)model.getWSCtnPrice();
            }
            if(packFlag){
                packTotal = Integer.parseInt(packQtyET.getText().toString()) * (int)model.getRetailPackPrice();
            }
            if(ctnFlag){
                ctnTotal = Integer.parseInt(ctnQtyET.getText().toString()) * (int)model.getRetailCtnPrice();
            }
            if(pieceTotal == 0 && packTotal == 0 && ctnTotal == 0){
                getMasterObject.setNetTotal(String.valueOf(Integer.parseInt(getMasterObject.getNetTotal()) * -1));
                getMasterObject.setTotalbill(String.valueOf(Integer.parseInt(getMasterObject.getTotalbill()) * -1));
                model.setDelivered_Quantity(totalQtyInPieces);
            }
            else{
                netTotal += (pieceTotal + packTotal + ctnTotal) * -1;
                getMasterObject.setNetTotal(String.valueOf(netTotal));
                getMasterObject.setTotalbill(String.valueOf(netTotal));
                Log.d("returnOrderTotal","TotalBill: "+getMasterObject.getNetTotal()+"\t qty: "+model.getPcs_qty()+" \t"+model.getPac_qty()+" \t"+model.getCtn_qty());
            }

            //model.setDelivered_Quantity(packQty + packQty + pieceQty);
            float ctnWSPrice = model.getRetailPiecePrice();
            float totalCtnWSPrice = ctnQty * ctnWSPrice;
            float ctnCostPrice = model.getCostCtnPrice();
            float totalctnCostPrice = ctnQty * ctnCostPrice;
            float ctnRetailPrice = model.getRetailCtnPrice();
            float totalctnRetailPrice = ctnQty * ctnRetailPrice;
            float packWSPrice = model.getWSPackPrice();
            float totalPackWSPrice = packQty * packWSPrice;
            float packCostPrice = model.getCostPackPrice();
            float totalPackCostPrice = packQty * packCostPrice;
            float packRetailPrice = model.getRetailPackPrice();
            float totalPackRetailPrice = packQty * packRetailPrice;
            float PiecesWSPrice = model.getWSPiecePrice();
            float totalPiecesWSPrice = pieceQty * PiecesWSPrice;
            float PiecesCostPrice = model.getCostPiecePrice();
            float totalPiecesCostPrice = pieceQty * PiecesCostPrice;
            float PiecesRetailPrice = model.getWSCtnPrice();
            float totalPiecesRetailPrice = pieceQty * PiecesRetailPrice;
            float sumOfWholeSalePrice = ctnWSPrice + packWSPrice + PiecesWSPrice;
            float sumOfTotalWholeSalePrice = totalCtnWSPrice + totalPackWSPrice + totalPiecesWSPrice;
            float sumOfCostPrice = ctnCostPrice + packCostPrice + PiecesCostPrice;
            float sumOfTotalCostPrice = totalctnCostPrice + totalPackCostPrice + totalPiecesCostPrice;
            float sumOfRetailPrice = ctnRetailPrice + packRetailPrice + PiecesRetailPrice;
            float sumOfTotalRetailPrice = totalctnRetailPrice + totalPackRetailPrice + totalPiecesRetailPrice;
            model.setTotalwholeSalePrice(sumOfTotalWholeSalePrice);  // this is handel on insertion time
            model.setTotalCostPrice(sumOfTotalCostPrice);
            model.setTotalRetailPrice(sumOfTotalRetailPrice);
            model.setDisplayPrice(0);
            model.setItemGstPer(Utility.GST_PERCENT);
            float gst = Utility.GST_PERCENT;
            float gstPercent = 0;
            if (gst != 0) {
                gstPercent = gst / 100.0f;
            }
            float gstValue = gstPercent * sumOfTotalRetailPrice;
            model.setItemGstValue(gstValue);
            model.setNetTotalRetailPrice((model.getTotalRetailPrice() + model.getItemGstValue()));
            Utility.logCatMsg("Gst Value " + model.getItemGstValue());
            Utility.logCatMsg("Net Total Bill  " + model.getNetTotalRetailPrice());
        }

    }

    private void setFocValuesOfList(DeliveryItemModel model, int flage) {
        model.setFoc_percentage(0);
        model.setFoc_qty(0);
        model.setFoc_value(0);
        model.setItem_discount(0);
        if (flage == 1) {
            if (model.getFocType() != null) {
                if (model.getFocType().toString().equals("Qty")) {
                    model.setFoc_qty(Integer.parseInt(focET.getText().toString()));
                } else if (model.getFocType().toString().equals("Value")) {
                    int focValue = Integer.parseInt(focET.getText().toString());
                    model.setFoc_value(focValue);
                    model.setItem_discount(focValue);
                } else if (model.getFocType().toString().equals("Percentage")) {
                    Float focValue = Float.parseFloat(focET.getText().toString());
                    Float discountInValue = (focValue / 100.0f) * model.getTotalRetailPrice();
                    Utility.logCatMsg(focValue + "FOC Percent % of " + model.getTotalRetailPrice() + "in value Is = " + discountInValue);
                    model.setFoc_value(Math.round(discountInValue));
                    model.setItem_discount(Math.round(discountInValue));
                    model.setFoc_percentage(Integer.parseInt(focET.getText().toString()));
                }
            }
        }
    }

    public void insertRecord(ArrayList<DeliveryItemModel> itemModelList, RegisterdCustomerModel getMasterObject){

        ZEDTrackDB db = new ZEDTrackDB(context);
        if(!updateClicked){

            for(int i = 0; i < itemModelList.size(); i++){
                itemModelList.get(i).setDelivered_Quantity(itemModelList.get(i).getTotal_Quantity() * -1);
            }
            getMasterObject.setTotalQty(getMasterObject.getTotalQty() * -1);
            getMasterObject.setNetTotal(String.valueOf(Integer.parseInt(getMasterObject.getNetTotal())* -1));
            getMasterObject.setPercentage_discount("0");
        }
        getMasterObject.setPercentage_discount("0");
        getMasterObject.setDate(Utility.getCurrentDate() + " " + Utility.getTime());
        getMasterObject.setTotalbill(String.valueOf(Integer.parseInt(getMasterObject.getTotalbill()) * -1));

        Log.d("updatedDate", getMasterObject.getDate());
        db.insertRunTimeOrderDetails(itemModelList, getMasterObject,"false");
    }

}
