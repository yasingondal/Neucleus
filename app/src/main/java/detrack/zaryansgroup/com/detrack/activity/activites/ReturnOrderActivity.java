package detrack.zaryansgroup.com.detrack.activity.activites;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import detrack.zaryansgroup.com.detrack.activity.Adapter.DeliveryListAdapter;
import detrack.zaryansgroup.com.detrack.activity.Model.DeliveryInfo;
import detrack.zaryansgroup.com.detrack.activity.SQLlite.ZEDTrackDB;
import detrack.zaryansgroup.com.detrack.activity.SharedPreferences.SharedPrefs;
import detrack.zaryansgroup.com.detrack.activity.utilites.Utility;
import detrack.zaryansgroup.com.detrack.R;

public class ReturnOrderActivity extends AppCompatActivity implements TabActivity.SendMultipleSelectionEventToRuntimeActivity {
    EditText searchET;
    Button searchBtn;
    ListView returnJobListView;
    ProgressBar pb;
    SharedPrefs prefs;
    TextView demotextTV, trackidTV;
    ZEDTrackDB db;
    private DeliveryListAdapter listAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_order);
        searchBtn = (Button) findViewById(R.id.SearchBtn);
        searchET= (EditText) findViewById(R.id.serachET);
        returnJobListView = (ListView) findViewById(R.id.return_job_listview);
        pb= (ProgressBar) findViewById(R.id.progressBar);
        prefs = new SharedPrefs(this);
        demotextTV = (TextView) findViewById(R.id.demotextTV);
        trackidTV = (TextView) findViewById(R.id.trackid);
        db = new ZEDTrackDB(ReturnOrderActivity.this);


        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReturnOrderActivity.this, ReturnOrderSearchActivity.class);
                //  intent.putExtra("Delivery_id", model.getDelivery_id() + "");
                startActivity(intent);
            }
        });
        returnJobListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DeliveryInfo model = (DeliveryInfo) parent.getItemAtPosition(position);
                Intent intent = new Intent(ReturnOrderActivity.this, POD_DashBoard.class);
                intent.putExtra("Delivery_id", model.getDelivery_id() + "");
                intent.putExtra("IsNew", "Return");
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);

            }
        });

        returnJobListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                DeliveryInfo model = (DeliveryInfo) arg0.getItemAtPosition(pos);
                ItemOrder_diloag(model.getDelivery_id() + "");

                return true;
            }
        });
    }
    private  void  Fill_listtView_From_SqliteDB(){
        ArrayList<DeliveryInfo> list=new ArrayList<>();
        list=db.getSQLiteOrderDelivery("Return");
        pb.setVisibility(View.GONE);
        Utility.logCatMsg("Feed items from DB Size " + list.size());
        if(list.size()>0){
            demotextTV.setVisibility(View.GONE);

            listAdapter = new DeliveryListAdapter(this, list);
            returnJobListView.setAdapter(listAdapter);
            returnJobListView.setVisibility(View.VISIBLE);
        }
        else{
            demotextTV.setVisibility(View.VISIBLE);
            demotextTV.setText("No Orders return today..");

        }

    }
    @Override
    protected void onResume() {
        Fill_listtView_From_SqliteDB();
        super.onResume();
    }
    private void ItemOrder_diloag(final String Delivery_id) {

        CharSequence[] phone_action_items = {"Delete Order","Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Option");
        builder.setItems(phone_action_items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        if (item == 0) {
                            if (db.deleteOrder(Delivery_id, "Return")) {
                                Utility.Toast(ReturnOrderActivity.this, "Deleted");
                                onResume();
                            } else {
                                Utility.Toast(ReturnOrderActivity.this, "Not Deleted");
                            }
                            onResume();
                        } else if (item == 1) {
                            dialog.cancel();
                        }

                    }
                }

        );
        AlertDialog alert = builder.create();
        alert.show();

    }


    @Override
    public void MultipleSelectionEventCapturedInRuntimeActivity() {

    }
}
