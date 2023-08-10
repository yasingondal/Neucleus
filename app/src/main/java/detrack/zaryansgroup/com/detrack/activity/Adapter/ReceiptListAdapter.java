package detrack.zaryansgroup.com.detrack.activity.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

import detrack.zaryansgroup.com.detrack.activity.Model.CompanyItemsModel.DeliveryItemModel;
import detrack.zaryansgroup.com.detrack.activity.Model.ReceiptModel;
import detrack.zaryansgroup.com.detrack.R;

/**
 * Created by 6520 on 2/3/2016.
 */
public class ReceiptListAdapter extends BaseAdapter {
    Context context;
    ArrayList<ReceiptModel> list;
    LayoutInflater inflater;
    boolean check=false;
    public ReceiptListAdapter(Context context, ArrayList<ReceiptModel> list) {
        this.context = context;
        this.list = list;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }



    public  int getListPosition(DeliveryItemModel model ){
        int position=-1;
        for(int i=0;i<list.size();i++){
            if(model.getItem_id()==list.get(i).getId()){
                position=i;
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

        View view = inflater.inflate(R.layout.receipt_row_layout,null);
        TextView customerTV = (TextView) view.findViewById(R.id.customerTV);
        TextView amountPaid = (TextView) view.findViewById(R.id.amountPaid);
        TextView balance = (TextView) view.findViewById(R.id.balance);
        TextView dateTV = (TextView) view.findViewById(R.id.dateTV);
        TextView isSync=(TextView)view.findViewById(R.id.syncTV);
        CheckBox cb= (CheckBox) view.findViewById(R.id.checkBox);

        //Bank work
        TextView CashDepositedBank = (TextView) view.findViewById(R.id.CashDepositedBank);

        customerTV.setText(list.get(position).getCustomerName());
        amountPaid.setText(list.get(position).getAmountPaid()+"");

//        Bank Deposited Id...
//        CashDepositedBank.setText(list.get(position).getCashDepositedBankName());
//        Log.d("BankNAmeis",list.get(position).getCashDepositedBankName());

        if(list.get(position).getCashDepositedBankName()!="")
        {
            CashDepositedBank.setText(list.get(position).getCashDepositedBankName());
        }
        else
        {
            CashDepositedBank.setText("Bank Not Found");
        }



        balance.setText(list.get(position).getBalance() + "");
        dateTV.setText(list.get(position).getDate());
        if(list.get(position).getIsSync()==0){
            isSync.setVisibility(View.VISIBLE);
            isSync.setTextColor(Color.parseColor("#E55B3C"));
            isSync.setText("Receipt Not Send");
        }
        else if(list.get(position).getIsSync()==1){
            isSync.setVisibility(View.VISIBLE);
            isSync.setTextColor(Color.parseColor("#728C00")); //green color
            isSync.setText("Receipt Sent");
        }
        if(check){
            cb.setVisibility(View.VISIBLE);
            if(list.get(position).isCBCheck()){
                cb.setChecked(true);
            }else if(!list.get(position).isCBCheck()) {
                cb.setChecked(false);
            }
        }
        return view;
    }
    public void isChecked(boolean check){
        this.check=check;
    }
}

