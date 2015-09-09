package amtt.epam.com.amtt.ui.views.paintview;

import android.graphics.Paint;

public final class DrawnText extends DrawObject {

    private final String mStringValue;
    private final int mX;
    private final int mY;
    private final int mYForDragView;
    private DragViewRectangle mDragViewRectangle;

    public DrawnText(String stringValue, int x, int y, int yForDragView, int widthDragView, int heightDragView, Paint paint) {
        super(paint);
        mStringValue = stringValue;
        mX = x;
        mY = y;
        mYForDragView = yForDragView;
        mDragViewRectangle = new DragViewRectangle(x, mYForDragView, widthDragView, heightDragView);
    }

    public int getYForDragView() {
        return mYForDragView;
    }

    public DragViewRectangle getDragViewRectangle() {
        return mDragViewRectangle;
    }

    public String getStringValue() {
        return mStringValue;
    }

    public int getX() {
        return mX;
    }

    public int getY() {
        return mY;
    }

    @Override
    protected boolean equals(DrawObject drawObject) {
        return drawObject instanceof DrawnText && ((DrawnText) drawObject).getStringValue().equals(mStringValue)
                && ((DrawnText) drawObject).getX() == mX && ((DrawnText) drawObject).getYForDragView() == mYForDragView;
    }
}