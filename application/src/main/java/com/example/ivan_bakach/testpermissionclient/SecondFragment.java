package com.example.ivan_bakach.testpermissionclient;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

//import com.example.ivan_bakach.injectlibrary.FragmentInfoManger;
import com.example.ivan_bakach.testpermissionclient.app.ThirdActivity;

/**
 * Created by Ivan_Bakach on 03.06.2015.
 */
public class SecondFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = (ViewGroup) inflater.inflate(R.layout.fragment_second, container, false);
        Button button = (Button) rootView.findViewById(R.id.button_third);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String result = FragmentInfoManger.INSTANSE.getAllFragment(getActivity().getClass().getSimpleName());
//                Intent in = new Intent();
//                in.setAction("TAKE_LOG");
//                getActivity().sendBroadcast(in);
//                Toast.makeText(getActivity(), in.getAction(), Toast.LENGTH_LONG).show();
//                Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getActivity().getBaseContext(), ThirdActivity.class);
                startActivity(intent);
                Log.e(getClass().getName(), "Expected result222");
            }
        });
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
}
