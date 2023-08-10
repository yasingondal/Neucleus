package detrack.zaryansgroup.com.detrack.activity.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;

import detrack.zaryansgroup.com.detrack.R;
import detrack.zaryansgroup.com.detrack.activity.Adapter.DeliveryListAdapter;
import detrack.zaryansgroup.com.detrack.activity.Adapter.Undelivered_Adapter;
import detrack.zaryansgroup.com.detrack.activity.Model.CompanyItemsModel.DeliveryItemModel;
import detrack.zaryansgroup.com.detrack.activity.Model.DeliveryInfo;
import detrack.zaryansgroup.com.detrack.activity.Model.OrderItemsListModel;
import detrack.zaryansgroup.com.detrack.activity.Model.ReceiptModel;
import detrack.zaryansgroup.com.detrack.activity.Model.RegisterCustomerModel.RegisterdCustomerModel;
import detrack.zaryansgroup.com.detrack.activity.SQLlite.ZEDTrackDB;
import detrack.zaryansgroup.com.detrack.activity.activites.MainActivity;
import detrack.zaryansgroup.com.detrack.activity.activites.POD_DashBoard;
import detrack.zaryansgroup.com.detrack.activity.activites.TestAct.TestPOD;
import detrack.zaryansgroup.com.detrack.activity.activites.WelcomeActivity;
import detrack.zaryansgroup.com.detrack.activity.utilites.RecyclerClickListner;
import detrack.zaryansgroup.com.detrack.activity.utilites.Utility;
import detrack.zaryansgroup.com.detrack.activity.viewmodels.Undelivered_ViewModel;
import detrack.zaryansgroup.com.detrack.databinding.AssignedorderLayouttestBinding;


public class UndeliveredFragment extends Fragment {

    AssignedorderLayouttestBinding mBinding;
    Undelivered_ViewModel undelivered_viewModel;
    ArrayList<DeliveryInfo> mOrderList = new ArrayList<>();
    ArrayList<RegisterdCustomerModel> mCustomerList = new ArrayList<>();
    ArrayList<RegisterdCustomerModel> mCustomerListFilter;
    ArrayList<OrderItemsListModel>mOrderItems = new ArrayList<>();
    ArrayList<OrderItemsListModel>mOrderItemsFilter;
    Undelivered_Adapter undeliveredAdapter;
    private ZEDTrackDB db;
    String ordertype;
    String query;
    ArrayList<Integer> searchIndexList ;
    ArrayList<DeliveryInfo>  filteredList;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        mBinding= DataBindingUtil.inflate(inflater, R.layout.assignedorder_layouttest, container, false);
        undelivered_viewModel =  new ViewModelProvider(requireActivity(), ViewModelProvider.AndroidViewModelFactory.
                getInstance(getActivity().getApplication())).get(Undelivered_ViewModel.class);

//        undelivered_viewModel=  new ViewModelProvider(requireActivity()).get(Undelivered_ViewModel.class);
        mBinding.setLifecycleOwner(getActivity());
        mBinding.setUndeliverViewmodel(undelivered_viewModel);


        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = new ZEDTrackDB(getContext());

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            ordertype = bundle.getString("type");
            if(ordertype.equalsIgnoreCase("Inprogress")){
                mBinding.txtheading.setText("Undelivered Orders");
            }
            else if (ordertype.equalsIgnoreCase("Delivered")){
                mBinding.txtheading.setText("Delivered Order");
            }
            else if(ordertype.equalsIgnoreCase("Returned")){
                mBinding.txtheading.setText("Returned Order");
            }else if(ordertype.equalsIgnoreCase("Booking")){
                mBinding.txtheading.setText("Booking Order");   
            }
        }


        loadData();
