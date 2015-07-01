package amtt.epam.com.amtt.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Region;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * Created by Artsiom_Kaliaha on 25.06.2015.
 */
public class PaintView extends ImageView {

    private Canvas mDrawCanvas;
    private Bitmap mCanvasBitmap;
    private Path mDrawPath;
    private Paint mDrawPaint;
    private Paint mBitmapPaint;
    private boolean isEraseMode;

    public PaintView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDrawingCacheEnabled(true);
        setUpDrawingArticles();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //TODO canvas.save / restoreToCount
        super.onDraw(canvas);
        canvas.drawPath(mDrawPath, mDrawPaint);
        canvas.clipPath(mDrawPath, Region.Op.UNION);
        canvas.drawBitmap(mCanvasBitmap, 0, 0, mBitmapPaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w > 0 && h > 0) {
            mCanvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            mDrawCanvas = new Canvas(mCanvasBitmap);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDrawPath.moveTo(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                mDrawPath.lineTo(x, y);
                break;
            case MotionEvent.ACTION_UP:
                mDrawCanvas.drawPath(mDrawPath, mDrawPaint);
                mDrawPath.reset();
                break;
            default:
                return false;
        }

        invalidate();
        return true;
    }

    private void setUpDrawingArticles() {
        mDrawPath = new Path();

        mDrawPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mDrawPaint.setStrokeWidth(20);
        mDrawPaint.setStyle(Paint.Style.STROKE);
        mDrawPaint.setStrokeCap(Paint.Cap.ROUND);
        mDrawPaint.setStrokeJoin(Paint.Join.ROUND);

        mBitmapPaint = new Paint(Paint.DITHER_FLAG);
    }

    public void setBrushColor(int brushColor) {
        mDrawPaint.setColor(brushColor);
    }

    public void setEraseMode(boolean eraseMode) {
        isEraseMode = eraseMode;
        if (eraseMode) {
            mDrawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        } else {
            mDrawPaint.setXfermode(null);
        }
    }

    public boolean isEraseMode() {
        return isEraseMode;
    }

}
