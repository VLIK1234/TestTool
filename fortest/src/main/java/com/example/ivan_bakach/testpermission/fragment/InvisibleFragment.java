package com.example.ivan_bakach.testpermission.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ivan_bakach.testpermission.R;

/**
 * @author Ivan_Bakach
 * @version on 20.07.2015
 */
public class InvisibleFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_invisible, container, false);
        return rootView;
    }
}
