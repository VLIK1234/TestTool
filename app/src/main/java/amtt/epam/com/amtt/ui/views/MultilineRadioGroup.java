package amtt.epam.com.amtt.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Artyom on 03.07.2015.
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

    private void addRadioGroup(View child, int index) {
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

    public void restoreCheck() {
        for (int i = 0; i < mRadioGroups.size(); i++) {
            if (i == mLastCheckedGroupIndex) {
                mRadioGroups.get(mLastCheckedGroupIndex).restoreCheck();
            } else {
                mRadioGroups.get(i).setEnabled(true);
            }
        }
    }

    public void setOnEntireGroupCheckedListener(OnEntireGroupCheckedChangeListener listener) {
        mListener = listener;
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        addRadioGroup(child, index);
    }

    @Override
    public void clearCheck() {
        for (int i = 0; i < mRadioGroups.size(); i++) {
            RadioGroupLine radioGroup = mRadioGroups.get(i);
            if (radioGroup.isGroupChecked()) {
                mLastCheckedGroupIndex = i;
            }
            radioGroup.clearCheck();
            radioGroup.setEnabled(false);
        }
    }

}


