package detrack.zaryansgroup.com.detrack.activity.activites.TestAct;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import detrack.zaryansgroup.com.detrack.R;
import detrack.zaryansgroup.com.detrack.activity.SharedPreferences.SharedPrefs;
import detrack.zaryansgroup.com.detrack.activity.activites.DailySaleSummary;
import detrack.zaryansgroup.com.detrack.activity.activites.NewUserActivity;
import detrack.zaryansgroup.com.detrack.activity.activites.ReceiptActivity;
import detrack.zaryansgroup.com.detrack.activity.activites.ReportsActivity;
import detrack.zaryansgroup.com.detrack.activity.activites.SelectCustomerActivity;
import detrack.zaryansgroup.com.detrack.activity.activites.SelectProductActivity;
import detrack.zaryansgroup.com.detrack.activity.activites.ShowTakenOrderActivity;
import detrack.zaryansgroup.com.detrack.activity.activites.TakeNewReceiptsActivity;
import detrack.zaryansgroup.com.detrack.activity.activites.TakeOrder;
import detrack.zaryansgroup.com.detrack.activity.utilites.Utility;

public class NavbarActivity extends AppCompatActivity {


    RelativeLayout layReport, newOrderLL, newCustomerReceiptLL, newCustomerLL,
            reportLL, syncedOrderLL, salesReturnLinear,receiptLL;
    TextView txtback;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navbar);

        layReport = findViewById(R.id.layReport);
        newOrderLL = findViewById(R.id.newOrderLL);
        newCustomerReceiptLL = findViewById(R.id.newCustomerReceiptLL);
        newCustomerLL = findViewById(R.id.newCustomerLL);
        reportLL = findViewById(R.id.reportLL);
        syncedOrderLL = findViewById(R.id.syncedOrderLL);
        receiptLL = findViewById(R.id.receiptLL);
        salesReturnLinear = findViewById(R.id.salesReturnLinear);
        txtback = findViewById(R.id.txtback);

        clickListner();

    }

    private void clickListner() {

        layReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NavbarActivity.this, ReportsActivity.class);
                startActivity(intent);
            }
        });

        newOrderLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (new SharedPrefs(NavbarActivity.this).getView().equals("secondView")) {
//                    deliveryInfo = null;
//                    startActivity(new Intent(NavbarActivity.this, TakeOrder.class).putExtra("addOrder", "true"));

                } else {
                    startActivity(new Intent(NavbarActivity.this, SelectCustomerActivity.class).
                            putExtra("updateLocation", "false").putExtra("addOrder", "true"));

                }
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        });

        newCustomerReceiptLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NavbarActivity.this, TakeNewReceiptsActivity.class));
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        });

        newCustomerLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NavbarActivity.this, NewUserActivity.class));
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        });

        reportLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NavbarActivity.this, DailySaleSummary.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        });

        syncedOrderLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(NavbarActivity.this, ShowTakenOrderActivity.class);
                intent.putExtra("title", "Synced Order");
                intent.putExtra("syncedOrder", "yes");
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        });

        receiptLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NavbarActivity.this, ReceiptActivity.class);
                intent.putExtra("Position", "2");
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        });


        salesReturnLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.salesReturnDialog(NavbarActivity.this);
            }
        });




        txtback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

}
