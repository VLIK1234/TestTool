package amtt.epam.com.amtt.ui.views.paintview;

import android.graphics.Paint;

public final class DrawnText extends DrawObject {

    private final String mStringValue;
    private final float mX;
    private final float mY;

    public DrawnText(String stringValue, float x, float y, Paint paint) {
        super(paint);
        mStringValue = stringValue;
        mX = x;
        mY = y;
    }

    public String getStringValue() {
        return mStringValue;
    }

    public float getX() {
        return mX;
    }

    public float getY() {
        return mY;
    }

    public boolean equals(DrawnText drawnText) {
        return drawnText.getStringValue().equals(mStringValue)&&drawnText.getX()==mX;
    }
}