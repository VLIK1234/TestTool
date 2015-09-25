package amtt.epam.com.amtt.ui.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.support.annotation.IntDef;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.util.UIUtil;

/**
 * @author IvanBakach
 * @version on 25.08.2015
 */
public class DragTextView extends LinearLayout implements View.OnTouchListener{

    private boolean mIsRealSize;

    public interface IDrawCallback{
        void onDrawTextClick(String drawStringValue, int x, int y, int width, int height, int rightY, Paint paint);
        void onRemoveClick(View view);
        void onDrawTextClick(String drawStringValue, Paint paintText);
    }

    @IntDef({VISIBLE, INVISIBLE, GONE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Visibility {}

    private TextView mDragText;
    private String mDrawString = "";
    private Paint mPaintText = new Paint();
    private WindowManager.LayoutParams mDragViewLayoutParams;
    private WindowManager mWindowManager;
    private DisplayMetrics mDisplayMetrics;
    private IDrawCallback mIDrawCallback;

    private int mFirstX;
    private int mFirstY;
    private int mLastX;
    private int mLastY;
    private static final int sThreshold = 10;
    private boolean isMoving;
    private LinearLayout mLeftButtonLayout;
    private LinearLayout mRightButtonLayout;
    private LinearLayout mUnderButtonLayout;

    private DragTextView(Context context){
        super(context);
    }

    private DragTextView(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
    }

    public DragTextView(Context context, String drawStringValue, Paint paintText, int x, int y, IDrawCallback iDrawCallback) {
        super(context);
        mIDrawCallback = iDrawCallback;
        this.setOrientation(HORIZONTAL);
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mDisplayMetrics = getContext().getResources().getDisplayMetrics();
        mDragViewLayoutParams = initMainLayoutParams(x, y);
        this.setLayoutParams(mDragViewLayoutParams);
        //set side buttons, before get real width and height view after draw, like
        //______________
        //|Right |Left  |
        //|      |      |
        //|      |      |
        //|______|______|
        //|Up         Up|
        //|_____________|
        if (x < (mDisplayMetrics.widthPixels/2)&& y < (mDisplayMetrics.heightPixels*2/3)) {
            mLeftButtonLayout = initDragButtonsLayout(GONE);
            mRightButtonLayout = initDragButtonsLayout(VISIBLE);
            mUnderButtonLayout = initDragButtonsLayout(GONE, HORIZONTAL);
        } else if (y > (mDisplayMetrics.heightPixels * 2/3)) {
            mLeftButtonLayout = initDragButtonsLayout(GONE);
            mRightButtonLayout = initDragButtonsLayout(GONE);
            mUnderButtonLayout = initDragButtonsLayout(VISIBLE, HORIZONTAL);
        } else {
            mLeftButtonLayout = initDragButtonsLayout(VISIBLE);
            mRightButtonLayout = initDragButtonsLayout(GONE);
            mUnderButtonLayout = initDragButtonsLayout(GONE, HORIZONTAL);
        }
        addView(mLeftButtonLayout);

        mDragText = new TextView(context);
        UIUtil.setBackgroundCompat(mDragText, getResources().getDrawable(R.drawable.background_drag_view));
        setDragText(drawStringValue, paintText);
        LinearLayout verticalLayout = new LinearLayout(getContext());
        verticalLayout.setOrientation(VERTICAL);
        verticalLayout.addView(mUnderButtonLayout);
        verticalLayout.addView(mDragText);
        addView(verticalLayout);

        addView(mRightButtonLayout);

        this.setOnTouchListener(this);
        this.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if (!mIsRealSize && getWidth()!=0 && getHeight()!=0) {
                    mIsRealSize =true;
                    int width = getWidth();
                    int height = getHeight();
                    if (mDragViewLayoutParams.x + width > mDisplayMetrics.widthPixels) {
                        mDragViewLayoutParams.x -= ((mDragViewLayoutParams.x + width)- mDisplayMetrics.widthPixels);
                    }
                    if ((mDragViewLayoutParams.y + height) > (mDisplayMetrics.heightPixels - UIUtil.getStatusBarHeight())) {
                        mDragViewLayoutParams.y -= ((mDragViewLayoutParams.y + height) - mDisplayMetrics.heightPixels
                                - UIUtil.getStatusBarHeight()+UIUtil.getSoftbuttonsbarHeight(mWindowManager));
                    }
                    mWindowManager.updateViewLayout(DragTextView.this, mDragViewLayoutParams);
                }
                return true;
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }

    @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    private WindowManager.LayoutParams initMainLayoutParams(int x, int y) {
        int flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FORMAT_CHANGED;

        WindowManager.LayoutParams mainLayout = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT, x, y, WindowManager.LayoutParams.TYPE_APPLICATION,
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
                if ((mDragViewLayoutParams.x + deltaX) > 0 && (mDragViewLayoutParams.x + deltaX) < (mDisplayMetrics.widthPixels - getWidth())) {
                    mDragViewLayoutParams.x += deltaX;
                }
                if ((mDragViewLayoutParams.y + deltaY) > 0 && (mDragViewLayoutParams.y + deltaY) < (mDisplayMetrics.heightPixels - getHeight() - UIUtil.getStatusBarHeight())) {
                    mDragViewLayoutParams.y += deltaY;
                }
                int valueToChangePosition = 50;
                if (!((mDragViewLayoutParams.y + deltaY)+valueToChangePosition < (mDisplayMetrics.heightPixels - getHeight() - UIUtil.getStatusBarHeight()))) {
                    mLeftButtonLayout.setVisibility(GONE);
                    mRightButtonLayout.setVisibility(GONE);
                    mUnderButtonLayout.setVisibility(VISIBLE);
                } else if((mDragViewLayoutParams.x + deltaX)+ valueToChangePosition < (mDisplayMetrics.widthPixels - getWidth())){
                    mLeftButtonLayout.setVisibility(GONE);
                    mRightButtonLayout.setVisibility(VISIBLE);
                    mUnderButtonLayout.setVisibility(GONE);
                } else {
                    mLeftButtonLayout.setVisibility(VISIBLE);
                    mRightButtonLayout.setVisibility(GONE);
                    mUnderButtonLayout.setVisibility(GONE);
                    if (!((mDragViewLayoutParams.x + deltaX) < (mDisplayMetrics.widthPixels - getWidth()))) {
                        mDragViewLayoutParams.x -= mDragViewLayoutParams.x + getWidth() - mDisplayMetrics.widthPixels;
                    }
                }
            }
            mWindowManager.updateViewLayout(this, mDragViewLayoutParams);
        }
    }

