package amtt.epam.com.amtt.app;

import amtt.epam.com.amtt.R;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Ivan_Bakach on 26.03.2015.
 */
public class ThirdActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        TextView textView = (TextView) findViewById(R.id.textview);
        textView.setText("Third");
        Button button = (Button) findViewById(R.id.buttons_second);
        button.setVisibility(View.GONE);
    }

}
