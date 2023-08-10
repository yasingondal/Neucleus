package detrack.zaryansgroup.com.detrack.activity.Adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import detrack.zaryansgroup.com.detrack.activity.Model.CompanyItemsModel.DeliveryItemModel;
import detrack.zaryansgroup.com.detrack.R;
import timber.log.Timber;

public class EmptyBottleRecyclerViewAdapter extends RecyclerView.Adapter<EmptyBottleRecyclerViewAdapter.MyViewHolder> {

    ArrayList<DeliveryItemModel> selectedItemList;
    Context context;
    LayoutInflater inflater;

    public EmptyBottleRecyclerViewAdapter(Context context, ArrayList<DeliveryItemModel> selectedItemList){
        this.context = context;
        this.selectedItemList = selectedItemList;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

       View view = inflater.inflate(R.layout.empty_bottle_custom_layout, parent, false);

       MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {

        final DeliveryItemModel model = selectedItemList.get(position);
        holder.tvSelectedItemName.setText(model.getTitle());
        holder.etEmptyBottleQty.setText(model.getCtn_qty()+"");


        model.setEmptyBottles(model.getCtn_qty());


        holder.etEmptyBottleQty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(i2 > 0){
//                    Toast.makeText(context, "qty change for "+holder.tvSelectedItemName.getText().toString()+"   "+holder.etEmptyBottleQty.getText().toString(), Toast.LENGTH_SHORT).show();
//                    model.setEmptyBottles(Integer.parseInt(holder.etEmptyBottleQty.getText().toString()));
//                    Timber.d("Case if is Running");
                    Editable TempNewValue = holder.etEmptyBottleQty.getText();
                    model.setEmptyBottles(Integer.parseInt(TempNewValue.toString()));


                }
                else{
                    Toast.makeText(context, "Empty Bottles amount Should be Greater Than Zero", Toast.LENGTH_SHORT).show();
                    model.setEmptyBottles(0);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        if(!model.getEmptyFlag()){
            holder.tvSelectedItemName.setVisibility(View.GONE);
            holder.etEmptyBottleQty.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return selectedItemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvSelectedItemName;
        EditText etEmptyBottleQty;

        public MyViewHolder(View itemView) {
            super(itemView);

            tvSelectedItemName = itemView.findViewById(R.id.tvSelectedItemName);
            etEmptyBottleQty = itemView.findViewById(R.id.etEmptyBottleQty);
        }
    }
}
