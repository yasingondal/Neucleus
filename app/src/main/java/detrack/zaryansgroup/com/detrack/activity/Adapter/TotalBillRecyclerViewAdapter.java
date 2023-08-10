package detrack.zaryansgroup.com.detrack.activity.Adapter;

import android.app.Dialog;
import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import detrack.zaryansgroup.com.detrack.activity.Model.CompanyItemsModel.DeliveryItemModel;
import detrack.zaryansgroup.com.detrack.R;
import detrack.zaryansgroup.com.detrack.activity.SQLlite.ZEDTrackDB;
import detrack.zaryansgroup.com.detrack.activity.SharedPreferences.SharedPrefs;
import detrack.zaryansgroup.com.detrack.activity.activites.SecondView.SelectProductActivitySecond;
import timber.log.Timber;

public class TotalBillRecyclerViewAdapter extends RecyclerView.Adapter<TotalBillRecyclerViewAdapter.MyViewHolder> {

    ArrayList<DeliveryItemModel> selectedItemList;
    Context context;
    LayoutInflater inflater;
    SharedPrefs sharedPrefs;
    ZEDTrackDB db;


    public TotalBillRecyclerViewAdapter(Context context, ArrayList<DeliveryItemModel> selectedItemList) {
        this.context = context;
        this.selectedItemList = selectedItemList;
        inflater = LayoutInflater.from(context);
        sharedPrefs = new SharedPrefs(context);
        db = new ZEDTrackDB(context);
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.total_bill_custom_layout, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        DeliveryItemModel model = selectedItemList.get(position);

        holder.tvItemDescription.setText(model.getName().toLowerCase());

        holder.tvItemDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Dialog dialog = new Dialog(context);
                Window window = dialog.getWindow();
                window.setGravity(Gravity.CENTER);
                //For Removing Background of Popup Dialogue...
                dialog.getWindow().getDecorView().setBackgroundColor(Color.TRANSPARENT);
                dialog.setContentView(R.layout.home_settings_dilaogue);

                TextView pfullname = dialog.findViewById(R.id.pFullName);

                if(model.getItemDetail().length()>0){
                    Timber.d("Item detail not null case is running => "+model.getItemDetail());
                    pfullname.setText(model.getItemDetail());
                }else{
                    Timber.d("Item detail null case is running ");
                    String itemDetail = db.getItemDetail(model.getItem_id());
                    pfullname.setText(itemDetail);
                }

                dialog.show();

            }
        });


        float rSpecialPrice = db.checkIfSpecialPriceExists(SelectProductActivitySecond.rCustomerId,model.getItem_id());

        if(model.getBonusQty()>0)
        {
            holder.tvBonusQty.setText(model.getBonusQty()+"");
            holder.tvBonusGst.setText(model.getBonusItemsGst()+"");
        }else{
            holder.tvBonusQty.setText(0+"");
            holder.tvBonusGst.setText(0+"");
        }


        if(sharedPrefs.getView().equalsIgnoreCase("secondView")){

            if(rSpecialPrice==0.0f)
            {

                if(new SharedPrefs(context).getDesignation().equalsIgnoreCase("Order Booker")){
                    holder.tvPeritem.setText("--");
                }else {
                    holder.tvPeritem.setText(String.valueOf(Math.abs(model.getRetailPiecePrice())));

                }


            }else{

                if(new SharedPrefs(context).getDesignation().equalsIgnoreCase("Order Booker")){
                    holder.tvPeritem.setText("--");
                }else {
                    holder.tvPeritem.setText(String.valueOf(Math.abs(model.getRetailPiecePrice())));
                }

            }


            //todo for IncentiveItem GST Calculations

            if(model.getTaxCode().equalsIgnoreCase("3rd")){

                float tam;
                if(rSpecialPrice==0.0f)
                {
                    tam = model.getRetailPiecePrice() * model.getCtn_qty();

                    if(new SharedPrefs(context).getDesignation().equalsIgnoreCase("Order Booker")){
                        holder.tvGrossAmount.setText("--");
                        holder.tvItemnettotal.setText("--");
                        holder.tvItemDiscountedValue.setText("--");
                    }else {
                        holder.tvGrossAmount.setText(String.valueOf(Math.abs(tam)));
                        holder.tvItemnettotal.setText(String.valueOf(tam-model.getDiscountPolicyValue()));
                        holder.tvItemDiscountedValue.setText(String.valueOf(model.getDiscountPolicyValue()));
                    }


                }else{

                    tam = 0;

                    tam = rSpecialPrice * model.getCtn_qty();

                    if(new SharedPrefs(context).getDesignation().equalsIgnoreCase("Order Booker")){
                        holder.tvGrossAmount.setText("--");
                        holder.tvItemnettotal.setText("--");

                    }else {
                        holder.tvGrossAmount.setText(String.valueOf(Math.abs(tam)));
                        holder.tvItemnettotal.setText(String.valueOf(Math.abs(tam)));
                    }


                }

            }else if(model.getTaxCode().equalsIgnoreCase("SR")){


                float tam;

                if(rSpecialPrice==0.0f)
                {
                    tam = 0;
                    tam = model.getRetailPiecePrice() * model.getCtn_qty();

                    if(new SharedPrefs(context).getDesignation().equalsIgnoreCase("Order Booker")){
                        holder.tvGrossAmount.setText("--");
                        holder.tvItemnettotal.setText("--");
                        holder.tvItemDiscountedValue.setText("--");

                    }else {
                        holder.tvGrossAmount.setText(String.valueOf(Math.abs(tam)));
                        holder.tvItemnettotal.setText(String.valueOf(Math.abs(tam-model.getDiscountPolicyValue())));
                        holder.tvItemDiscountedValue.setText(String.valueOf(Math.abs(model.getDiscountPolicyValue())));
                    }


                }else{

                    tam = rSpecialPrice * model.getCtn_qty();

                    if(new SharedPrefs(context).getDesignation().equalsIgnoreCase("Order Booker")){
                        holder.tvGrossAmount.setText("--");
                        holder.tvItemnettotal.setText("--");


                    }else {

                        holder.tvGrossAmount.setText(String.valueOf(Math.abs(tam)));
                        holder.tvItemnettotal.setText(String.valueOf(Math.abs(tam)));
                    }


                }


            } else {

                float tam;
                if(rSpecialPrice==0.0f)
                {
                    tam= 0;
                    tam = model.getRetailPiecePrice() * model.getCtn_qty();

                    if(new SharedPrefs(context).getDesignation().equalsIgnoreCase("Order Booker")){
                        holder.tvGrossAmount.setText("--");
                        holder.tvItemnettotal.setText("--");
                        holder.tvItemDiscountedValue.setText("--");


                    }else {

                        holder.tvGrossAmount.setText(String.valueOf(Math.abs(tam)));
                        holder.tvItemnettotal.setText(String.valueOf(Math.abs(tam-model.getDiscountPolicyValue())));
                        holder.tvItemDiscountedValue.setText(String.valueOf(Math.abs(model.getDiscountPolicyValue())));
                    }



                }else{


                    tam = rSpecialPrice * model.getCtn_qty();

                    if(new SharedPrefs(context).getDesignation().equalsIgnoreCase("Order Booker")){
                        holder.tvGrossAmount.setText("--");
                        holder.tvItemnettotal.setText("--");

                    }else {

                        holder.tvGrossAmount.setText(String.valueOf(Math.abs(tam)));
                        holder.tvItemnettotal.setText(String.valueOf(Math.abs(tam)));
                    }



                }
            }

        }
        else {

            if(new SharedPrefs(context).getDesignation().equalsIgnoreCase("Order Booker")){
                holder.tvPeritem.setText("--");
                holder.tvGrossAmount.setText("--");
                holder.tvItemnettotal.setText("--");

            }else {

                holder.tvPeritem.setText(String.valueOf(Math.abs(model.getRetailCtnPrice())));
                holder.tvGrossAmount.setText(String.valueOf(Math.abs(model.getNetTotalRetailPrice())));
                holder.tvItemnettotal.setText(String.valueOf(Math.abs(model.getNetTotalRetailPrice()-model.getDiscountPolicyValue())));
            }



        }



        holder.tvItemPcs.setText(String.valueOf(Math.abs(model.getPcs_qty())));

        // Controlling visibility according to company terms
        if(sharedPrefs.getIsDiscountVisible().equalsIgnoreCase("false") &&
                sharedPrefs.getIsDiscountVisible().equalsIgnoreCase("false"))
        {

//            holder.tvItemGst.setVisibility(View.GONE);
            holder.tvItemdiscount.setVisibility(View.GONE);

        }

        if(new SharedPrefs(context).getDesignation().equalsIgnoreCase("Order Booker")){
            holder.tvItemGst.setText("--");
            holder.tvItemdiscount.setText("--");
            holder.tvItemDiscountedValue.setText("--");

        }else {

            holder.tvItemGst.setText(String.valueOf(Math.abs(model.getItemGstValue())));
            holder.tvItemdiscount.setText(String.valueOf(Math.abs(model.getItem_discount())));
            holder.tvItemDiscountedValue.setText(String.valueOf(model.getDiscountPolicyValue()));
        }




        if(model.getFocType()!= null) {
            if (model.getFocType().equals("Qty")) {

                float tempFOCtotal = model.getPcs_qty() + model.getFoc_qty();
                holder.tvItemPcs.setText(String.valueOf(Math.abs(tempFOCtotal)));

            }
        }



        holder.tvItemPck.setText(String.valueOf(Math.abs(model.getPac_qty())));
        holder.tvItemCtn.setText(String.valueOf(Math.abs(model.getCtn_qty())));

        if(sharedPrefs.getView().equals("secondView")){
            holder.tvItemPck.setVisibility(View.GONE);
            holder.tvItemPcs.setVisibility(View.GONE);
        }

        Timber.d("Value beofre try catch of nettotal =>"+model.getNetTotalRetailPrice());

        try
        {
            float cleanNetTotalValue = Float.parseFloat(holder.tvItemnettotal.getText().toString());
            model.setNetTotalRetailPrice(cleanNetTotalValue);
            Timber.d("Value in try catch of nettotal =>"+model.getNetTotalRetailPrice());
        }
        catch(Exception e)
        {
            Timber.d("Exception => "+e.getMessage());
        }

    }



    @Override
    public int getItemCount() {
        return selectedItemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvItemDescription, tvItemPcs, tvItemPck, tvGrossAmount,
                tvItemCtn,tvPeritem,tvItemGst,tvItemnettotal,tvItemdiscount,tvItemDiscountedValue,tvBonusQty,tvBonusGst;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvItemDiscountedValue = itemView.findViewById(R.id.tvItemDiscountedValue);
            tvItemDescription = itemView.findViewById(R.id.tvItemDescription);
            tvItemPcs = itemView.findViewById(R.id.tvItemPcs);
            tvItemPck = itemView.findViewById(R.id.tvItemPck);
            tvGrossAmount = itemView.findViewById(R.id.tvItemValue);
            tvItemCtn = itemView.findViewById(R.id.tvItemCtn);
            tvPeritem = itemView.findViewById(R.id.tvPeritem);
            tvItemGst = itemView.findViewById(R.id.tvItemGst);
            tvBonusGst = itemView.findViewById(R.id.tvBonusGst);
            tvItemnettotal = itemView.findViewById(R.id.tvItemnettotal);
            tvItemdiscount = itemView.findViewById(R.id.tvItemdiscount);
            tvBonusQty = itemView.findViewById(R.id.tvBonusQty);
        }
    }
}