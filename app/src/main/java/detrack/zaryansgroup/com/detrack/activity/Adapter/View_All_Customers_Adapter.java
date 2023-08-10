package detrack.zaryansgroup.com.detrack.activity.Adapter;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import detrack.zaryansgroup.com.detrack.R;
import detrack.zaryansgroup.com.detrack.activity.Model.RegisterCustomerModel.RegisterdCustomerModel;
import detrack.zaryansgroup.com.detrack.activity.utilites.Utility;
import timber.log.Timber;


public class View_All_Customers_Adapter extends RecyclerView.Adapter<View_All_Customers_Adapter.CustomerviewHolder> {

    List<RegisterdCustomerModel> rCustomersList;
    Context context;
    List<RegisterdCustomerModel> rAllCustomersList;

    public View_All_Customers_Adapter(List<RegisterdCustomerModel> rCustomersList, Context context) {
        this.rCustomersList = rCustomersList;
        this.context = context;
        rAllCustomersList = new ArrayList<>(rCustomersList);
    }

    public void rSearchCustomers(ArrayList<RegisterdCustomerModel> filterName) {
        this.rCustomersList = filterName;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public CustomerviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CustomerviewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.single_customer_listnew,
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerviewHolder holder, int position) {


        holder.txtCustomerName.setText(rCustomersList.get(position).getName());

        if(rCustomersList.get(position).getAddress()!=null){
            holder.txtCustomerAddress.setText(rCustomersList.get(position).getAddress());
        }else if(rCustomersList.get(position).getAddress1()!=null){
            holder.txtCustomerAddress.setText(rCustomersList.get(position).getAddress1());
        }else{
            holder.txtCustomerAddress.setText("Address Not Found");
        }

        String BaseUrl = Utility.CDownloadImages;

        if(rCustomersList.get(position).getImageName()!=null)
        {
            Glide.with(context)
                    .load(BaseUrl+ rCustomersList.get(position).getImageName())
                    .into(holder.rCustomerProfileImage);
        }else{
            Glide.with(context)
                    .load(R.drawable.ic_no_image)
                    .into(holder.rCustomerProfileImage);
        }
    }



    @Override
    public int getItemCount() {
      return  rCustomersList.size();
    }


    class CustomerviewHolder extends RecyclerView.ViewHolder{
        ImageView rCustomerProfileImage, rBtnCustomerCall;
        TextView txtCustomerAddress,txtCustomerName;
        public CustomerviewHolder(@NonNull View itemView) {
            super(itemView);
            rCustomerProfileImage = itemView.findViewById(R.id.CustomerProfileImage);
            rBtnCustomerCall = itemView.findViewById(R.id.BtnCustomerCall);
            txtCustomerAddress = itemView.findViewById(R.id.txtCustomerAddress);
            txtCustomerName = itemView.findViewById(R.id.txtCustomerName);


            rBtnCustomerCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(rCustomersList.get(getAdapterPosition()).getCell()!=null)
                    {
                        context.startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", rCustomersList.get(getAdapterPosition()).getCell(), null)));
                    }else
                    {
                        Toast.makeText(context.getApplicationContext(), "Cell Number Not Available", Toast.LENGTH_SHORT).show();
                    }

                }
            });

        }


    }

}
