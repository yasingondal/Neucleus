package detrack.zaryansgroup.com.detrack.activity.activites;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;

import detrack.zaryansgroup.com.detrack.activity.SharedPreferences.SharedPrefs;
import detrack.zaryansgroup.com.detrack.R;

public class SettingActivity extends AppCompatActivity {

    CheckBox cbFirstView, cbSecondView,cbThirdView;
    SharedPrefs prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        if (getActionBar() != null) {
            setUpActionBar(getSupportActionBar());
        }
        init();
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }

    private void init() {
        cbFirstView = findViewById(R.id.cbFirstView);
        cbSecondView = findViewById(R.id.cbSecondView);
        cbThirdView = findViewById(R.id.cbThirdView);
        prefs = new SharedPrefs(this);
        clickListener();

        String view = prefs.getView();
        if (view.equals("secondView")) {
            cbSecondView.setChecked(true);
            cbFirstView.setEnabled(false);
            cbThirdView.setEnabled(false);
        } else if(view.equals("thirdview")) {
            cbThirdView.setChecked(true);
            cbFirstView.setChecked(false);
            cbSecondView.setEnabled(false);
        }
        else{
            cbThirdView.setChecked(false);
            cbFirstView.setChecked(true);
            cbSecondView.setEnabled(false);
        }

    }

    private void clickListener() {

        cbFirstView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbFirstView.isChecked()) {
                    cbSecondView.setChecked(false);
                    prefs.setView("firstView");
                } else {
                    cbFirstView.setChecked(false);
                }
            }
        });

        cbSecondView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbSecondView.isChecked()) {
                    cbFirstView.setChecked(false);
                    prefs.setView("secondView");
                } else {
                    cbSecondView.setChecked(false);
                }
            }
        });
    }

    private void setUpActionBar(ActionBar actionBar) {

        ScrollView mainLayout = findViewById(R.id.mainLayout);
        View v = getLayoutInflater().inflate(R.layout.actionbar_view, mainLayout, false);
        TextView actionbar = v.findViewById(R.id.actionBarTextView);
        ImageButton btnMenu = v.findViewById(R.id.btnMenu);
        btnMenu.setVisibility(View.GONE);
        actionbar.setText("Setting");
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.appbluegrey)));
        actionBar.setCustomView(v);
    }


}
