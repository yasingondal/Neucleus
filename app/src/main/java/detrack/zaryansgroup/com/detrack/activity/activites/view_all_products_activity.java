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
import detrack.zaryansgroup.com.detrack.activity.Adapter.View_All_Items_Adapter;
import detrack.zaryansgroup.com.detrack.activity.Model.CompanyItemsModel.DeliveryItemModel;
import detrack.zaryansgroup.com.detrack.activity.SQLlite.ZEDTrackDB;
import timber.log.Timber;


public class view_all_products_activity extends AppCompatActivity {

    RecyclerView Products_RecyclerView;
    List<DeliveryItemModel> rwholeItemsList;
    View_All_Items_Adapter view_all_items_adapter;
    ZEDTrackDB db;

    List<DeliveryItemModel> rFilteringPurposeList;
    ArrayList<DeliveryItemModel> rFilteredNameList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_products);

        getSupportActionBar().setTitle("All Products List");
        db = new ZEDTrackDB(view_all_products_activity.this);
        rwholeItemsList = new ArrayList<>();
        rFilteringPurposeList = new ArrayList<>();
        rFilteredNameList = new ArrayList<>();

        rwholeItemsList = (List<DeliveryItemModel>) db.getAllItems();
        rFilteringPurposeList = rwholeItemsList;
        view_all_items_adapter = new View_All_Items_Adapter(rwholeItemsList,view_all_products_activity.this);
        Products_RecyclerView = findViewById(R.id.Products_RecyclerView);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        Products_RecyclerView.setLayoutManager(mLayoutManager);
        Products_RecyclerView.setAdapter(view_all_items_adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_search_bar,menu);
        MenuItem menuItem = menu.findItem(R.id.app_bar_search);
        SearchView searchView = (SearchView) menuItem.getActionView();

        searchView.setQueryHint("Search Items...");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                rFilteredNameList.clear();
                rProductsFilter(newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void rProductsFilter(String newText) {

        for(int i = 0; i< rFilteringPurposeList.size(); i++){

            if(
                    rFilteringPurposeList.get(i).getName().trim().toLowerCase(Locale.ROOT).contains(newText)

            ){
                //add in a new list...
                rFilteredNameList.add(rFilteringPurposeList.get(i));
            }
        }

        Timber.d("Filtered Name List size is "+rFilteredNameList.size());

        //Calling Function that we made in adapter for searching
        this.view_all_items_adapter.rSearchProducts(rFilteredNameList);
    }


}