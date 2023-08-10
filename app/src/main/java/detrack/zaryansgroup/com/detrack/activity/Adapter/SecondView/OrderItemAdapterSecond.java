package detrack.zaryansgroup.com.detrack.activity.Adapter.SecondView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import detrack.zaryansgroup.com.detrack.R;
import detrack.zaryansgroup.com.detrack.activity.Model.CompanyItemsModel.DeliveryItemModel;
import detrack.zaryansgroup.com.detrack.activity.SQLlite.ZEDTrackDB;
import detrack.zaryansgroup.com.detrack.activity.SharedPreferences.SharedPrefs;
import detrack.zaryansgroup.com.detrack.activity.activites.BonusPolicyWork.BonusPolicyModel;
import detrack.zaryansgroup.com.detrack.activity.activites.DiscountPolicyWork.DiscountPolicyModel;
import detrack.zaryansgroup.com.detrack.activity.activites.SecondView.SelectProductActivitySecond;
import detrack.zaryansgroup.com.detrack.activity.activites.SelectCustomerActivity;
import detrack.zaryansgroup.com.detrack.activity.utilites.Utility;
import timber.log.Timber;


public class OrderItemAdapterSecond extends RecyclerView.Adapter<OrderItemAdapterSecond.MyViewHolder> {

    float disValue = 0.0f;
    ArrayList<DiscountPolicyModel> discountList;

    int   bonusQty = 0;
    ArrayList<BonusPolicyModel> bonusList;

    DeliveryItemModel model;
    Context context;
    ArrayList<DeliveryItemModel> orderList;

    String currency;
    ZEDTrackDB db;

    float totalAmount = 0;
    float pckqty;
    float pcsqty;
    float ctnqty;
    float pcsTotal;
    float pckTotal;
    float ctnTotal;


    private OnItemClick onItemClick;
    private OnItemLongClick onItemLongClick;

    public interface OnItemClick {
        void onClick(int position, DeliveryItemModel deliveryItemModel);
    }

    public interface OnItemLongClick {
        void onLongClick(int position, View view);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvProductName,
                tvCtvQty,
                tvCtnDisc,
                tvCtnPrice, tvTotalAmount,tvProductNameBonus,tvCtnQtyBonus;

        LinearLayout bonusPolicyLayout;
        View bonusSeparator;



        public CardView maincard;
        public MyViewHolder(View view) {
            super(view);

            tvProductName = view.findViewById(R.id.tvProductName);
            tvCtvQty = view.findViewById(R.id.tvCtnQty);
            tvCtnPrice = view.findViewById(R.id.tvCtnPrice);
            tvTotalAmount = view.findViewById(R.id.tvTotalAmount);
            maincard = view.findViewById(R.id.maincard);
            tvCtnDisc = view.findViewById(R.id.tvCtnDisc);
            tvProductNameBonus = view.findViewById(R.id.tvProductNameBonus);
            tvCtnQtyBonus = view.findViewById(R.id.tvCtnQtyBonus);
            bonusPolicyLayout = view.findViewById(R.id.ll_bonus_policy);
            bonusSeparator = view.findViewById(R.id.view_bonus_separator);

        }
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public void setOnItemLongClick(OnItemLongClick onItemLongClick) {
        this.onItemLongClick = onItemLongClick;
    }

    public OrderItemAdapterSecond(Context context, ArrayList<DeliveryItemModel> orderlist) {

        this.context = context;
        this.orderList = orderlist;
        currency = new SharedPrefs(context).getCurrency();
        db = new ZEDTrackDB(context);
        discountList = new ArrayList<>();
        bonusList = new ArrayList<>();

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_custom_layout_second, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {


        model = orderList.get(position);

        if(model.getItemDetail().length()>0){
            Timber.d("Item detail not null case is running => "+model.getItemDetail());
            holder.tvProductName.setText(model.getItemDetail());
        }else{
            Timber.d("Item detail null case is running ");
            String itemDetail = db.getItemDetail(model.getItem_id());
            holder.tvProductName.setText(itemDetail);
        }


        holder.tvCtvQty.setText(String.valueOf(Math.abs(model.getCtn_qty())));

        holder.maincard.setOnClickListener(v -> {
            if (onItemClick != null)
                onItemClick.onClick(position, model);
        });

        holder.maincard.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onItemLongClick != null)
                    onItemLongClick.onLongClick(position, v);
                return true;
            }
        });




         pckqty = Float.parseFloat(String.valueOf(model.getPac_qty()));
         pcsqty = Float.parseFloat(String.valueOf(model.getPcs_qty()));
         ctnqty = Float.parseFloat(String.valueOf(model.getCtn_qty()));
         pcsTotal = pcsqty * (float) model.getRetailCtnPrice();
         pckTotal = pckqty * model.getRetailPackPrice();
         ctnTotal = 0;

//        Work is Continued on prices...... I have changed Two Few Things You will get in History

