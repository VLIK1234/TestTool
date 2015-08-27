package amtt.epam.com.amtt.ui.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.util.UIUtil;

/**
 * @author IvanBakach
 * @version on 25.08.2015
 */
public class DragImageView extends LinearLayout implements View.OnTouchListener{

    private ImageView mDragImage;

    public interface IDrawCallback{
        void onDrawClick(String drawValue, int x, int y, Paint paint);
        void onRemoveClick(View view);
    }

    private String mDrawString = "";
    private Paint mPaintText = new Paint();
    private Bitmap mCacheCanvasBitmap;
    private Canvas mCacheCanvas;
    private WindowManager.LayoutParams mDragImageLayoutParams;
    public WindowManager mWindowManager;
    private DisplayMetrics mDisplayMetrics;
    private IDrawCallback mIDrawCallback;

    private int mFirstX;
    private int mFirstY;
    private int mLastX;
    private int mLastY;
    private static final int sThreshold = 10;
    private boolean isMoving;

    public DragImageView(Context context, String drawStringValue, Paint paintText, IDrawCallback iDrawCallback) {
        super(context);
        mPaintText = paintText;
        mDrawString = drawStringValue;
        mIDrawCallback = iDrawCallback;
        this.setOrientation(HORIZONTAL);
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mDisplayMetrics = getContext().getResources().getDisplayMetrics();
        mDragImageLayoutParams = initMainLayoutParams();
        this.setLayoutParams(mDragImageLayoutParams);

        mDragImage = new ImageView(context);
        UIUtil.setBackgroundCompat(mDragImage, getResources().getDrawable(R.drawable.background_drag_image));
        mDragImage.setAdjustViewBounds(true);
        mDragImage.setScaleType(ImageView.ScaleType.FIT_XY);
        mCacheCanvasBitmap = Bitmap.createBitmap(300, 300, Bitmap.Config.ARGB_8888);
        mCacheCanvas = new Canvas(mCacheCanvasBitmap);
        mCacheCanvas.drawText(mDrawString, 0, UIUtil.getStatusBarHeight(), mPaintText);
        mDragImage.setImageBitmap(mCacheCanvasBitmap);
        addView(mDragImage);

        LinearLayout buttonLayout = new LinearLayout(context);
        buttonLayout.setOrientation(LinearLayout.VERTICAL);
        buttonLayout.setLayoutParams(initButtonLayoutParams());

        ImageButton deleteDragViewButton = new ImageButton(context);
        UIUtil.setBackgroundCompat(deleteDragViewButton, getResources().getDrawable(R.drawable.ic_delete_drag_view));
        deleteDragViewButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mIDrawCallback.onRemoveClick(DragImageView.this);
            }
        });

        ImageButton confirmDragViewButton = new ImageButton(context);
        UIUtil.setBackgroundCompat(confirmDragViewButton, getResources().getDrawable(R.drawable.ic_confirm_draw_drag_view));
        confirmDragViewButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mIDrawCallback.onDrawClick(mDrawString, mDragImageLayoutParams.x, mDragImageLayoutParams.y, mPaintText);
                mIDrawCallback.onRemoveClick(DragImageView.this);
            }
        });

        buttonLayout.addView(deleteDragViewButton);
        buttonLayout.addView(confirmDragViewButton);
        addView(buttonLayout);

        this.setOnTouchListener(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }

    @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    private WindowManager.LayoutParams initMainLayoutParams() {
        int flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FORMAT_CHANGED;

        WindowManager.LayoutParams mainLayout = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT, 0, 0, WindowManager.LayoutParams.TYPE_APPLICATION,
                flags, PixelFormat.TRANSLUCENT);
        mainLayout.gravity = Gravity.TOP | Gravity.START;
        return mainLayout;
    }

    private WindowManager.LayoutParams initButtonLayoutParams() {
        int flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FORMAT_CHANGED;

        WindowManager.LayoutParams mainLayout = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT, 0, 0, WindowManager.LayoutParams.TYPE_APPLICATION,
                flags, PixelFormat.TRANSLUCENT);
        mainLayout.gravity = Gravity.TOP | Gravity.START;
        return mainLayout;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int totalDeltaX = mLastX - mFirstX;
        int totalDeltaY = mLastY - mFirstY;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                handleDownAction(event);
                break;
            case MotionEvent.ACTION_MOVE:
                handleMoveAction(event, totalDeltaX, totalDeltaY);
                break;
            case MotionEvent.ACTION_UP:
                handleUpAction(event, totalDeltaX, totalDeltaY);
                break;
        }
        return true;
    }

    private void handleDownAction(MotionEvent event) {
        mLastX = (int) event.getRawX();
        mLastY = (int) event.getRawY();

        mFirstX = mLastX;
        mFirstY = mLastY;
    }

    private void handleMoveAction(MotionEvent event, int totalDeltaX, int totalDeltaY) {
        int deltaX = (int) event.getRawX() - mLastX;
        int deltaY = (int) event.getRawY() - mLastY;

        mLastX = (int) event.getRawX();
        mLastY = (int) event.getRawY();

        if (isMoving || Math.abs(totalDeltaX) >= sThreshold || Math.abs(totalDeltaY) >= sThreshold) {
            isMoving = true;

            // update the position of the view
            if (event.getPointerCount() == 1) {
                if ((mDragImageLayoutParams.x + deltaX) > 0 && (mDragImageLayoutParams.x + deltaX) <= (mDisplayMetrics.widthPixels - getWidth())) {
                    mDragImageLayoutParams.x += deltaX;
                }
                if ((mDragImageLayoutParams.y + deltaY) > 0 && (mDragImageLayoutParams.y + deltaY) <= (mDisplayMetrics.heightPixels - getHeight() - UIUtil.getStatusBarHeight())) {
                    mDragImageLayoutParams.y += deltaY;
                }
            }
            mWindowManager.updateViewLayout(this, mDragImageLayoutParams);
        }
    }

    private void handleUpAction(MotionEvent event, int totalDeltaX, int totalDeltaY) {
        isMoving = false;

        if (event.getPointerCount() == 1) {

            boolean tap = Math.abs(totalDeltaX) < sThreshold
                    && Math.abs(totalDeltaY) < sThreshold;
            if (tap) {
                Toast.makeText(getContext(), "Just tap!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
