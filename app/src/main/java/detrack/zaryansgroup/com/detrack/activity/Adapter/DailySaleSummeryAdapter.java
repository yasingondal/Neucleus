package detrack.zaryansgroup.com.detrack.activity.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import detrack.zaryansgroup.com.detrack.activity.Model.CompanyItemsModel.DeliveryItemModel;
import detrack.zaryansgroup.com.detrack.activity.Model.DailySummeryModel;
import detrack.zaryansgroup.com.detrack.R;
import detrack.zaryansgroup.com.detrack.activity.SharedPreferences.SharedPrefs;
import timber.log.Timber;

/**
 * Created by 6520 on 2/3/2016.
 */
public class DailySaleSummeryAdapter extends BaseAdapter {

    SharedPrefs prefs;

    Context context;
    ArrayList<DailySummeryModel> list;
    LayoutInflater inflater;
    DailySummeryModel model;

    public DailySaleSummeryAdapter(Context context, ArrayList<DailySummeryModel> list) {
        this.context = context;
        this.list = list;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        prefs = new SharedPrefs(context);
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

        View view = inflater.inflate(R.layout.sale_row_layout,null);
        TextView srno = view.findViewById(R.id.srno);
        TextView name = view.findViewById(R.id.name);
        TextView qty = view.findViewById(R.id.qty);
        TextView grossAmount = view.findViewById(R.id.grossAmount);
        TextView amount = view.findViewById(R.id.amount);
        TextView disc = view.findViewById(R.id.disc);
        TextView cash = view.findViewById(R.id.cash);
        TextView credit = view.findViewById(R.id.credit);
        TextView receipt = view.findViewById(R.id.receipt);
        TextView tvsaleType = view.findViewById(R.id.tvsaleType);



        model = list.get(position);
        srno.setText(String.valueOf(position+1));
        name.setText(model.getCustomerName());
        qty.setText(String.valueOf(model.getItemQty()));
        disc.setText(model.getDisc());
        grossAmount.setText(String.valueOf(model.getGrossTotal()));


        if(model.getOrderStatus()!=null){

        if(model.getOrderStatus().equals("Returned")){
            tvsaleType.setText("Return");

            if(model.getCustomerSalesMode().equalsIgnoreCase("Credit"))
            {
                credit.setText(String.valueOf(model.getCredit() * -1));
            }else{
                cash.setText(String.valueOf(model.getCash() * -1));
            }

            amount.setText(String.valueOf(model.getAmount() * -1));
            qty.setText(String.valueOf(model.getItemQty() * -1));
        }
        else{
            amount.setText(String.valueOf(model.getAmount()));
            cash.setText(String.valueOf(model.getCash()));
            credit.setText(String.valueOf(model.getCredit()));
        }

        }else{
            tvsaleType.setText("Recp");
        }

        receipt.setText(model.getReceipt());
        return view;
    }
}

