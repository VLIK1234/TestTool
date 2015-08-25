package amtt.epam.com.amtt.ui.views;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import amtt.epam.com.amtt.R;

/**
 * @author Artsiom_Kaliaha
 * @version on 25.06.2015
 */

public class PaintView extends ImageView {

    protected static class DrawObject {
        private final Paint mPaint;

        protected DrawObject(Paint paint) {
            mPaint = paint;
        }

        protected Paint getPaint() {
            return mPaint;
        }
    }

    private static final class DrawnPath extends DrawObject {

        private final Path mPath;
        private final PaintMode mPaintMode;

        public DrawnPath(Path path, Paint paint, PaintMode paintMode) {
            super(paint);
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

    private static final class DrawnText extends DrawObject {

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
    }

    public enum PaintMode {
        DRAW,
        TEXT,
        ERASE
    }

    private static final int OUT_OF_SCREEN_COORDINATE = -999;
    public static final int DEFAULT_OPACITY = 255;
    public static final int DEFAULT_BRUSH_THICKNESS = 20;
    private static final int DEFAULT_ERASER_THICKNESS = 100;

    private Canvas mCacheCanvas;
    private Bitmap mCacheCanvasBitmap;
    private Path mDrawPath;
    private Paint mPaintPath;
    private Paint mEraserPaint;
    private Paint mBitmapPaint;
    private Point mEraserPoint;
    private PorterDuffXfermode mClearMode;
    private boolean isEraseMode;
    private int mCurrentOpacity = DEFAULT_OPACITY;
    private int mLastBrushThickness;
    private OnTouchListener mOnTouchListener;

    private List<DrawObject> mDrawObjects;
    private List<DrawObject> mUndone;

    private Paint mPaintText = new Paint();
    private PaintMode mPaintMode;
    private String mDrawString ="";
    private float xText = 0;
    private float yText = 0;

    public PaintView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setUpDrawingArticles();
        setDrawingCacheEnabled(true);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        redrawCache();
        if (mCacheCanvas != null) {
            canvas.drawBitmap(mCacheCanvasBitmap, 0, 0, mBitmapPaint);
            canvas.drawPath(mDrawPath, mPaintPath);
//            canvas.drawText(mDrawString, xText, yText, mPaintText);

//            String[] stringsArr = {"Some text ", " This arr ", "Else arr "};
//            int i = 10;
//            float[] floatArr;
//            for (String s :stringsArr) {
//                floatArr = new float[s.length()];
//                for (float j: floatArr) {
//                    j = 20;
//                }
//                canvas.drawText(s + xText, xText, yText+(i*=3), mPaintText);
//            }
            if (mPaintMode == PaintMode.ERASE) {
                canvas.drawPoint(mEraserPoint.x, mEraserPoint.y, mEraserPaint);
            }
        }
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
    public boolean onTouchEvent(@NonNull final MotionEvent event) {
        final float x = event.getX();
        final float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                switch (mPaintMode){
                    case ERASE:
                        mDrawObjects.add(new DrawnPath(new Path(mDrawPath), new Paint(mPaintPath), PaintMode.ERASE));
                        mEraserPoint.set((int) x, (int) y);
                        mDrawPath.moveTo(x, y);
                        break;
                    case DRAW:
                        mDrawPath.moveTo(x, y);
                        break;
                    case TEXT:
                        final View view = ((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.dialog_draw_text, null);
                        final EditText editDrawText = (EditText) view.findViewById(R.id.et_draw_text);

                        new AlertDialog.Builder(getContext())
                                .setTitle("Draw text")
                                .setView(view)
                                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mDrawString = editDrawText.getText().toString();
                                        mCacheCanvas.drawText(mDrawString, x, y, new Paint(mPaintText));
                                        mDrawObjects.add(new DrawnText(mDrawString, x, y,  new Paint(mPaintText)));
                                    }
                                })
                                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mDrawString = "";
                                    }
                                })
                                .create()
                                .show();
                        break;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                switch (mPaintMode){
                    case ERASE:
                        mDrawPath.lineTo(x, y);
                        mCacheCanvas.drawPath(mDrawPath, mPaintPath);
                        if (mDrawObjects.get(mDrawObjects.size() - 1) instanceof DrawnPath) {
                            ((DrawnPath) mDrawObjects.get(mDrawObjects.size() - 1)).addPath(mDrawPath);
                            mDrawPath.reset();
                            mDrawPath.moveTo(x, y);
                            mEraserPoint.set((int) x, (int) y);
                        }
                        break;
                    case DRAW:
                        mDrawPath.lineTo(x, y);
                        mCacheCanvas.drawPath(mDrawPath, mPaintPath);
                        break;
                    case TEXT:
                        break;
                }
                break;
            case MotionEvent.ACTION_UP:
                Log.d("TAG", "UP");
                switch (mPaintMode){
                    case ERASE:
                        mCacheCanvas.drawPath(mDrawPath, mPaintPath);
                        if (mDrawObjects.get(mDrawObjects.size() - 1) instanceof DrawnPath) {
                            ((DrawnPath) mDrawObjects.get(mDrawObjects.size() - 1)).addPath(mDrawPath);
                            mEraserPoint.set(OUT_OF_SCREEN_COORDINATE, OUT_OF_SCREEN_COORDINATE);
                        }
                        break;
                    case DRAW:
                        mCacheCanvas.drawPath(mDrawPath, mPaintPath);
                        mDrawObjects.add(new DrawnPath(new Path(mDrawPath), new Paint(mPaintPath), PaintMode.DRAW));
                        mDrawPath.reset();
                        break;
                    case TEXT:
                        break;
                }
                xText = x;
                yText = y;

                break;
            default:
                return false;
        }

        mOnTouchListener.onTouch(this, event);
        invalidate();
        return true;
    }

    @Override
    public void setOnTouchListener(OnTouchListener l) {
        mOnTouchListener = l;
    }

    private void setUpDrawingArticles() {
        mPaintMode = PaintMode.DRAW;

        mBitmapPaint = new Paint(Paint.DITHER_FLAG);
        mDrawPath = new Path();

        mPaintPath = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mPaintPath.setStrokeWidth(mLastBrushThickness = DEFAULT_BRUSH_THICKNESS);
        mPaintPath.setStyle(Paint.Style.STROKE);
        mPaintPath.setStrokeCap(Paint.Cap.ROUND);
        mPaintPath.setStrokeJoin(Paint.Join.ROUND);

        mPaintText.setColor(Color.BLACK);
        mPaintText.setTextSize(mLastBrushThickness = DEFAULT_BRUSH_THICKNESS);

        mEraserPaint = new Paint();
        mEraserPaint.setStrokeWidth(DEFAULT_ERASER_THICKNESS);
        mEraserPaint.setStyle(Paint.Style.STROKE);
        mEraserPaint.setStrokeCap(Paint.Cap.ROUND);
        mEraserPaint.setStrokeJoin(Paint.Join.ROUND);
        mEraserPaint.setColor(getResources().getColor(R.color.primaryTranslucent));

        mEraserPoint = new Point();

        mClearMode = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);

        mDrawObjects = new ArrayList<>();
        mUndone = new ArrayList<>();
    }

    public void setBrushColor(int brushColor) {
        mPaintPath.setColor(brushColor);
        mPaintPath.setAlpha(mCurrentOpacity);
        mPaintText.setColor(brushColor);
    }

    public void undo() {
        if (mDrawObjects.size() != 0) {
            mUndone.add(mDrawObjects.remove(mDrawObjects.size() - 1));
            redrawCache();
        }
    }

    public void redo() {
        if (mUndone.size() != 0) {
            mDrawObjects.add(mUndone.remove(mUndone.size() - 1));
            if (mDrawObjects.get(mDrawObjects.size() - 1) instanceof DrawnPath) {
                DrawnPath drawnPath = (DrawnPath) mDrawObjects.get(mDrawObjects.size() - 1);

                if (drawnPath.getPaintMode() == PaintMode.DRAW) {
                    mCacheCanvas.drawPath(drawnPath.getPath(), drawnPath.getPaint());
                } else {
                    mCacheCanvas.drawPath(drawnPath.getPath(), drawnPath.getPaint());
                }
            }
            invalidate();
        }
    }

    public void setThickness(float thickness) {
        if (isEraseMode) {
            mEraserPaint.setStrokeWidth(thickness);
        }
        mPaintText.setTextSize(thickness);
        mPaintPath.setStrokeWidth(thickness);
    }

    public void setBrushOpacity(int opacity) {
        mPaintPath.setAlpha(opacity);
        mCurrentOpacity = opacity;
    }

    private void redrawCache() {
        if (mCacheCanvas != null) {
            mCacheCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            for (DrawObject drawObject : mDrawObjects) {
                if (drawObject instanceof DrawnPath) {
                    if (((DrawnPath) drawObject).getPaintMode() == PaintMode.DRAW) {
                        mCacheCanvas.drawPath(((DrawnPath) drawObject).getPath(), drawObject.getPaint());
                    } else {
                        mCacheCanvas.drawPath(((DrawnPath) drawObject).getPath(), drawObject.getPaint());
                    }
                } else if(drawObject instanceof DrawnText){
                    DrawnText drawnText = (DrawnText) drawObject;
                    mCacheCanvas.drawText(drawnText.getStringValue(), drawnText.getX(), drawnText.getY(), drawnText.getPaint());
                }
            }
            invalidate();
        }
    }

    public void clear() {
        mDrawObjects.clear();
        invalidate();
    }

    public int getEraserThickness() {
        return (int) mEraserPaint.getStrokeWidth();
    }

    public int getBrushThickness() {
        return (int) mPaintPath.getStrokeWidth();
    }

    public void setPaintMode(PaintMode paintMode){
        mPaintMode = paintMode;
        if (paintMode == PaintMode.ERASE) {
            mPaintPath.setXfermode(mClearMode);
        } else {
            mPaintPath.setXfermode(null);
            mPaintPath.setStrokeWidth(mLastBrushThickness);
        }
    }
}
