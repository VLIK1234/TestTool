package com.example.ivan_bakach.testappamtt.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.ivan_bakach.testappamtt.R;
import com.example.ivan_bakach.testappamtt.app.ActivityWithoutFragments;

/**
 @author Ivan_Bakach
 @version on 03.06.2015
 */

public class SecondPageFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_second_page, container, false);
        Button button = (Button) rootView.findViewById(R.id.button_activity_without_fragments);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getBaseContext(), ActivityWithoutFragments.class);
                startActivity(intent);
            }
        });
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
