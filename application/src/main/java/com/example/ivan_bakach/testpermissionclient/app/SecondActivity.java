package com.example.ivan_bakach.testpermissionclient.app;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.ivan_bakach.testpermissionclient.R;

/**
 * Created by Ivan_Bakach on 04.06.2015.
 */
public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activtiy_second);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        Button mRuntest = (Button) findViewById(R.id.bt_runtest);
        mRuntest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(), "RuntimeException after 10 second", Toast.LENGTH_LONG).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        throw new RuntimeException("test exception");
                    }
                }, 10000l);
            }
        });
    }
}
