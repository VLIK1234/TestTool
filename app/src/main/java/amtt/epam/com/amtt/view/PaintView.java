package amtt.epam.com.amtt.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Artsiom_Kaliaha on 25.06.2015.
 */
public class PaintView extends ImageView {

    //TODO add field with PAINT COLOR
    private static final class DrawnPath {

        private final Path mPath;
        private final PaintMode mPaintMode;

        public DrawnPath(Path path, PaintMode paintMode) {
            mPath = path;
            mPaintMode = paintMode;
        }

        public Path getPath() {
            return mPath;
        }

        public PaintMode getPaintMode() {
            return mPaintMode;
        }

        public void addPath(Path path) {
            mPath.addPath(path);
        }

    }

    private enum PaintMode {

        DRAW,
        ERASE

    }

    private Canvas mCacheCanvas;
    private Bitmap mCacheCanvasBitmap;
    private Path mDrawPath;
    private Paint mPaint;
    private Paint mBitmapPaint;
    private PorterDuffXfermode mClearMode;
    private boolean isEraseMode;

    private List<DrawnPath> mDrawnPaths;
    private List<DrawnPath> mUndone;

    public PaintView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setUpDrawingArticles();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(mDrawPath, mPaint);
        canvas.drawBitmap(mCacheCanvasBitmap, 0, 0, mBitmapPaint);
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
                    mDrawnPaths.add(new DrawnPath(new Path(mDrawPath), PaintMode.ERASE));
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
                    mDrawnPaths.add(new DrawnPath(new Path(mDrawPath), PaintMode.DRAW));
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
        mDrawPath = new Path();

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mPaint.setStrokeWidth(20);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeJoin(Paint.Join.ROUND);

        mBitmapPaint = new Paint(Paint.DITHER_FLAG);

        mClearMode = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);

        mDrawnPaths = new ArrayList<>();
        mUndone = new ArrayList<>();
    }

    public void setBrushColor(int brushColor) {
        mPaint.setColor(brushColor);
    }

    public void setEraseMode(boolean eraseMode) {
        isEraseMode = eraseMode;
        if (eraseMode) {
            mPaint.setXfermode(mClearMode);
        } else {
            mPaint.setXfermode(null);
        }
    }

    public boolean isEraseMode() {
        return isEraseMode;
    }

    public void undo() {
        if (mDrawnPaths.size() != 0) {
            for (DrawnPath drawnPath : mDrawnPaths) {
                if (drawnPath.getPaintMode() == PaintMode.DRAW) {
                    //TODO code below
                } else {
                    //TODO draw erased path
                }
            }

            //code below
            mUndone.add(mDrawnPaths.remove(mDrawnPaths.size() - 1));
            mCacheCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

            Xfermode currentXfermode = mPaint.getXfermode();
            for (DrawnPath drawnPath : mDrawnPaths) {
                if (drawnPath.getPaintMode() == PaintMode.DRAW) {
                    mCacheCanvas.drawPath(drawnPath.getPath(), mPaint);
                } else {
                    mPaint.setXfermode(mClearMode);
                    mCacheCanvas.drawPath(drawnPath.getPath(), mPaint);
                    mPaint.setXfermode(currentXfermode);
                }
            }
            mPaint.setXfermode(currentXfermode);
            invalidate();
        }
    }

    public void redo() {
        if (mUndone.size() != 0) {
            mDrawnPaths.add(mUndone.remove(mUndone.size() - 1));

            Xfermode currentXfermode = mPaint.getXfermode();
            DrawnPath drawnPath = mDrawnPaths.get(mDrawnPaths.size() - 1);
            if (drawnPath.getPaintMode() == PaintMode.DRAW) {
                mCacheCanvas.drawPath(drawnPath.getPath(), mPaint);
            } else {
                mPaint.setXfermode(mClearMode);
                mCacheCanvas.drawPath(drawnPath.getPath(), mPaint);
                mPaint.setXfermode(currentXfermode);
            }
            invalidate();
        }
    }

}