//        if(model.getTaxCode().equalsIgnoreCase("3rd") || model.getTaxCode().equalsIgnoreCase("SR")){

        float rSpecialPrice = db.checkIfSpecialPriceExists(SelectProductActivitySecond.rCustomerId, model.getItem_id());

        if (rSpecialPrice == 0.0f) {

            ctnTotal = ctnqty * model.getRetailPiecePrice();

            if(new SharedPrefs(context).getDesignation().equalsIgnoreCase("Order Booker")){
                holder.tvCtnPrice.setText("--");
            }
            else{
                holder.tvCtnPrice.setText(model.getRetailPiecePrice() + "");
            }


        } else {

            ctnTotal = ctnqty * rSpecialPrice;

            if(new SharedPrefs(context).getDesignation().equalsIgnoreCase("Order Booker")){
                holder.tvCtnPrice.setText("--");
            }
            else{
                holder.tvCtnPrice.setText(rSpecialPrice + "");
            }

        }


//        }else {
//            holder.tvCtnPrice.setText(String.valueOf((int) model.getRetailCtnPrice()));
//        }


        //This Block is running inside Add Sales Return Order...
        if (SelectCustomerActivity.addOrder.equals("false")) {


            totalAmount = (pckTotal + pcsTotal + ctnTotal) + Float.parseFloat(String.valueOf(model.getFoc_value()));

        } else {

            totalAmount = (pckTotal + pcsTotal + ctnTotal) - Float.parseFloat(String.valueOf(model.getFoc_value()));



            //code for Bonus Policy
            //todo main parent
            try {
                bonusList.clear();
                bonusQty = 0;
                bonusList = db.getBonusPolicyData(Utility.getCurrentDate()+"T00:00:00",SelectProductActivitySecond.rCustomerId,"Customer");

                if(!(bonusList.size() > 0))
                {
                    Timber.d("BonusList  for Customer Query is running");

                    if(!(bonusList.size()>0)){

                        Timber.d("BonusList  == 0 (for Customer) so 2nd CustomerType Query is running");

                        bonusList.clear();
                        bonusQty = 0;
                        bonusList = db.getBonusPolicyData(Utility.getCurrentDate()+"T00:00:00",SelectProductActivitySecond.rCustomerTypeId,"CustomerType");


                    if(!(bonusList.size()>0)){

                        Timber.d("BonusList  == 0 (for CustomerType) so 3rd Item Query is running");
                        //todo req for Next 2nd Priority call
                        bonusList.clear();
                        bonusQty = 0;
                        bonusList = db.getBonusPolicyData(Utility.getCurrentDate()+"T00:00:00",model.getItem_id(),"Item");

                        if(!(bonusList.size()>0))
                        {
                            Timber.d("BonusList  == 0 (for Item) so 4rd Division Query is running");
                            bonusList.clear();
                            bonusQty = 0;
                            bonusList = db.getBonusPolicyData(Utility.getCurrentDate()+"T00:00:00",model.getDivisionId(),"Division");


                            if(!(bonusList.size() > 0))
                            {
                                Timber.d("BonusList  == 0 (for Division) so 5th Brand Query is running");
                                bonusList.clear();
                                bonusQty = 0;
                                bonusList = db.getBonusPolicyData(Utility.getCurrentDate()+"T00:00:00",model.getBrandId(),"Brand");

                                if(!(bonusList.size() > 0))
                                {
                                    Timber.d("There is not any type of Bonus available ");

                                }else{
                                    //todo actual code of (Brand Bonus Policy)
                                    Timber.d("Brand Bonus Policy need to apply condition meet");
                                    applyBonusPolicy((int) ctnqty,bonusList,"Brand");

                                }

                            }else{

                                //todo actual code of DivisionType
                                Timber.d("DivisionType Bonus Policy need to apply condition meet");
                                applyBonusPolicy((int) ctnqty,bonusList,"Division");

                            }

                        }else{
                            //todo actual code of (itemType polciy)
                            Timber.d("itemType Bonus Policy need to apply condition meet");
                            Timber.d("BonusList size inside itemType is "+bonusList.size());
                            applyBonusPolicy((int) ctnqty,bonusList,"Item");
                        }

                    }
                    else{
                        //todo actual code of (CustomerType)
                        Timber.d("CustomerType Policy need to apply condtion meet");
                        Timber.d("BonusList size inside customerType is "+bonusList.size());
                        applyBonusPolicy((int) ctnqty,bonusList,"CustomerType");
                    }

                    }
                }
                else{
                    //todo actual code of (Customer)
                    Timber.d("Customer Bonus Policy need to apply condtion meet");
                    Timber.d("BonusList size inside customer is "+bonusList.size());
                    applyBonusPolicy((int) ctnqty,bonusList,"Customer");
                }


            }catch (Exception e)
            {
                Timber.d("Exception in bonus is "+e.getMessage());
            }





            //code of Discount Policy Parent Priority
            try {

                discountList.clear();
                disValue = 0.0f;
                discountList = db.getDiscountPolicyData(Utility.getCurrentDate()+"T00:00:00",SelectProductActivitySecond.rCustomerId,"Customer");

                if(!(discountList.size() > 0))
                {
                    Timber.d("DiscountList  == 0 (for Customer) so 2ndP CustomerType condition is running");
                    //todo req for 2nd Priority (its done )
                    discountList.clear();
                    disValue = 0.0f;
                    discountList = db.getDiscountPolicyData(Utility.getCurrentDate()+"T00:00:00",SelectProductActivitySecond.rCustomerTypeId,"CustomerType");

                    if(!(discountList.size()>0)){
                        Timber.d("DiscountList  == 0 (for CustomerType) so 3rdP Item Query is running");
                        //todo req for Next 3rd Priority call (its done )
                        discountList.clear();
                        disValue = 0.0f;
                        discountList = db.getDiscountPolicyData(Utility.getCurrentDate()+"T00:00:00",model.getItem_id(),"Item");

                        if(!(discountList.size()>0))
                        {

                            //todo req for Next 4rth Priority call (Item discount policy).. its done
                            discountList.clear();
                            disValue = 0.0f;
                            discountList = db.getDiscountPolicyData(Utility.getCurrentDate()+"T00:00:00",model.getDivisionId(),"Division");

                            Timber.d("Div id for tuc lu is "+model.getDivisionId());

                            if(!(discountList.size() > 0))
                            {
                                //todo req for Next 5th Priority call (Brand discount policy) its done
                                discountList.clear();
                                disValue = 0.0f;
                                discountList = db.getDiscountPolicyData(Utility.getCurrentDate()+"T00:00:00",model.getBrandId(),"Brand");

                                if(!(discountList.size() > 0))
                                {
                                    Timber.d("There is not any type of discount avaialable on this item");
                                }else{
                                    //todo actual code of (Brand Discount Policy.) its done
                                    Timber.d("Brand Discount Policy need to apply condtion meet");
                                    applyDiscountPolicyShort(totalAmount, (int) ctnqty,discountList,"brand");

                                }

                            }else{

                                //todo actual code of DivisionType
                                Timber.d("DivisionType Policy need to apply condtion meet");
                                applyDiscountPolicyShort(totalAmount, (int) ctnqty,discountList,"division");

                            }

                        }else{
                            //todo actual code of (itemType polciy)
                            Timber.d("itemType Policy need to apply condtion meet");
                            Timber.d("DiscountList size inside itemType is "+discountList.size());
                            applyDiscountPolicyShort(totalAmount, (int) ctnqty,discountList,"item");
                        }

                    }
                    else{
                        //todo actual code of (CustomerType)
                        Timber.d("CustomerType Policy need to apply condtion meet");
                        Timber.d("DiscountList size inside customerType is "+discountList.size());
                        applyDiscountPolicy(totalAmount, (int) ctnqty,discountList);
                    }



                }else{
                    //todo actual code for discount policy on customer
                    applyDiscountPolicy(totalAmount, (int) ctnqty,discountList);
                }


            }catch (Exception e){

                Timber.d("Exception In Discount Policy is "+e.getMessage());

            }




        }


        float finalPrice = totalAmount  - disValue;

        model.setDiscountPolicyValue(disValue);
        model.setNetTotalRetailPrice(finalPrice);
        holder.tvCtnDisc.setText(disValue+"");

        if(new SharedPrefs(context).getDesignation().equalsIgnoreCase("Order Booker")){
            holder.tvTotalAmount.setText("--");
        }else{
            holder.tvTotalAmount.setText(finalPrice+"");
        }


        if(!(model.getBonusPolicyId()>0))
        {
            holder.bonusPolicyLayout.setVisibility(View.GONE);
            holder.bonusSeparator.setVisibility(View.GONE);
        }

        holder.tvProductNameBonus.setText(model.getItemDetail()+" (Bonus)");
        holder.tvCtnQtyBonus.setText(model.getBonusQty()+"");

    }



    //Discount policies on customer/customerType
    public  void applyDiscountPolicy(float totalAmount, int ctnqty, ArrayList<DiscountPolicyModel> discountList){
        Timber.d("applyDiscountPolicy function is calling  ");

        //Todo Discount Policy work
        try {

            //Todo discount policy for customer
            if(discountList.size()>0) {
                {

                    int startPos = 0;
                    Timber.d("Discount list size is > 0 for customer");


                        Timber.d("---------------------------------------------------");
                        Timber.d("ItemId is "+ discountList.get(startPos).getItemId());
                        Timber.d("DivId is "+ discountList.get(startPos).getDivId());
                        Timber.d("GroupDivId"+ discountList.get(startPos).getGroupDivId());
                        Timber.d("---------------------------------------------------");


                        // Step2: Check ItemId>0
                        Timber.d(" item id is "+ discountList.get(startPos).getItemId());
                        if (discountList.get(startPos).getItemId() > 0) //Second If
                        {

                            Timber.d("itemId is > 0 case ");
                            //Step3: Check Type of Discount Policy Applied ( Value / Qty )
                            if (discountList.get(startPos).getDiscountType().equalsIgnoreCase("Value")) //Third if Start
                            {

                                Timber.d("Value type is running inside itemid");

                                Timber.d("Discountlist size is "+discountList.size());
                                Timber.d("Target value is "+discountList.get(startPos).getTargetValue());
                                Timber.d("Total amount is "+totalAmount);

                                // Step4 Check Target amount Meet or not
                                if (totalAmount == discountList.get(startPos).getTargetValue() || totalAmount > discountList.get(startPos).getTargetValue()) {

                                    model.setDiscountPolicyId(discountList.get(startPos).getDiscountPolicyId());
                                    //Step:5: Check which type of Discount Applied (Fix value/Percentage)
                                    if (discountList.get(startPos).getDiscountPercentage() > 0) {

                                        model.setDiscPercentage(discountList.get(startPos).getDiscountPercentage());

                                        //Todo for Percentage Type Calculation and Minus from Total Amount(done)
                                        disValue = (totalAmount* discountList.get(startPos).getDiscountPercentage())/100;
                                        totalAmount = totalAmount -disValue;


                                    } else {

                                        //Todo Simply Minus discountList.get(position).getdiscountValue from total amount...(done)
                                        Timber.d("Before calculation the amount is "+totalAmount);
                                        totalAmount = totalAmount - discountList.get(startPos).getDiscountValue();
                                        Timber.d("here inside the Total amount is "+totalAmount);

                                    }


                                } else {
//                                    Toast.makeText(context, "Target Value not Matched", Toast.LENGTH_SHORT).show();
                                }
                            } else if (discountList.get(startPos).getDiscountType().equalsIgnoreCase("Qty")) // Else Check for Qty type

                            {
                                Timber.d("Qty type is running inside itemid");
                                if (ctnqty == discountList.get(startPos).getTargetQty() || ctnqty > discountList.get(startPos).getTargetQty()) {
                                    model.setDiscountPolicyId(discountList.get(startPos).getDiscountPolicyId());
                                    if (discountList.get(startPos).getDiscountPercentage() > 0) {
                                        Timber.d("Percent type is running iside qty inside itemid");
                                        Timber.d("Value before is "+totalAmount);

                                        model.setDiscPercentage(discountList.get(startPos).getDiscountPercentage());
                                        disValue = (totalAmount* discountList.get(startPos).getDiscountPercentage())/100;
                                        totalAmount = totalAmount - disValue;

                                        Timber.d("Value after is "+totalAmount);


                                    } else if (discountList.get(startPos).getDiscountValue() > 0) {
                                        //Todo Simply Minus discountList.get(position).getdiscountValue from total amount...
                                        disValue = discountList.get(startPos).getDiscountValue();
                                        totalAmount = totalAmount - disValue;

                                    } else {
                                        Timber.d("Nothing from Value/Qty Type Found");
                                    }


                                } else {
//                                    Toast.makeText(context, "Target Qty not Matched", Toast.LENGTH_SHORT).show();
                                }

                            }

                        }

                        //Check if DivId > 0
                        else if (discountList.get(startPos).getDivId() > 0) {

                            Timber.d("getDivId is > 0 case");

                            if (discountList.get(startPos).getDiscountType().equalsIgnoreCase("Value"))
                            {
                                Timber.d("Value Type is running inside Divid");
                                // Step4 Check Target amount Meet or not
                                if (totalAmount == discountList.get(startPos).getTargetValue() || totalAmount > discountList.get(startPos).getTargetValue()) {
                                    Timber.d("Target Value Matched");

                                    model.setDiscountPolicyId(discountList.get(startPos).getDiscountPolicyId());
                                    //Step:5: Check which type of Discount Applied (Fix value/Percentage
                                    if (discountList.get(startPos).getDiscountPercentage() > 0) {


                                        //Todo for Percentage Type Calculation and Minus from Total Amount(done)
                                        model.setDiscPercentage(discountList.get(startPos).getDiscountPercentage());
                                        disValue = (totalAmount* discountList.get(startPos).getDiscountPercentage())/100;
                                        totalAmount = totalAmount -disValue;



                                    } else {

                                        //Todo Simply Minus discountList.get(position).getdiscountValue from total amount...(done)
                                        Timber.d("Before calculation the amount is "+totalAmount);
                                        totalAmount = totalAmount - discountList.get(startPos).getDiscountValue();
                                        Timber.d("here inside the Total amount is "+totalAmount);
                                    }


                                } else {
                                    Toast.makeText(context, "Target Value not Matched", Toast.LENGTH_SHORT).show();
                                }
                            } else if (discountList.get(startPos).getDiscountType().equalsIgnoreCase("Qty")) // Else Check for Qty type

                            {
                                Timber.d("Qty type is running inside divid");

                                if (ctnqty == discountList.get(startPos).getTargetQty() || ctnqty > discountList.get(startPos).getTargetQty()) {

                                    model.setDiscountPolicyId(discountList.get(startPos).getDiscountPolicyId());

                                    Timber.d("Target qty is matched inside Divid");
                                    //Step:5: Check which type of Discount Applied (Fix value/Percentage
                                    if (discountList.get(startPos).getDiscountPercentage() > 0) {

                                        //Todo for Percentage Type Calculation and Minus from Total Amount(done)
                                        model.setDiscPercentage(discountList.get(startPos).getDiscountPercentage());
                                        disValue = (totalAmount* discountList.get(startPos).getDiscountPercentage())/100;
                                        totalAmount = totalAmount -disValue;

                                    }
                                    else
                                    {
                                        //Todo Simply Minus discountList.get(position).getdiscountValue from total amount...(done)
                                        Timber.d("Before calculation the amount is "+totalAmount);
                                        totalAmount = totalAmount - discountList.get(startPos).getDiscountValue();
                                        Timber.d("here inside the Total amount is "+totalAmount);
                                    }


                                } else {
                                    Toast.makeText(context, "Target Qty not Matched", Toast.LENGTH_SHORT).show();
                                }

                            }


                        } else if (discountList.get(startPos).getGroupDivId() > 0) {

                            Timber.d("getGroupDivId is > 0 case");

                            if (discountList.get(startPos).getDiscountType().equalsIgnoreCase("Value")) //Third if Start
                            {
                                Timber.d("Group Div Value case is running");
                                // Step4 Check Target amount Meet or not
                                if (totalAmount == discountList.get(startPos).getTargetValue() || totalAmount > discountList.get(startPos).getTargetValue()) {
                                    model.setDiscountPolicyId(discountList.get(startPos).getDiscountPolicyId());
                                    Timber.d("Target Value matched inside groupDivid");
                                    //Step:5: Check which type of Discount Applied (Fix value/Percentage
                                    if (discountList.get(startPos).getDiscountPercentage() > 0) {

                                        //Todo for Percentage Type Calculation and Minus from Total Amount(done)
                                        model.setDiscPercentage(discountList.get(startPos).getDiscountPercentage());
                                        disValue = (totalAmount* discountList.get(startPos).getDiscountPercentage())/100;
                                        totalAmount = totalAmount -disValue;

                                    } else {

                                        //Todo Simply Minus discountList.get(position).getdiscountValue from total amount...(done)
                                        Timber.d("Before calculation the amount is "+totalAmount);
                                        totalAmount = totalAmount - discountList.get(startPos).getDiscountValue();
                                        Timber.d("here inside the Total amount is "+totalAmount);
                                    }


                                } else {
                                    Toast.makeText(context, "Target Value not Matched", Toast.LENGTH_SHORT).show();
                                }
                            } else if (discountList.get(startPos).getDiscountType().equalsIgnoreCase("Qty")) // Else Check for Qty type

                            {
                                Timber.d("Group div Qty type is running");

                                if (ctnqty == discountList.get(startPos).getTargetQty() || ctnqty > discountList.get(startPos).getTargetQty()) {
                                    model.setDiscountPolicyId(discountList.get(startPos).getDiscountPolicyId());
                                    //Step:5: Check which type of Discount Applied (Fix value/Percentage
                                    if (discountList.get(startPos).getDiscountPercentage() > 0) {

                                        //Todo for Percentage Type Calculation and Minus from Total Amount(done)
                                        model.setDiscPercentage(discountList.get(startPos).getDiscountPercentage());
                                        disValue = (totalAmount* discountList.get(startPos).getDiscountPercentage())/100;
                                        totalAmount = totalAmount -disValue;


                                    } else if (discountList.get(startPos).getDiscountValue() > 0) {

                                        //Todo Simply Minus discountList.get(position).getdiscountValue from total amount...(done)
                                        Timber.d("Before calculation the amount is "+totalAmount);
                                        totalAmount = totalAmount - discountList.get(startPos).getDiscountValue();
                                        Timber.d("here inside the Total amount is "+totalAmount);

                                    } else {
                                        Timber.d("Nothing from Value/Qty Type Found");
                                    }


                                } else {
                                    Toast.makeText(context, "Target Qty not Matched", Toast.LENGTH_SHORT).show();
                                }

                            }
                        } else {
                            Timber.d("In Customers Not any Discount Found");
                        }


                }
            }

        }
        catch (Exception e)
        {
            Timber.d("Error is "+e.getMessage());
        }


    }


    //Function for discount policies
    public  void applyDiscountPolicyShort(float totalAmount, int ctnqty, ArrayList<DiscountPolicyModel> discountList, String decision){

        String decide = decision;

        if(decide.equalsIgnoreCase("item"))
        {

            Timber.d("applyDiscountPolicyShort function is running for item  ");

            //Todo Discount Policy work
            try {

                //Todo discount policy for customer
                if(discountList.size()>0) {

                    {
                        int startPos = 0;
                        Timber.d("---------------------------------------------------");
                        Timber.d("Item is "+ discountList.get(startPos).getTypeId());
                        Timber.d("---------------------------------------------------");


                        // Step2: Check ItemId>0
                        Timber.d(" item id is "+ discountList.get(startPos).getTypeId());
                        if (discountList.get(startPos).getTypeId() > 0 && discountList.get(startPos).getType().equalsIgnoreCase("Item")) //Second If
                        {

                            Timber.d("itemId is > 0 case ");
                            //Step3: Check Type of Discount Policy Applied ( Value / Qty )
                            if (discountList.get(startPos).getDiscountType().equalsIgnoreCase("Value")) //Third if Start
                            {

                                Timber.d("Value type is running inside itemid");

                                Timber.d("Discountlist size is "+discountList.size());
                                Timber.d("Target value is "+discountList.get(startPos).getTargetValue());
                                Timber.d("Total amount is "+totalAmount);

                                // Step4 Check Target amount Meet or not
                                if (totalAmount == discountList.get(startPos).getTargetValue() || totalAmount > discountList.get(startPos).getTargetValue()) {

                                    model.setDiscountPolicyId(discountList.get(startPos).getDiscountPolicyId());
                                    //Step:5: Check which type of Discount Applied (Fix value/Percentage)
                                    if (discountList.get(startPos).getDiscountPercentage() > 0) {

                                        //Todo for Percentage Type Calculation and Minus from Total Amount(done)
                                        model.setDiscPercentage(discountList.get(startPos).getDiscountPercentage());
                                        disValue = (totalAmount* discountList.get(startPos).getDiscountPercentage())/100;
                                        totalAmount = totalAmount -disValue;


                                    } else {

                                        //Todo Simply Minus discountList.get(position).getdiscountValue from total amount...(done)
                                        Timber.d("Before calculation the amount is "+totalAmount);

                                        disValue = discountList.get(startPos).getDiscountValue();
                                        totalAmount = totalAmount - disValue;
                                        Timber.d("here inside the Total amount is "+totalAmount);

                                    }


                                } else {
                                    Toast.makeText(context, "Target Value not Matched", Toast.LENGTH_SHORT).show();
                                }
                            } else if (discountList.get(startPos).getDiscountType().equalsIgnoreCase("Qty")) // Else Check for Qty type

                            {
                                Timber.d("Qty type is running inside itemid");
                                if (ctnqty == discountList.get(startPos).getTargetQty() || ctnqty > discountList.get(startPos).getTargetQty()) {
                                    //Step:5: Check which type of Discount Applied (Fix value/Percentage)

                                    model.setDiscountPolicyId(discountList.get(startPos).getDiscountPolicyId());

                                    if (discountList.get(startPos).getDiscountPercentage() > 0) {
                                        Timber.d("Percent type is running inside qty inside itemid");
                                        Timber.d("Value before is "+totalAmount);


                                        model.setDiscPercentage(discountList.get(startPos).getDiscountPercentage());
                                        disValue = (totalAmount* discountList.get(startPos).getDiscountPercentage())/100;
                                        totalAmount = totalAmount - disValue;

                                        Timber.d("Value after is "+totalAmount);


                                    } else if (discountList.get(startPos).getDiscountValue() > 0) {
                                        //Todo Simply Minus discountList.get(position).getdiscountValue from total amount...

                                        disValue = discountList.get(startPos).getDiscountValue();
                                        totalAmount = totalAmount - disValue;

                                    } else {
                                        Timber.d("Nothing from Value/Qty Type Found");
                                    }


                                } else {
                                    Toast.makeText(context, "Target Qty not Matched", Toast.LENGTH_SHORT).show();
                                }

                            }

                        } else {
                            Timber.d("In Customers Not any Discount Found");
                        }


                    }
                }

            }
            catch (Exception e)
            {
                Timber.d("Error is "+e.getMessage());
            }


        }
        else if(decide.equalsIgnoreCase("division") ){

            Timber.d("applyDiscountPolicyShort function is running for division");

            //Todo Discount Policy work
            try {

                //Todo discount policy for customer
                if(discountList.size()>0) {

                    {
                        int startPos = 0;
                        Timber.d("---------------------------------------------------");
                        Timber.d("Divsion id is "+ discountList.get(startPos).getTypeId());
                        Timber.d("---------------------------------------------------");
                        Timber.d("targetvalue is "+discountList.get(startPos).getTargetValue());


                        if (discountList.get(startPos).getTypeId() > 0 && discountList.get(0).getType().equalsIgnoreCase("division")) //Second If
                        {
                            Timber.d("division is > 0 case ");
                            //Step3: Check Type of Discount Policy Applied ( Value / Qty )
                            if (discountList.get(startPos).getDiscountType().equalsIgnoreCase("Value")) //Third if Start
                            {

                                Timber.d("Value type is running inside division");

                                Timber.d("Discountlist size is "+discountList.size());
                                Timber.d("Target value is "+discountList.get(startPos).getTargetValue());
                                Timber.d("Total amount is "+totalAmount);

                                // Step4 Check Target amount Meet or not
                                if (totalAmount == discountList.get(startPos).getTargetValue() || totalAmount > discountList.get(startPos).getTargetValue()) {

                                    model.setDiscountPolicyId(discountList.get(startPos).getDiscountPolicyId());

                                    //Step:5: Check which type of Discount Applied (Fix value/Percentage)
                                    if (discountList.get(startPos).getDiscountPercentage() > 0) {


                                        //Todo for Percentage Type Calculation and Minus from Total Amount(done)
                                        model.setDiscPercentage(discountList.get(startPos).getDiscountPercentage());
                                        disValue = (totalAmount* discountList.get(startPos).getDiscountPercentage())/100;
                                        totalAmount = totalAmount -disValue;


                                    } else {

                                        //Todo Simply Minus discountList.get(position).getdiscountValue from total amount...(done)
                                        Timber.d("Before calculation the amount is "+totalAmount);

                                        disValue = discountList.get(startPos).getDiscountValue();
                                        totalAmount = totalAmount - disValue;
                                        Timber.d("here inside the Total amount is "+totalAmount);

                                    }


                                } else {
                                    Toast.makeText(context, "Target Value not Matched", Toast.LENGTH_SHORT).show();
                                }
                            } else if (discountList.get(startPos).getDiscountType().equalsIgnoreCase("Qty")) // Else Check for Qty type

                            {
                                Timber.d("Qty type is running inside division");


                                if (ctnqty == discountList.get(startPos).getTargetQty() || ctnqty > discountList.get(startPos).getTargetQty()) {

                                    model.setDiscountPolicyId(discountList.get(startPos).getDiscountPolicyId());

                                    //Step:5: Check which type of Discount Applied (Fix value/Percentage)
                                    if (discountList.get(startPos).getDiscountPercentage() > 0) {
                                        Timber.d("Percent type is running iside qty inside division");
                                        Timber.d("Value before is "+totalAmount);

                                        model.setDiscPercentage(discountList.get(startPos).getDiscountPercentage());
                                        disValue = (totalAmount* discountList.get(startPos).getDiscountPercentage())/100;
                                        totalAmount = totalAmount - disValue;

                                        Timber.d("Value after is "+totalAmount);


                                    } else if (discountList.get(startPos).getDiscountValue() > 0) {
                                        //Todo Simply Minus discountList.get(position).getdiscountValue from total amount...

                                        disValue = discountList.get(startPos).getDiscountValue();
                                        totalAmount = totalAmount - disValue;

                                    } else {
                                        Timber.d("Nothing from Value/Qty Type Found");
                                    }


                                } else {
                                    Toast.makeText(context, "Target Qty not Matched", Toast.LENGTH_SHORT).show();
                                }

                            }

                        } else {
                            Timber.d("In Division Not any Discount Found");
                        }


                    }
                }

            }
            catch (Exception e)
            {
                Timber.d("Error is "+e.getMessage());
            }

        }
        else if(decide.equalsIgnoreCase("brand")){

            //Todo Discount Policy work
            try {

                //Todo discount policy for brand(done)
                if(discountList.size()>0) {

                    {
                        int startPos = 0;
                        Timber.d("---------------------------------------------------");
                        Timber.d("brandid is "+ discountList.get(startPos).getTypeId());
                        Timber.d("---------------------------------------------------");


                        if (discountList.get(startPos).getTypeId() > 0 && discountList.get(startPos).getType().equalsIgnoreCase("brand")) //Second If
                        {

                            Timber.d("getGroupDivId is > 0 case ");
                            //Step3: Check Type of Discount Policy Applied ( Value / Qty )
                            if (discountList.get(startPos).getDiscountType().equalsIgnoreCase("Value")) //Third if Start
                            {

                                Timber.d("Value type is running inside itemid");

                                Timber.d("Discountlist size is "+discountList.size());
                                Timber.d("Target value is "+discountList.get(startPos).getTargetValue());
                                Timber.d("Total amount is "+totalAmount);

                                // Step4 Check Target amount Meet or not
                                if (totalAmount == discountList.get(startPos).getTargetValue() || totalAmount > discountList.get(startPos).getTargetValue()) {

                                    model.setDiscountPolicyId(discountList.get(startPos).getDiscountPolicyId());
                                    //Step:5: Check which type of Discount Applied (Fix value/Percentage)
                                    if (discountList.get(startPos).getDiscountPercentage() > 0) {

                                        //Todo for Percentage Type Calculation and Minus from Total Amount(done)
                                        model.setDiscPercentage(discountList.get(startPos).getDiscountPercentage());
                                        disValue = (totalAmount* discountList.get(startPos).getDiscountPercentage())/100;
                                        totalAmount = totalAmount -disValue;


                                    } else {

                                        //Todo Simply Minus discountList.get(position).getdiscountValue from total amount...(done)
                                        Timber.d("Before calculation the amount is "+totalAmount);

                                        disValue = discountList.get(startPos).getDiscountValue();
                                        totalAmount = totalAmount - disValue;
                                        Timber.d("here inside the Total amount is "+totalAmount);

                                    }


                                } else {
//                                    Toast.makeText(context, "Target Value not Matched", Toast.LENGTH_SHORT).show();
                                }
                            } else if (discountList.get(startPos).getDiscountType().equalsIgnoreCase("Qty")) // Else Check for Qty type

                            {
                                Timber.d("Qty type is running inside brand");
                                if (ctnqty == discountList.get(startPos).getTargetQty() || ctnqty > discountList.get(startPos).getTargetQty()) {
                                    model.setDiscountPolicyId(discountList.get(startPos).getDiscountPolicyId());

                                    //Step:5: Check which type of Discount Applied (Fix value/Percentage)
                                    if (discountList.get(startPos).getDiscountPercentage() > 0) {
                                        Timber.d("Percent type is running iside qty inside brand");
                                        Timber.d("Value before is "+totalAmount);

                                        model.setDiscPercentage(discountList.get(startPos).getDiscountPercentage());
                                        disValue = (totalAmount* discountList.get(startPos).getDiscountPercentage())/100;
                                        totalAmount = totalAmount - disValue;

                                        Timber.d("Value after is "+totalAmount);


                                    } else if (discountList.get(startPos).getDiscountValue() > 0) {
                                        //Todo Simply Minus discountList.get(position).getdiscountValue from total amount...

                                        disValue = discountList.get(startPos).getDiscountValue();
                                        totalAmount = totalAmount - disValue;

                                    } else {
                                        Timber.d("Nothing from Value/Qty Type Found");
                                    }


                                } else {
//                                    Toast.makeText(context, "Target Qty not Matched", Toast.LENGTH_SHORT).show();
                                }

                            }

                        } else {
                            Timber.d("In Brand Not any Discount Found");
                        }


                    }
                }

            }
            catch (Exception e)
            {
                Timber.d("Error is "+e.getMessage());
            }

        }
    }


    //Function for Bonus policies
    public  void applyBonusPolicy(int ctnqty, ArrayList<BonusPolicyModel> bonusList, String decision){

        String decide = decision;
        String ItemName = "";
        String ItemTaxCode = "";
        float gstPer = 0.0f;
        float bonusRetail = 0.0f;
        float bonusItemsGst=0.0f;
        int startPos = 0;


        Timber.d("Bonus Policy running for "+decide);


            //todo for Customer Bonus Policy
            try {

                Timber.d("The Target Qty inside Customer is "+bonusList.get(startPos).getTargetQty()+" and ctnqty is "+ctnqty);

                if(ctnqty == bonusList.get(startPos).getTargetQty() || ctnqty > bonusList.get(startPos).getTargetQty() ){
                    Timber.d("Target Quantity matched");

                    Timber.d("The incentiveItemId is "+bonusList.get(startPos).getIncentiveItemId() +" and ItemId is "+model.getItem_id());

                    if(bonusList.get(startPos).getIncentiveItemId() > 0 )
                    {

                        Timber.d("Bonus Policy will be added as another Item");


                        ItemName ="";
                        gstPer = 0.0f;
                        ItemTaxCode="";
                        bonusRetail=0.0f;
                        bonusItemsGst = 0.0f;


                        ItemName    =  db.getItemName(bonusList.get(startPos).getIncentiveItemId());
                        ItemTaxCode =  db.getTaxCode(bonusList.get(startPos).getIncentiveItemId());
                        gstPer      =  db.getItemgstPer(bonusList.get(startPos).getIncentiveItemId());
                        bonusRetail =  db.getBonusRetail(bonusList.get(startPos).getIncentiveItemId());
                        bonusItemsGst = caluclateGst(bonusList.get(startPos).getBonus(),bonusRetail,gstPer,ItemTaxCode);

                        model.setBonusItemName(ItemName);
                        model.setBonusIncentiveItemId(bonusList.get(startPos).getIncentiveItemId());
                        model.setBonusPolicyId(bonusList.get(startPos).getBonusPolicesId());
                        model.setBonusQty(bonusList.get(startPos).getBonus());
                        model.setBonusItemsGst(bonusItemsGst);

                        
                    }
                    else
                    {

                        Timber.d("Bonus Policy will be added as Same Item");

                        model.setBonusItemName(model.getName());
                        model.setBonusIncentiveItemId(model.getItem_id());
                        model.setBonusPolicyId(bonusList.get(startPos).getBonusPolicesId());
                        model.setBonusQty(bonusList.get(startPos).getBonus());


                        bonusItemsGst = 0.0f;
                        bonusItemsGst = caluclateGst(model.getBonusQty(),model.getWSCtnPrice(),model.getItemGstPer(),model.getTaxCode());
                        model.setBonusItemsGst(bonusItemsGst);





                    }

                }else{
                    Timber.d("Target Qty not matched for "+model.getName());
                }


            }catch (Exception e){
                Timber.d("Exception inside Bonus is "+e.getMessage());
            }


        }

    private float caluclateGst(int bonusQty, float bonusRetail, float gstPerr, String taxCode) {


        float gst=0.0f;

        float totalAmount = bonusQty  * bonusRetail;
        float gstPer   = gstPerr;


        if (taxCode.equalsIgnoreCase("3rd") ||
                taxCode.equalsIgnoreCase("SR")) {


            gst = (gstPer / 100f) * totalAmount;

        } else {
            gst = 0.0f;
        }


        return gst;
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
