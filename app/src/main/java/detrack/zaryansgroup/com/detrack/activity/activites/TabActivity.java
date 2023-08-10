package detrack.zaryansgroup.com.detrack.activity.activites;

import android.app.ActionBar;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;

import java.util.Calendar;

import detrack.zaryansgroup.com.detrack.activity.Map.GPSTracker;
import detrack.zaryansgroup.com.detrack.activity.Service.CompanyInfoService;
import detrack.zaryansgroup.com.detrack.activity.Service.GPSService;
import detrack.zaryansgroup.com.detrack.activity.SharedPreferences.SharedPrefs;
import detrack.zaryansgroup.com.detrack.activity.app_setttings.AppSettings;
import detrack.zaryansgroup.com.detrack.activity.utilites.ConnectionDetector;
import detrack.zaryansgroup.com.detrack.activity.utilites.Utility;
import detrack.zaryansgroup.com.detrack.R;

public class TabActivity extends android.app.TabActivity {
    ImageButton btnMenu;
    ListView  menuList;
    TextView actionbar;
    PendingIntent pintent;
    AlarmManager alarm;
    SharedPrefs prefs;
    private TabHost mTabHost,tabHost;
    AppSettings settings;
    boolean isBack=false;
    static SendMultipleSelectionEventToRuntimeActivity sendMultipleSelectionEventToRuntimeActivity;
    static SendMultipleSelectionEventToPlaindOrderActivity sendMultipleSelectionEventToPlainOrderActivity;
    public static String syncedOrder ="";

    public interface SendMultipleSelectionEventToRuntimeActivity {
        void MultipleSelectionEventCapturedInRuntimeActivity();
    }
    public interface SendMultipleSelectionEventToPlaindOrderActivity {
        void MultipleSelectionEventToPlaindOrderActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab2);
        prefs = new SharedPrefs(this);
        setUpActionBar(getActionBar());
        actionbar.setText("Planned Orders ");
        settings = AppSettings.init();
        settings.setContext(getApplicationContext());
        //setMenuList();
        InitilizeAlaram();
         tabHost = findViewById(android.R.id.tabhost);

        TabHost.TabSpec tab1 = tabHost.newTabSpec("First Tab");
        TabHost.TabSpec tab2 = tabHost.newTabSpec("Second Tab");
        TabHost.TabSpec tab3 = tabHost.newTabSpec("Third Tab");

        syncedOrder = getIntent().getStringExtra("syncedOrder");

        tab1.setIndicator("Planned Order");
        tab1.setContent(new Intent(this, MainActivity.class));

        tab2.setIndicator("Taken Order");
        tab2.setContent(new Intent(this, ShowTakenOrderActivity.class));

