package amtt.epam.com.amtt.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Artsiom_Kaliaha on 25.06.2015.
 */
public class PaintView extends ImageView {

    private static final class DrawnPath {

        private final Path mPath;
        private final Paint mPaint;
        private final PaintMode mPaintMode;

        public DrawnPath(Path path, Paint paint, PaintMode paintMode) {
            mPath = path;
            mPaint = paint;
            mPaintMode = paintMode;
        }

        public Path getPath() {
            return mPath;
        }

        public PaintMode getPaintMode() {
            return mPaintMode;
        }

        public Paint getPaint() {
            return mPaint;
        }

        public void addPath(Path path) {
            mPath.addPath(path);
        }

    }

    private enum PaintMode {

        DRAW,
        ERASE

    }

    public static final int DEFAULT_OPACITY = 255;
    public static final int DEFAULT_BRUSH_THICKNESS = 20;

    private Canvas mCacheCanvas;
    private Bitmap mCacheCanvasBitmap;
    private Path mDrawPath;
    private Paint mPaint;
    private Paint mBitmapPaint;
    private PorterDuffXfermode mClearMode;
    private boolean isEraseMode;
    private int mCurrentOpacity = 255;

    private List<DrawnPath> mDrawnPaths;
    private List<DrawnPath> mUndone;

    public PaintView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setUpDrawingArticles();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        redrawCache();
        canvas.drawBitmap(mCacheCanvasBitmap, 0, 0, mBitmapPaint);
        canvas.drawPath(mDrawPath, mPaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w > 0 && h > 0) {
            mCacheCanvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            mCacheCanvas = new Canvas(mCacheCanvasBitmap);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (isEraseMode) {
                    mDrawnPaths.add(new DrawnPath(new Path(mDrawPath), new Paint(mPaint), PaintMode.ERASE));
                }
                mDrawPath.moveTo(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                mDrawPath.lineTo(x, y);
                mCacheCanvas.drawPath(mDrawPath, mPaint);
                if (isEraseMode) {
                    mDrawnPaths.get(mDrawnPaths.size() - 1).addPath(mDrawPath);
                    mDrawPath.reset();
                    mDrawPath.moveTo(x, y);
                }
                break;
            case MotionEvent.ACTION_UP:
                mCacheCanvas.drawPath(mDrawPath, mPaint);
                if (isEraseMode) {
                    mDrawnPaths.get(mDrawnPaths.size() - 1).addPath(mDrawPath);
                } else {
                    mDrawnPaths.add(new DrawnPath(new Path(mDrawPath), new Paint(mPaint), PaintMode.DRAW));
                }
                mDrawPath.reset();
                break;
            default:
                return false;
        }

        invalidate();
        return true;
    }

    private void setUpDrawingArticles() {
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);
        mDrawPath = new Path();

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mPaint.setStrokeWidth(DEFAULT_BRUSH_THICKNESS);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeJoin(Paint.Join.ROUND);

        mClearMode = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);

        mDrawnPaths = new ArrayList<>();
        mUndone = new ArrayList<>();
    }

    public void setBrushColor(int brushColor) {
        mPaint.setColor(brushColor);
        mPaint.setAlpha(mCurrentOpacity);
    }

    public void setEraseMode(boolean eraseMode) {
        isEraseMode = eraseMode;
        if (eraseMode) {
            mPaint.setXfermode(mClearMode);
        } else {
            mPaint.setXfermode(null);
        }
    }

    public void undo() {
        if (mDrawnPaths.size() != 0) {
            mUndone.add(mDrawnPaths.remove(mDrawnPaths.size() - 1));
            redrawCache();
        }
    }

    public void redo() {
        if (mUndone.size() != 0) {
            mDrawnPaths.add(mUndone.remove(mUndone.size() - 1));
            DrawnPath drawnPath = mDrawnPaths.get(mDrawnPaths.size() - 1);

            if (drawnPath.getPaintMode() == PaintMode.DRAW) {
                mCacheCanvas.drawPath(drawnPath.getPath(), drawnPath.getPaint());
            } else {
                mCacheCanvas.drawPath(drawnPath.getPath(), drawnPath.getPaint());
            }
            invalidate();
        }
    }

    public void setBrushThickness(float thickness) {
        mPaint.setStrokeWidth(thickness);
    }

    public void setBrushOpacity(int opacity) {
        mPaint.setAlpha(opacity);
        mCurrentOpacity = opacity;
    }

    private void redrawCache() {
        mCacheCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        for (DrawnPath drawnPath : mDrawnPaths) {
            if (drawnPath.getPaintMode() == PaintMode.DRAW) {
                mCacheCanvas.drawPath(drawnPath.getPath(), drawnPath.getPaint());
            } else {
                mCacheCanvas.drawPath(drawnPath.getPath(), drawnPath.getPaint());
            }
        }
        invalidate();
    }

}
