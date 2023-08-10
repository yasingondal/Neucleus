package detrack.zaryansgroup.com.detrack.activity.activites;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import detrack.zaryansgroup.com.detrack.activity.Adapter.DeliveryListAdapter;
import detrack.zaryansgroup.com.detrack.activity.Model.DeliveryInfo;
import detrack.zaryansgroup.com.detrack.activity.Model.CompanyItemsModel.DeliveryItemModel;
import detrack.zaryansgroup.com.detrack.activity.SQLlite.ZEDTrackDB;
import detrack.zaryansgroup.com.detrack.activity.SharedPreferences.SharedPrefs;
import detrack.zaryansgroup.com.detrack.activity.utilites.Utility;
import detrack.zaryansgroup.com.detrack.R;

public class NewOrderNotificationActivity extends AppCompatActivity {
    Button startbtn;
    ListView dilervyJobListView;
    private DeliveryListAdapter listAdapter;
    ArrayList<DeliveryInfo> feedItems;
    ArrayList<DeliveryItemModel> feedDeliveryItems;
    TextView demotextTV, trackidTV;

    String allDelivery_id;
    ProgressBar pb;
    SharedPrefs prefs;
    ZEDTrackDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order_notification);
        if (getActionBar() != null) {
            setUpActionBar(getSupportActionBar());
        }
        dilervyJobListView = (ListView) findViewById(R.id.divery_job_list);
        startbtn = (Button) findViewById(R.id.startBtn);
        feedItems = new ArrayList<DeliveryInfo>();
        feedDeliveryItems = new ArrayList<DeliveryItemModel>();

        demotextTV = (TextView) findViewById(R.id.demotextTV);
        trackidTV = (TextView) findViewById(R.id.trackid);
        pb= (ProgressBar) findViewById(R.id.progressBar);
        prefs = new SharedPrefs(this);
        db = new ZEDTrackDB(NewOrderNotificationActivity.this);

        Fill_listtView_From_SqliteDB();
        dilervyJobListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DeliveryInfo model = (DeliveryInfo) parent.getItemAtPosition(position);
                Intent intent = new Intent(NewOrderNotificationActivity.this, POD_DashBoard.class);
                intent.putExtra("Delivery_id", model.getDelivery_id() + "");
                intent.putExtra("IsNew", "False");
                startActivity(intent);

            }
        });

    }
    private void setUpActionBar(ActionBar actionBar) {

        LinearLayout mainLayout = (LinearLayout) findViewById(R.id.mainLayout);
        View v = getLayoutInflater().inflate(R.layout.actionbar_view, mainLayout, false);
        TextView actionbar= (TextView) v.findViewById(R.id.actionBarTextView);
        // serach.setVisibility(View.VISIBLE);
        ImageButton btnMenu = (ImageButton) v.findViewById(R.id.btnMenu);
        btnMenu.setVisibility(View.GONE);
        actionbar.setText("New Order Details");
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.appbluegrey)));
        actionBar.setCustomView(v);
    }
    private  void  Fill_listtView_From_SqliteDB(){
        ArrayList<DeliveryInfo> list=new ArrayList<>();
        list=db.getSQLiteNotificationOrderDelivery("False");
        Utility.logCatMsg("Feed items from DB Size " + list.size());
        if(list.size()>0){
            demotextTV.setVisibility(View.GONE);
            listAdapter = new DeliveryListAdapter(this, list);
            dilervyJobListView.setAdapter(listAdapter);
            dilervyJobListView.setVisibility(View.VISIBLE);
        }

    }
}
