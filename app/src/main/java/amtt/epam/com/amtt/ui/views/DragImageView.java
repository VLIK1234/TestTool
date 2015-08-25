package amtt.epam.com.amtt.ui.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * @author IvanBakach
 * @version on 25.08.2015
 */
public class DragImageView extends ImageView {

    private String mDrawString = "";
    private Paint mPaintText = new Paint();
    private Bitmap mCacheCanvasBitmap;
    private Paint mBitmapPaint = new Paint(Paint.DITHER_FLAG);
    private Canvas mCacheCanvas;

    public DragImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mCacheCanvas != null) {
            canvas.drawBitmap(mCacheCanvasBitmap, 0, 0, mBitmapPaint);
        }

    }

    @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w > 0 && h > 0) {
            mCacheCanvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        }
    }

    public void drawTextOnBitmap(String drawString, Paint paintText) {
        mPaintText = paintText;
        mDrawString = drawString;
        mCacheCanvas = new Canvas(mCacheCanvasBitmap = Bitmap.createBitmap(300, 300, Bitmap.Config.ARGB_8888));
        mCacheCanvas.drawText(mDrawString, 20, 20, mPaintText);
//        setImageBitmap(mCacheCanvasBitmap);
    }
}