    private void handleUpAction(MotionEvent event, int totalDeltaX, int totalDeltaY) {
        isMoving = false;

        if (event.getPointerCount() == 1) {

            boolean tap = Math.abs(totalDeltaX) < sThreshold
                    && Math.abs(totalDeltaY) < sThreshold;
            if (tap) {
                mIDrawCallback.onDrawTextClick(mDrawString, mPaintText);
            }
        }
    }

    public void setDragText(String drawStringValue, Paint paintText){
        mPaintText = paintText;
        mDrawString = drawStringValue;
        mDragText.setIncludeFontPadding(false);
        mDragText.setText(drawStringValue);
        mDragText.setTextSize(TypedValue.COMPLEX_UNIT_FRACTION, paintText.getTextSize());
        mDragText.setTextColor(paintText.getColor());
    }

    public LinearLayout initDragButtonsLayout(@Visibility int visibility){
        return initDragButtonsLayout(visibility, VERTICAL);
    }

    public LinearLayout initDragButtonsLayout(@Visibility int visibility, @LinearLayoutCompat.OrientationMode int orientation){
        final LinearLayout buttonLayout = new LinearLayout(getContext());
        buttonLayout.setOrientation(orientation);
        buttonLayout.setVisibility(visibility);

        ImageButton deleteDragViewButton = new ImageButton(getContext());
        UIUtil.setBackgroundCompat(deleteDragViewButton, getResources().getDrawable(R.drawable.ic_delete_drag_view));
        deleteDragViewButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mIDrawCallback.onRemoveClick(DragTextView.this);
            }
        });

        ImageButton confirmDragViewButton = new ImageButton(getContext());
        UIUtil.setBackgroundCompat(confirmDragViewButton, getResources().getDrawable(R.drawable.ic_confirm_draw_drag_view));
        confirmDragViewButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int yRightIndent = (int)(mPaintText.getTextSize()-mPaintText.getTextSize()/10);
                int leftXIndent = mLeftButtonLayout.getVisibility() == VISIBLE ? mLeftButtonLayout.getWidth():0;
                mIDrawCallback.onDrawTextClick(mDrawString, mDragViewLayoutParams.x + leftXIndent, mDragViewLayoutParams.y + (int) mDragText.getY() + yRightIndent, mDragText.getWidth(), mDragText.getHeight(), mDragViewLayoutParams.y, mPaintText);
                mIDrawCallback.onRemoveClick(DragTextView.this);
            }
        });

        int size = (int)getResources().getDimension(R.dimen.drag_view_button_size);
        LinearLayout.LayoutParams params = new LayoutParams(size, size);

        deleteDragViewButton.setLayoutParams(params);
        confirmDragViewButton.setLayoutParams(params);

        buttonLayout.addView(deleteDragViewButton);
        buttonLayout.addView(confirmDragViewButton);
        return buttonLayout;
    }


}
