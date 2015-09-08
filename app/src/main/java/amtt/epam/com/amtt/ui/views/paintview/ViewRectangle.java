package amtt.epam.com.amtt.ui.views.paintview;

import android.graphics.Point;

/**
 * @author IvanBakach
 * @version on 08.09.2015
 */
public class ViewRectangle {
    private Point mLeftTop;
    private Point mRightBottom;

    public ViewRectangle(int x, int y, int width, int height) {
        mLeftTop = new Point(x, y);
        mRightBottom = new Point(x + width, y + height);
    }

    public boolean isIncludeInRegion(int x, int y) {
        return x>=mLeftTop.x && y>=mLeftTop.y && x<=mRightBottom.x && y<=mRightBottom.y;
    }
}
