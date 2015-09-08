package amtt.epam.com.amtt.ui.views.paintview;

import android.graphics.Paint;

/**
 * @author IvanBakach
 * @version on 08.09.2015
 */
class DrawObject {
    private final Paint mPaint;

    protected DrawObject(Paint paint) {
        mPaint = paint;
    }

    protected Paint getPaint() {
        return mPaint;
    }
}