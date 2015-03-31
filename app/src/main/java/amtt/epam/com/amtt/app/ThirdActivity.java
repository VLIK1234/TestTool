package amtt.epam.com.amtt.app;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import amtt.epam.com.amtt.R;

/**
 * Created by Ivan_Bakach on 26.03.2015.
 */
public class ThirdActivity extends BaseActivity {

    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_view_layout);
        linearLayout = (LinearLayout) findViewById(R.id.custom_linear);
        int[] buttonDrawableResourceIds = new int[] {
                R.drawable.button_add, R.drawable.button_share, R.drawable.button_auth,
                R.drawable.button_screen,R.drawable.button_bug_rep
        };
        Button[] arrayButton = new Button[5];
        int counter = 0;
        for (Button item : arrayButton) {
            item = new Button(this);
            item.setText(String.valueOf(counter));
            item.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            linearLayout.addView(item);
            counter++;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
//        setContentView(R.layout.activity_third);
    }
}
