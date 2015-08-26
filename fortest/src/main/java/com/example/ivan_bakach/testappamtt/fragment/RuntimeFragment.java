package com.example.ivan_bakach.testappamtt.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.ivan_bakach.testappamtt.R;

/**
 * @author Ivan_Bakach
 * @version on 20.07.2015
 */
public class RuntimeFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_runtime, container, false);
        Button mRuntest = (Button) rootView.findViewById(R.id.bt_runtime);
        mRuntest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "RuntimeException after 5 second", Toast.LENGTH_LONG).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        throw new RuntimeException("test exception");
                    }
                }, 5000l);
            }
        });
        return rootView;
    }

    public static Fragment newInstance() {
        RuntimeFragment runtimeFragment = new RuntimeFragment();
        Bundle args = new Bundle();
        args.putInt("int_key", 12345);
        args.putString("String_key", "Hello");
        args.putBoolean("boolean_key", false);
        runtimeFragment.setArguments(args);
        return runtimeFragment;
    }
}
