package detrack.zaryansgroup.com.detrack.activity.Adapter;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;
import detrack.zaryansgroup.com.detrack.R;
import detrack.zaryansgroup.com.detrack.activity.Model.CustomerVisitedModel;
import detrack.zaryansgroup.com.detrack.activity.SQLlite.ZEDTrackDB;
import detrack.zaryansgroup.com.detrack.activity.SharedPreferences.SharedPrefs;
import detrack.zaryansgroup.com.detrack.activity.activites.VisitsActivity;
import detrack.zaryansgroup.com.detrack.activity.activites.WelcomeActivity;
import detrack.zaryansgroup.com.detrack.activity.utilites.ConnectionDetector;
import detrack.zaryansgroup.com.detrack.activity.utilites.Utility;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import timber.log.Timber;


public class VisitedCustomerAdapter extends RecyclerView.Adapter<VisitedCustomerAdapter.vhVisitedData> {

    List<CustomerVisitedModel> rVisitedCustomersList;
    Context context;
    SharedPrefs prefs;
    ZEDTrackDB db;

    boolean statusSync;
    ProgressDialog progressDialog;

    public VisitedCustomerAdapter(List<CustomerVisitedModel> rVisitedCustomersList, Context context) {
        this.rVisitedCustomersList = rVisitedCustomersList;
        this.context = context;
        prefs = new SharedPrefs(context);
        db = new ZEDTrackDB(context);
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Wait");
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Visit Saving to Local Database");
    }



    @NonNull
    @Override
    public vhVisitedData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VisitedCustomerAdapter.vhVisitedData(
                LayoutInflater.from(parent.getContext()).inflate(
                        detrack.zaryansgroup.com.detrack.R.layout.visit_row_layout,
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull vhVisitedData holder, int position) {

        int IsSync = rVisitedCustomersList.get(position).getIsSync();
        holder.tv_CustomerName.setText(rVisitedCustomersList.get(holder.getAdapterPosition()).getCustomerName());
        holder.tv_CustomerRoute.setText(rVisitedCustomersList.get(holder.getAdapterPosition()).getRouteName());
        holder.tv_VisitDate.setText(rVisitedCustomersList.get(holder.getAdapterPosition()).getVisitDate());
        holder.tv_VisitTime.setText(rVisitedCustomersList.get(holder.getAdapterPosition()).getVisitTime());
        holder.tv_VisitStatus.setText(rVisitedCustomersList.get(holder.getAdapterPosition()).getVisitStatus());


        if(IsSync == 0)
        {
            holder.tv_IsSync.setText("Please Click To Sync Visit");
        }
        else{
            holder.tv_IsSync.setText("Visit Already Sync");
        }


        holder.eachVisitLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(context);
                Window window = dialog.getWindow();
                window.setGravity(Gravity.CENTER);

                dialog.getWindow().getDecorView().setBackgroundColor(Color.TRANSPARENT);
                dialog.setContentView(R.layout.confirm_visit_sync_dialogue);

                if(rVisitedCustomersList.get(holder.getAdapterPosition()).getIsSync() == 0) {
                    dialog.show();


                    Button btnyes = dialog.findViewById(R.id.btnyes);
                    Button btnno = dialog.findViewById(R.id.btnno);

                    btnyes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();

                            if (ConnectionDetector.isConnectingToInternet(context.getApplicationContext())) {

                                int rCurrentPosition = holder.getAbsoluteAdapterPosition();
                                Intent intent = new Intent(context, VisitsActivity.class);
                                intent.putExtra("position", rCurrentPosition);
                                context.startActivity(intent);

                            } else {
                                Utility.Toast(context.getApplicationContext(), "Check network connection and try again");
                            }

                        }

                    });

                    btnno.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });


                }else{
                    Toast.makeText(context, "Visit Already Synced", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return rVisitedCustomersList.size();
    }

    class vhVisitedData extends RecyclerView.ViewHolder{

        TextView tv_CustomerName,tv_CustomerRoute,tv_VisitDate,tv_VisitTime,tv_VisitStatus,tv_IsSync;
        LinearLayout eachVisitLL;

        public vhVisitedData(@NonNull View itemView) {
            super(itemView);
            tv_CustomerName = itemView.findViewById(detrack.zaryansgroup.com.detrack.R.id.tv_CustomerName);
            tv_CustomerRoute = itemView.findViewById(detrack.zaryansgroup.com.detrack.R.id.tv_CustomerRoute);
            tv_VisitDate = itemView.findViewById(detrack.zaryansgroup.com.detrack.R.id.tv_VisitDate);
            tv_VisitTime = itemView.findViewById(detrack.zaryansgroup.com.detrack.R.id.tv_VisitTime);
            tv_VisitStatus = itemView.findViewById(detrack.zaryansgroup.com.detrack.R.id.tv_VisitStatus);
            tv_IsSync = itemView.findViewById(detrack.zaryansgroup.com.detrack.R.id.tv_IsSync);
            eachVisitLL = itemView.findViewById(detrack.zaryansgroup.com.detrack.R.id.eachVisitLL);

        }
    }
}
