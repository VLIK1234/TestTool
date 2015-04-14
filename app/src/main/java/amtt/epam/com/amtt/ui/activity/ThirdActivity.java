package amtt.epam.com.amtt.ui.activity;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;
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
        int[] buttonDrawableResourceIds = new int[]{
                R.drawable.button_add, R.drawable.button_share, R.drawable.button_auth,
                R.drawable.button_screen, R.drawable.button_bug_rep
        };
        ImageView[] arrayButton = new ImageView[5];
        int counter = 0;
        for (ImageView item : arrayButton) {
            item = new ImageView(this);
            item.setImageResource(buttonDrawableResourceIds[counter]);
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
