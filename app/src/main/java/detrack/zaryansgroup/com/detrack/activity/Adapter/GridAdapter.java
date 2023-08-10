package detrack.zaryansgroup.com.detrack.activity.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import detrack.zaryansgroup.com.detrack.activity.Model.ImagesModel;
import detrack.zaryansgroup.com.detrack.activity.utilites.Utility;
import detrack.zaryansgroup.com.detrack.R;

/**
 * Created by Farhan Ali on 12/12/2015.
 */
public class GridAdapter extends BaseAdapter
{
    Context context;
    ArrayList<ImagesModel> list;
    LayoutInflater inflater;

    public GridAdapter(Context context, ArrayList<ImagesModel> list) {
        this.context = context;
        this.list = list;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View view = inflater.inflate(R.layout.grid_layout, null);
        final ImageView iv = (ImageView) view.findViewById(R.id.img);

        String isSync = list.get(position).getImag_is_synced();

        if(isSync.equals("0")){

        Bitmap bm=Utility.getImage(list.get(position).getImage_name());

            if(bm!=null) {
                Glide.with(context)
                        .load(bm)
                        .into(iv);
            }else {
                iv.setImageResource(R.drawable.img_not_found);
            }
        }
        else{

                Glide.with(context)
                        .load(R.drawable.ic_alreadysent)
                        .into(iv);
        }

        /*if(true){
            iv.setImageBitmap(Utility.getImage("1457350166457.jpg"));
            progressBar.setVisibility(View.GONE);

        }else {
            Picasso.with(context).load(removeSpaces(list.get(position).getImage()))
                    .noFade().resize(150, 150).centerCrop()
                    .into(iv, new Callback() {
                        @Override
                        public void onSuccess() {
                            progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {
                            iv.setImageResource(R.drawable.no_image_available);
                            progressBar.setVisibility(View.GONE);
                        }
                    });
        }*/
        return view;
    }

    private String removeSpaces(String path){
        return path.replaceAll(" ", "%20");
    }
}