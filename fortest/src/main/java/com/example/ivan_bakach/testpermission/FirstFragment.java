package com.example.ivan_bakach.testpermission;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.ivan_bakach.testpermission.app.SecondActivity;

/**
 * Created by Ivan_Bakach on 03.06.2015.
 */
public class FirstFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_first, container, false);

        Button button = (Button) rootView.findViewById(R.id.button_second);
        final TextView textView = (TextView) rootView.findViewById(R.id.list_app);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SecondActivity.class);
                getActivity().startActivity(intent);
            }
        });
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
}
