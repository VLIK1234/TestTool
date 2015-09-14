package amtt.epam.com.amtt.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

/**
 @author Artyom
 @version on 03.07.2015
 */

public class MultilineRadioGroup extends RadioGroup {

    public interface OnEntireGroupCheckedChangeListener {

        void onCheckedChanged(PaletteItem paletteItem);

    }

    private int mLastCheckedGroupIndex;
    private List<RadioGroupLine> mRadioGroups;
    private OnEntireGroupCheckedChangeListener mListener;

    public MultilineRadioGroup(Context context) {
        this(context, null);
    }

    public MultilineRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void addRadioGroup(View child) {
        if (child instanceof RadioGroup) {
            RadioGroupLine radioGroup = (RadioGroupLine) child;
            radioGroup.setOnGroupCheckedListener(new RadioGroupLine.OnLineCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroupLine radioGroup, PaletteItem paletteItem) {
                    if (mListener != null) {
                        mListener.onCheckedChanged(paletteItem);
                        for (RadioGroupLine group : mRadioGroups) {
                            if (group != radioGroup) {
                                group.setEnabled(true);
                                group.clearCheck();
                            }
                        }
                    }
                }
            });
            if (getChildCount() == 1) {
                ((PaletteItem) ((RadioGroupLine) child).getChildAt(0)).setChecked(true);
            }
            if (mRadioGroups == null) {
                mRadioGroups = new ArrayList<>();
            }
            mRadioGroups.add(radioGroup);
        }
    }

    public void setOnEntireGroupCheckedListener(OnEntireGroupCheckedChangeListener listener) {
        mListener = listener;
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        addRadioGroup(child);
    }

    public PaletteItem getChekedItem() {
        for (RadioGroupLine line: mRadioGroups) {
            for (int i = 0; i < line.getChildCount(); i++) {
                if (((PaletteItem) line.getChildAt(i)).isChecked()) {
                    return ((PaletteItem) line.getChildAt(i));
                }

            }
        }
        return null;
    }

}