//        undelivered_viewModel.loadOrder("Inprogress",null);
        recyclerListner();
        openSearchBar();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    private void openSearchBar() {
        mBinding.searchET.addTextChangedListener(new SearchWatcher());
        mBinding.searchET.setHint("Search....");
        mBinding.searchET.setHintTextColor(Color.BLACK);
    }


    private void recyclerListner() {

       mBinding.recyAssignorder.addOnItemTouchListener(new RecyclerClickListner(getContext(), new RecyclerClickListner.OnItemClickListener() {
           @Override
           public void onItemClick(View view, int position) {
               DeliveryInfo model =mOrderList.get(position);
               Intent intent = new Intent(getActivity(), TestPOD.class);
//               Log.d("delivery_id", model.getServer_Delivery_id() + "    " + model.getDelivery_id());
               intent.putExtra("Delivery_id", model.getDelivery_id() + "");
               intent.putExtra("IsNew", "False");
               startActivity(intent);
//               overridePendingTransition(R.anim.right_in, R.anim.left_out);
           }
       }));
    }


    private void loadData() {

        if(!ordertype.equalsIgnoreCase("Inprogress")){
            mOrderList = db.getOrderDelivery(ordertype,"");
        }else {
            mOrderList = db.getSQLiteOrderDelivery(ordertype);
        }

        Log.d("LoadOrderO","s="+mOrderList.size());
        Utility.setProgressDialog(getContext(),"Loading","Please wait ...");
        Utility.showProgressDialog();
        for(int i=0; i<mOrderList.size(); i++){
            Log.d("cID","id="+mOrderList.get(i).getCustomer_id());
            RegisterdCustomerModel registerdCustomerModel = db.getCustomerById(mOrderList.get(i).getCustomer_id());

            mCustomerList.add(registerdCustomerModel);
            ArrayList<DeliveryItemModel> mlist =
                    db.getSQLiteOrderDeliveryItems(String.valueOf(mOrderList.get(i).getDelivery_id()),
                            "isNew",false);
            mOrderItems.add(new OrderItemsListModel(mlist));

        }
        Utility.hideProgressDialog();
        RecyclerInit();




    }

    private class SearchWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence c, int i, int i2, int i3) {
        }

        @Override
        public void onTextChanged(CharSequence c, int i, int i2, int i3) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            query = mBinding.searchET.getText().toString();
            filteredList = performSearch(mOrderList, query);
            Log.d("searchSize","="+searchIndexList.size());
            if(searchIndexList != null){
                for(int i=0; i<searchIndexList.size(); i++){
                    mCustomerListFilter.add(mCustomerList.get(searchIndexList.get(i)));
                    mOrderItemsFilter.add(mOrderItems.get(i));
                    Log.d("searchSizeI","="+searchIndexList.get(i));
                }
            }
            undeliveredAdapter = new Undelivered_Adapter(getContext(),
                    filteredList,mCustomerListFilter,mOrderItemsFilter);
            mBinding.recyAssignorder.setAdapter(undeliveredAdapter);
        }
    }


    private ArrayList<DeliveryInfo> performSearch(ArrayList<DeliveryInfo> modal, String query) {
        String[] queryByWords = query.toLowerCase().split("\\s+");
        ArrayList<DeliveryInfo> filter = new ArrayList<>();
        mCustomerListFilter = new ArrayList<>();
        mOrderItemsFilter = new ArrayList<>();
        searchIndexList = new ArrayList<>();
        if(modal == null ) return filter;
        for (int i = 0; i < modal.size(); i++) {
            DeliveryInfo data = modal.get(i);
            String name = data.getDeliver_to_name().toLowerCase();
            Utility.logCatMsg("Search query :" + name);
            for (String word : queryByWords) {
                int numberOfMatches = queryByWords.length;
                if (name.contains(word)) {
                    numberOfMatches--;
                    Utility.logCatMsg("Match " + name + " " + word);
                } else{
                    break;
                }

                if (numberOfMatches == 0) {
                    filter.add(data);
                    searchIndexList.add(i);
                }
            }
        }
        return filter;
    }

    private void RecyclerInit() {
        Log.d("undecheck","s:c="+mCustomerList.size()+" :o="+mOrderList.size()+": oI="+mOrderItems.size());

        if(mOrderList.size() > 0) mBinding.txtnoitem.setVisibility(View.GONE);
        undeliveredAdapter = new Undelivered_Adapter(getContext(),
                mOrderList,mCustomerList,mOrderItems);
        mBinding.recyAssignorder.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.recyAssignorder.setAdapter(undeliveredAdapter);
    }
}
