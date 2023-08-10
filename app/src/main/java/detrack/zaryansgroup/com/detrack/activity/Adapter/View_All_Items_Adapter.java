package detrack.zaryansgroup.com.detrack.activity.Adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import detrack.zaryansgroup.com.detrack.R;
import detrack.zaryansgroup.com.detrack.activity.Model.CompanyItemsModel.DeliveryItemModel;
import detrack.zaryansgroup.com.detrack.activity.Model.RegisterCustomerModel.RegisterdCustomerModel;
import detrack.zaryansgroup.com.detrack.activity.SharedPreferences.SharedPrefs;
import detrack.zaryansgroup.com.detrack.activity.utilites.Utility;
import timber.log.Timber;


public class View_All_Items_Adapter extends RecyclerView.Adapter<View_All_Items_Adapter.ItemviewHolder> {

    List<DeliveryItemModel> ItemList;
    Context context;
    List<DeliveryItemModel> rAllItemsList;

    public View_All_Items_Adapter(List<DeliveryItemModel> itemList, Context context) {
        ItemList = itemList;
        this.context = context;
        rAllItemsList = new ArrayList<>(itemList);
    }



    public void rSearchProducts(ArrayList<DeliveryItemModel> rFilteredNameList) {
        this.ItemList = rFilteredNameList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ItemviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemviewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.single_product_list,
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ItemviewHolder holder, int position) {


        holder.productName.setText(ItemList.get(position).getName());

        if(new SharedPrefs(context).getDesignation().equalsIgnoreCase("Order Booker")){
            holder.productPrice.setText("--");
        }else{
            holder.productPrice.setText(ItemList.get(position).getDisplayPrice() + " Pkr");
        }



        holder.ProductCode.setText(ItemList.get(position).getSKU());


        if(ItemList.get(position).getImageName()!=null)
        {
           // Timber.d("Image name is not null case running");

            String BaseUrl = Utility.PDownloadImages;

          //  Timber.d("Image Link is :"+BaseUrl+ItemList.get(position).getImageName());

            Glide.with(context)
                    .load(BaseUrl+ItemList.get(position).getImageName())
                    .into(holder.productImage);
        }else{

        //    Timber.d("Image name is  null case running");

            Glide.with(context)
                    .load(R.drawable.product)
                    .into(holder.productImage);
        }
    }


    @Override
    public int getItemCount() {
        return ItemList.size();
    }



    class ItemviewHolder extends RecyclerView.ViewHolder {
        TextView productName, ProductCode, productPrice;
        ImageView productImage;

        public ItemviewHolder(@NonNull View itemView) {
            super(itemView);

            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            ProductCode = itemView.findViewById(R.id.ProductCode);
            productPrice = itemView.findViewById(R.id.productPrice);

        }
    }
}

