package amtt.epam.com.amtt.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import amtt.epam.com.amtt.R;

/**
 * Created by Ivan_Bakach on 19.03.2015.
 */
public class SecondActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
    }

    public void onClickThird(View view) {
        Intent intent = new Intent(this, ThirdActivity.class);
        startActivity(intent);
    }
}
