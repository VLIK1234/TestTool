package com.example.ivan_bakach.testpermissionclient.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.ivan_bakach.testpermissionclient.R;

/**
 * Created by Ivan_Bakach on 04.06.2015.
 */
public class ThirdActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        Button button = (Button) findViewById(R.id.bt_npe);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG","it's after run");
//                throw new NullPointerException();
            }
        });
    }
}
