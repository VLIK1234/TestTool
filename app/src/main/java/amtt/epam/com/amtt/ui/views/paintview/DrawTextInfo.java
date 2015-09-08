package amtt.epam.com.amtt.ui.views.paintview;

import android.graphics.Paint;
import android.graphics.Rect;

/**
 * @author IvanBakach
 * @version on 08.09.2015
 */
public class DrawTextInfo {
    private ViewRectangle mViewRectangle;
    private String mTextValue;
    private int mXPoint;
    private int mYPoint;
    private Paint mPaint;

    public DrawTextInfo(int xPoint, int yPoint, int widthWindow, int heightWindow, String textValue, Paint paint) {
        mXPoint = xPoint;
        mYPoint = yPoint;
        mViewRectangle = new ViewRectangle(xPoint, yPoint, widthWindow, heightWindow);
        mTextValue = textValue;
        mPaint = paint;
    }

    public boolean equals(DrawTextInfo drawTextInfo) {
        return drawTextInfo.getTextValue().equals(mTextValue)&&drawTextInfo.getXPoint()==mXPoint;
    }

    public int getXPoint() {
        return mXPoint;
    }

    public int getYPoint() {
        return mYPoint;
    }

    public ViewRectangle getViewRectangle() {
        return mViewRectangle;
    }

    public String getTextValue() {
        return mTextValue;
    }

    public Paint getPaint() {
        return mPaint;
    }
}
