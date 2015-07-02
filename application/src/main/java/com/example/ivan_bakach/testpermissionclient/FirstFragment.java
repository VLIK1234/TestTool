package com.example.ivan_bakach.testpermissionclient;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ivan_bakach.testpermissionclient.app.SecondActivity;
import com.example.ivan_bakach.testpermissionclient.app.ThirdActivity;

import java.util.List;

/**
 * Created by Ivan_Bakach on 03.06.2015.
 */
public class FirstFragment extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_first, container, false);

        Button button = (Button) rootView.findViewById(R.id.button_second);
        final TextView textView = (TextView) rootView.findViewById(R.id.list_app);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
//                final List<ResolveInfo> pkgAppsList = getActivity().getPackageManager().queryIntentActivities(mainIntent, 0);
//                StringBuilder builder = new StringBuilder();
//                for (ResolveInfo info:pkgAppsList) {
//                    if (info.toString()!=null) {
//                        builder.append(info.toString().split(" ")[1]).append("\n");
//                    }
//                }
//                textView.setText(builder);
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
