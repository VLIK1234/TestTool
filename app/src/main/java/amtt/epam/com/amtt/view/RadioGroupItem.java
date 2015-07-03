package amtt.epam.com.amtt.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Artyom on 03.07.2015.
 */
public class RadioGroupItem extends RadioGroup {

    public interface OnCheckedChangeListener {

        void onCheckedChanged();

    }

    private boolean isChecked;

    public RadioGroupItem(Context context) {
        this(context, null);
    }

    public RadioGroupItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        //TODO foreach added PALETTE ITEM set onclick listener
        //when PALETTE ITEM clicked report about it to the RadioGroupItem that holds this button
        //RadioGroupItem set this button checked and report RadioGroupContainer to set all the RadioGroupItem to unchecked
        //=> all the PaletteItems in this RadioGroupItemS must be unchecked
    }
}
