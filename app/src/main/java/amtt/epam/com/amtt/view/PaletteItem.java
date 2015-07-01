package amtt.epam.com.amtt.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.ImageButton;

import amtt.epam.com.amtt.R;

/**
 * Created by Artsiom_Kaliaha on 01.07.2015.
 */
public class PaletteItem extends ImageButton {

    private int mBackgroundSelectedId;
    private int mBackgroundUnselectedId;
    private int mColor;
    private boolean isItemSelected;

    public PaletteItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = null;
        try {
            typedArray = context.obtainStyledAttributes(attrs, R.styleable.PaletteItem, 0, 0);
            mBackgroundSelectedId = typedArray.getResourceId(R.styleable.PaletteItem_background_selected, -1);
            mBackgroundUnselectedId = typedArray.getResourceId(R.styleable.PaletteItem_background_unselected, -1);
            mColor = typedArray.getColor(R.styleable.PaletteItem_item_color, Color.BLACK);
        } finally {
            if (typedArray != null) {
                typedArray.recycle();
            }
        }
        setBackgroundColor(Color.TRANSPARENT);
        setImageResource(mBackgroundUnselectedId);
    }

    public void setSelected() {
        setImageResource(mBackgroundSelectedId);
        setBackgroundColor(mColor);
        isItemSelected = true;
    }

    public void setUnselected() {
        setImageResource(mBackgroundUnselectedId);
        setBackgroundColor(Color.TRANSPARENT);
        isItemSelected = false;
    }

    public int getColor() {
        return mColor;
    }

    public boolean isItemSelected() {
        return isItemSelected;
    }

}
