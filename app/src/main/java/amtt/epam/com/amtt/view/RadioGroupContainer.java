package amtt.epam.com.amtt.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Artyom on 03.07.2015.
 */
public class RadioGroupContainer extends LinearLayout {

    private enum RadioGroupCheckedState {

        CHECKED,
        UNCHECKED

    }

    private List<RadioGroupItem> mRadioGroups;

    public RadioGroupContainer(Context context) {
        this(context, null);
    }

    public RadioGroupContainer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RadioGroupContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        addRadioGroup(child);
    }

    private void addRadioGroup(View child) {
        if (child instanceof RadioGroup) {
            if (mRadioGroups == null) {
                mRadioGroups = new ArrayList<>();
            }
            RadioGroupItem radioGroup = (RadioGroupItem) child;
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    RadioGroupItem radioGroupItem = (RadioGroupItem) group;
                    if (radioGroupItem.isChecked()) {
                        radioGroupItem.setChecked(true);
                    }
                    for (RadioGroupItem item : mRadioGroups) {
                        if (item != group) {
                            item.setChecked(false);
                        }
                    }
                }
            });
            mRadioGroups.add(radioGroup);
        }
    }

}


