package com.example.ivan_bakach.testappamtt.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ivan_bakach.testappamtt.R;

/**
 * @author Ivan_Bakach
 * @version on 20.07.2015
 */
public class InvisibleFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_invisible, container, false);
    }
}
