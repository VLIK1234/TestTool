package amtt.epam.com.amtt.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.RadioButton;

import amtt.epam.com.amtt.R;

/**
 * Created by Artsiom_Kaliaha on 01.07.2015.
 */
public class PaletteItem extends RadioButton {

    private int mColor;

    public PaletteItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = null;
        try {
            typedArray = context.obtainStyledAttributes(attrs, R.styleable.PaletteItem, 0, 0);
            mColor = typedArray.getColor(R.styleable.PaletteItem_cell_color, Color.BLACK);
        } finally {
            if (typedArray != null) {
                typedArray.recycle();
            }
        }
    }

    public int getColor() {
        return mColor;
    }

}
