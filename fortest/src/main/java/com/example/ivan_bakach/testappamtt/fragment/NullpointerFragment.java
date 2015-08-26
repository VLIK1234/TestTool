package com.example.ivan_bakach.testappamtt.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.ivan_bakach.testappamtt.R;

/**
 * @author Ivan_Bakach
 * @version on 20.07.2015
 */
public class NullpointerFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_nullpointer, container, false);
        Button buttonNulllPointerException = (Button) rootView.findViewById(R.id.bt_nullpointer);
        buttonNulllPointerException.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                throw new NullPointerException();
            }
        });
        return rootView;
    }
}
