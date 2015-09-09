package amtt.epam.com.amtt.ui.views.paintview;

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

    public Paint getPaintText() {
        return mPaintText;
    }

    private Paint mPaintText = new Paint();
    private PaintMode mPaintMode;
    private String mDrawString ="";
    private ITextDialogButtonClick mITextDialogButtonClick;

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
                        boolean isExistedText = false;
                        for (DrawObject object: mDrawObjects) {
                            if (object instanceof DrawnText) {
                                if (((DrawnText)object).getDragViewRectangle().isIncludeInRegion((int) x, (int) y)) {
                                    isExistedText = true;
                                    undo((DrawnText)object);
                                    break;
                                } else {
                                    isExistedText = false;
                                }
                            }
                        }
                        if (!isExistedText) {
                            new AlertDialog.Builder(getContext())
                                    .setTitle(getContext().getString(R.string.label_title_draw_text_dialog))
                                    .setView(view)
                                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            mDrawString = editDrawText.getText().toString();
                                            mITextDialogButtonClick.CreateDragViewCallback(mDrawString, new Paint(mPaintText), 0, 0);

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

    public void undo(DrawObject compareObject) {
        if (mDrawObjects.size() != 0) {
            for (int i = 0; i < mDrawObjects.size(); i++) {
                if (compareObject instanceof DrawnPath) {
                    if (mDrawObjects.get(i) instanceof DrawnPath) {
                        if ((mDrawObjects.get(i)).equals(compareObject)) {
                            mUndone.add(mDrawObjects.remove(i));
                            redrawCache();
                        }
                    }
                }else if (compareObject instanceof DrawnText) {
                    if (mDrawObjects.get(i) instanceof DrawnText) {
                        if ((mDrawObjects.get(i)).equals(compareObject)) {
                            mITextDialogButtonClick.CreateDragViewCallback(((DrawnText) mDrawObjects.get(i)).getStringValue(),
                                    mDrawObjects.get(i).getPaint(), ((DrawnText) mDrawObjects.get(i)).getX(), ((DrawnText) mDrawObjects.get(i)).getYForDragView());
                            mUndone.add(mDrawObjects.remove(i));
                            redrawCache();
                        }
                    }
                }
            }
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

    public void setITextDialogButtonClick(ITextDialogButtonClick ITextDialogButtonClick) {
        mITextDialogButtonClick = ITextDialogButtonClick;
    }

    public void drawText(String drawStringValue, int x, int y, int width, int height, int rightY, Paint paintText) {
        mCacheCanvas.drawText(drawStringValue, x, y, new Paint(paintText));
        mDrawObjects.add(new DrawnText(drawStringValue, x, y, rightY, width, height, new Paint(paintText)));
    }
}
