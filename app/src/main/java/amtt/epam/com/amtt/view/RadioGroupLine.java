package amtt.epam.com.amtt.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioGroup;

/**
 * Created by Artyom on 03.07.2015.
 */
public class RadioGroupLine extends RadioGroup {

    public interface OnLineCheckedChangeListener {

        void onCheckedChanged(RadioGroupLine radioGroup, PaletteItem paletteItem);

    }

    private boolean isEnabled = true;
    private int mLastCheckedRadioButton = -1;
    private boolean isGroupChecked;
    private OnLineCheckedChangeListener mListener;

    public RadioGroupLine(Context context) {
        this(context, null);
    }

    public RadioGroupLine(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setOnGroupCheckedListener(OnLineCheckedChangeListener listener) {
        mListener = listener;
    }

    public void restoreCheck() {
        isEnabled = true;
        if (mLastCheckedRadioButton != -1) {
            PaletteItem lastCheckedPaletteItem = (PaletteItem) findViewById(mLastCheckedRadioButton);
            lastCheckedPaletteItem.setChecked(true);
        }
    }

    public boolean isGroupChecked() {
        return isGroupChecked;
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        if (child instanceof PaletteItem) {
            ((PaletteItem) child).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isEnabled) {
                        if (isGroupChecked = isChecked) {
                            mListener.onCheckedChanged(RadioGroupLine.this, (PaletteItem) buttonView);
                        }
                    } else {
                        buttonView.setChecked(false);
                    }
                }
            });
        }
    }

    @Override
    public void clearCheck() {
        isGroupChecked = false;
        mLastCheckedRadioButton = getCheckedRadioButtonId();
        super.clearCheck();
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }
}