        tab3.setIndicator("Receipts");
        tab3.setContent(new Intent(this, ReceiptActivity.class));
        tabHost.addTab(tab1);
        tabHost.addTab(tab2);
         tabHost.addTab(tab3);
        for(int i=0;i<tabHost.getTabWidget().getChildCount();i++)
        {
            TextView tv = tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
            tv.setTextColor(Color.parseColor("#ffffff"));
        }
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String arg0) {
                actionbar.setTextColor(Color.parseColor("#ffffff"));
                if (tabHost.getCurrentTab() == 0) {
                    actionbar.setText("Planned Orders");
                } else if(tabHost.getCurrentTab() == 1){
                    isBack=false;
                    actionbar.setText("Runtime Orders");
                }
                else if(tabHost.getCurrentTab() == 2){
                    actionbar.setText("Receipts");
                }
            }
        });
        if(getIntent()!=null){
            try {
                String position=getIntent().getStringExtra("Position");
                tabHost.setCurrentTab(Integer.parseInt(position));
            }catch (Exception e){
                Utility.logCatMsg("Error "+e.getMessage());
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        for(int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            SpannableString spanString = new SpannableString(menu.getItem(i).getTitle().toString());
            spanString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, spanString.length(), 0); //fix the color to white
            item.setTitle(spanString);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.actionRegNewCustomer:{
                Intent intent = new Intent(TabActivity.this, NewUserActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                finish();
                break;
            }
            case R.id.actionSyncCompanyCustomerInfo:{
                if (ConnectionDetector.isConnectingToInternet(TabActivity.this)) {
                    startService(new Intent(TabActivity.this, CompanyInfoService.class));
                    Utility.Toast(TabActivity.this, "Getting customer info...");
                } else {
                    Utility.Toast(TabActivity.this, "Check network connection and try again");
                }
                break;
            }
            case R.id.actionAddSalesOrder:{
                Intent intent = new Intent(TabActivity.this, TakeOrder.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                finish();
                break;
            }
            case R.id.actionSettings:{
                Intent intent = new Intent(TabActivity.this, SettingActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                break;
            }
            case R.id.actionAddSalesReturn:{

                Intent intent = new Intent(TabActivity.this, ReturnOrderSearchActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                break;
            }
            default:{
                return false;
            }

        }

        return true;
    }

    public static void setSendMultipleSelectionEventToRuntimeActivity(SendMultipleSelectionEventToRuntimeActivity event){
        sendMultipleSelectionEventToRuntimeActivity = event;
    }
    public static void setSendMultipleSelectionEventToPlainOrderActivity(SendMultipleSelectionEventToPlaindOrderActivity event){
        sendMultipleSelectionEventToPlainOrderActivity = event;
    }
    private void setUpActionBar(ActionBar actionBar) {

        RelativeLayout mainLayout = findViewById(R.id.mainLayout);
        View v = getLayoutInflater().inflate(R.layout.actionbar_view, mainLayout, false);
        actionbar= v.findViewById(R.id.actionBarTextView);
        btnMenu = v.findViewById(R.id.btnMenu);
        btnMenu.setVisibility(View.GONE);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.appbluegrey)));
        actionBar.setCustomView(v);
    }
    private void setMenuList(){
        final String[] menuArray = new String[] {"Register New Customer","Enable Location","Disable Location","Sync Company Info","Take new Order","Send Multiple Orders","Settings","LogOut"};
        menuList = findViewById(R.id.menuList);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(TabActivity.this, R.layout.menu_list_row, menuArray);
        menuList.setAdapter(adapter);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (menuList.getVisibility() != View.VISIBLE)
                    menuList.setVisibility(View.VISIBLE);
                else
                    menuList.setVisibility(View.GONE);
            }
        });
        menuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String option = ((TextView) view).getText().toString();
                menuList.setVisibility(View.GONE);
                if (option.equals("Register New Customer")) {
                    Intent intent = new Intent(TabActivity.this, NewUserActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                    menuList.setVisibility(View.GONE);
                } else if (option.equals("Enable Location")) {
                    GPSTracker gps = new GPSTracker(TabActivity.this);
                    if (ConnectionDetector.isConnectingToInternet(TabActivity.this)) {
                        if (gps.canGetLocation()) {
                            Utility.Toast(TabActivity.this, "Location Enable Successfully");
                            startservice();
                            menuList.setVisibility(View.GONE);
                        } else {
                            Utility.Toast(TabActivity.this, "Enable your GPS first and try again..");
                            //gps.showSettingsAlert();
                        }
                    } else {
                        Utility.Toast(TabActivity.this, "Check network connection and try again");
                    }

                } else if (option.equals("Disable Location")) {
                    Utility.Toast(TabActivity.this, "Location Disable Successfully");
                    stopservice();
                    menuList.setVisibility(View.GONE);

                }  else if (option.equals("Sync Company Info")) {
                    if (ConnectionDetector.isConnectingToInternet(TabActivity.this)) {
                        startService(new Intent(TabActivity.this, CompanyInfoService.class));
                        Utility.Toast(TabActivity.this, "Syncing Start...");
                        menuList.setVisibility(View.GONE);
                    } else {
                        Utility.Toast(TabActivity.this, "Check network connection and try again");
                    }
                } else if (option.equals("Take new Order")) {
                    Intent intent = new Intent(TabActivity.this, TakeOrder.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                    menuList.setVisibility(View.GONE);
                } else if (option.equals("Search return order")) {
                    Intent intent = new Intent(TabActivity.this, ReturnOrderSearchActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                    menuList.setVisibility(View.GONE);
                } else if (option.equals("Send Multiple Orders")) {
                    if (tabHost.getCurrentTab() == 0) {
                        sendMultipleSelectionEventToPlainOrderActivity.MultipleSelectionEventToPlaindOrderActivity();
                    } else {
                        sendMultipleSelectionEventToRuntimeActivity.MultipleSelectionEventCapturedInRuntimeActivity();
                    }
                } else if (option.equals("Settings")) {
                    Intent intent = new Intent(TabActivity.this, SettingActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                } else if (option.equals("LogOut"))
                    logoutDialog();

            }
        });
    }
    private void logoutDialog(){
    AlertDialog.Builder builder=new AlertDialog.Builder(TabActivity.this);
    builder.setTitle("Logout").setMessage("Make sure you upload your orders to server,other wise you will be lost it.")
            .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    prefs.ClearPrefs();
                    Intent intent = new Intent(TabActivity.this, RegisterActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.left_in, R.anim.right_out);
                }
            })
            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
    AlertDialog dialog=builder.create();
    dialog.show();
}
    private void startservice() {
        Calendar cal = Calendar.getInstance();
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 1 * 10 * 1000, pintent);
    }
    private  void stopservice(){
        alarm.cancel(pintent);
    }
    private void InitilizeAlaram(){
        startService(new Intent(this, GPSService.class));

    }
    private void setupTab(final View view, final String tag, final String className) {
        View tabview = createTabView(mTabHost.getContext(), tag);
        Intent intent;
        intent = new Intent().setClass(this, MainActivity.class);
        if (className.equals("Search.class")) {

            intent = new Intent().setClass(this, MainActivity.class);
        }
        if (className.equals("ShowTakenOrderActivity.class")) {
            intent = new Intent().setClass(this, ShowTakenOrderActivity.class);}
        if (className.equals("ReturnOrderActivity.class")) {
            intent = new Intent().setClass(this, ReturnOrderActivity.class);}

        TabHost.TabSpec setContent = mTabHost.newTabSpec(tag).setIndicator(tabview).setContent(intent); {
        }
        mTabHost.addTab(setContent);
    }
    private static View createTabView(final Context context, final String text) {
        View view = LayoutInflater.from(context).inflate(R.layout.tabs_bg, null);
        TextView tv = view.findViewById(R.id.tabsText);
        tv.setText(text);
        return view;
    }

    @Override
    public void onBackPressed() {
     /* if(menuList.getVisibility()==View.VISIBLE)
          menuList.setVisibility(View.GONE);
       else if(tabHost.getCurrentTab() == 0) {
            if(isBack)
                finish();
            else {
                isBack = true;
                Utility.Toast(TabActivity.this,"Press again to exit");
        }
        }else {
            tabHost.setCurrentTab(0);
        }
*/
        finish();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }
}
