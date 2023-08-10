package detrack.zaryansgroup.com.detrack.activity.Fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import detrack.zaryansgroup.com.detrack.R;

public class SelectRoutesFragment extends DialogFragment {


    private RecyclerView routesListRecyclerView;



    public SelectRoutesFragment() {

    }


    public static SelectRoutesFragment newInstance(String title) {

        SelectRoutesFragment frag = new SelectRoutesFragment();

        Bundle args = new Bundle();

        args.putString("title", title);

        frag.setArguments(args);

        return frag;

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,

                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.select_routes_fragment, container);

    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        // Fetch arguments from bundle and set title

//        String title = getArguments().getString("title", "Enter Name");
//
//        getDialog().setTitle(title);
//
//        // Show soft keyboard automatically and request focus to field
//
//
//
//                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

    }



}
