package detrack.zaryansgroup.com.detrack.activity.activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import detrack.zaryansgroup.com.detrack.R;
import detrack.zaryansgroup.com.detrack.activity.Adapter.View_All_Customers_Adapter;
import detrack.zaryansgroup.com.detrack.activity.Model.RegisterCustomerModel.RegisterdCustomerModel;
import detrack.zaryansgroup.com.detrack.activity.SQLlite.ZEDTrackDB;

public class View_Customers_Activity extends AppCompatActivity {

    RecyclerView Customers_RecyclerView;
    List<RegisterdCustomerModel> rRegisteredCustomersList;

    List<RegisterdCustomerModel> rFilteringPurposeList;
    ArrayList<RegisterdCustomerModel> rFilteredNameList;

    View_All_Customers_Adapter view_all_customers_adapter;
    ZEDTrackDB db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_customers);
        getSupportActionBar().setTitle("All Customers List");

        rRegisteredCustomersList = new ArrayList<>();
        rFilteringPurposeList = new ArrayList<>();
        rFilteredNameList = new ArrayList<>();

        db = new ZEDTrackDB(View_Customers_Activity.this);


        rRegisteredCustomersList = db.getAllRegisterCustomers();
        rFilteringPurposeList = rRegisteredCustomersList;
        view_all_customers_adapter = new View_All_Customers_Adapter(rRegisteredCustomersList,View_Customers_Activity.this);

        Customers_RecyclerView = findViewById(R.id.Customers_RecyclerView);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        Customers_RecyclerView.setLayoutManager(mLayoutManager);
        Customers_RecyclerView.setAdapter(view_all_customers_adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_search_bar,menu);
        MenuItem menuItem = menu.findItem(R.id.app_bar_search);
        SearchView searchView = (SearchView) menuItem.getActionView();

        searchView.setQueryHint("Search Customer here...");


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                rFilteredNameList.clear();
                rCustomersFilter(newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void rCustomersFilter(String newText) {
        //Making a new list to add all the notes that contain "newText" or searched Text...

        //For each loop on FilterNotesAllList for checking From whole Notes
        for(int i = 0; i< rFilteringPurposeList.size(); i++){

            // if whole notes contain SearchedText
            if(
                    rFilteringPurposeList.get(i).getName().trim().toLowerCase(Locale.ROOT).contains(newText)

            ){
                //add in a new list...
                rFilteredNameList.add(rFilteringPurposeList.get(i));
            }
        }

        //Calling Function that we made in adapter for searching
        this.view_all_customers_adapter.rSearchCustomers(rFilteredNameList);
    }
}