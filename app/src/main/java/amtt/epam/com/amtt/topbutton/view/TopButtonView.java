package amtt.epam.com.amtt.topbutton.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.util.UIUtil;

/**
 @author Ivan_Bakach
 @version on 23.03.2015
 */

@SuppressLint("ViewConstructor")
public class TopButtonView extends FrameLayout {

    private static class RotatingDrawable extends LayerDrawable {
        public RotatingDrawable(Drawable drawable) {
            super(new Drawable[]{drawable});
        }

        private float mRotation;

        @SuppressWarnings("UnusedDeclaration")
        public float getRotation() {
            return mRotation;
        }

        @SuppressWarnings("UnusedDeclaration")
        public void setRotation(float rotation) {
            mRotation = rotation;
            invalidateSelf();
        }

        @Override
        public void draw(Canvas canvas) {
            canvas.save();
            canvas.rotate(mRotation, getBounds().centerX(), getBounds().centerY());
            super.draw(canvas);
            canvas.restore();
        }
    }

    private static final int sThreshold = 10;
    private DisplayMetrics mDisplayMetrics;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mLayoutParams;
    private TopButtonBarView mTopButtonBarView;
    private int mCurrentOrientation;
    private float mWidthProportion;
    private float mHeightProportion;
    private int mFirstX;
    private int mFirstY;
    private int mLastX;
    private int mLastY;
    public ImageButton mMainImageButton;
    public boolean isMoving;

    public TopButtonView(Context context, WindowManager.LayoutParams layoutParams) {
        super(context);
        initComponent();
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mDisplayMetrics = getContext().getResources().getDisplayMetrics();
        mCurrentOrientation = getResources().getConfiguration().orientation;
        this.mLayoutParams = layoutParams;
        mWidthProportion = (float) mLayoutParams.x / mDisplayMetrics.widthPixels;
        mHeightProportion = (float) mLayoutParams.y / mDisplayMetrics.heightPixels;
    }

    @SuppressWarnings("unchecked")
    private void initComponent() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.top_button_layout, this, true);
        mMainImageButton = (ImageButton)findViewById(R.id.main_button);
        mMainImageButton.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                mMainImageButton.getViewTreeObserver().removeOnPreDrawListener(this);
                mTopButtonBarView = new TopButtonBarView(getContext(), mMainImageButton.getHeight(), mMainImageButton.getWidth());
                return true;
            }
        });
    }

    private void savePositionAfterScreenRotation() {
        int overWidth;
        int overHeight;
        if (Math.round(mDisplayMetrics.widthPixels * mWidthProportion) + mMainImageButton.getWidth() < mDisplayMetrics.widthPixels) {
            mLayoutParams.x = Math.round(mDisplayMetrics.widthPixels * mWidthProportion);
        } else {
            overWidth = Math.round(mDisplayMetrics.widthPixels * mWidthProportion) + mMainImageButton.getWidth() - mDisplayMetrics.widthPixels;
            mLayoutParams.x = Math.round(mDisplayMetrics.widthPixels * mWidthProportion) - overWidth - UIUtil.getStatusBarHeight();
        }

        if (Math.round(mDisplayMetrics.heightPixels * mHeightProportion) + mMainImageButton.getWidth() < mDisplayMetrics.heightPixels - UIUtil.getStatusBarHeight()) {
            mLayoutParams.y = Math.round(mDisplayMetrics.heightPixels * mHeightProportion);
        } else {
            overHeight = Math.round(mDisplayMetrics.heightPixels * mHeightProportion) + mMainImageButton.getHeight() - mDisplayMetrics.heightPixels;
            mLayoutParams.y = Math.round(mDisplayMetrics.heightPixels * mHeightProportion) - overHeight - UIUtil.getStatusBarHeight();
        }
        mWindowManager.updateViewLayout(this, mLayoutParams);
        mCurrentOrientation = getResources().getConfiguration().orientation;
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
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
                if ((mLayoutParams.x + deltaX) > 0 && (mLayoutParams.x + deltaX) <= (mDisplayMetrics.widthPixels - getWidth())) {
                    mLayoutParams.x += deltaX;
                }
                if ((mLayoutParams.y + deltaY) > 0 && (mLayoutParams.y + deltaY) <= (mDisplayMetrics.heightPixels - getHeight() - UIUtil.getStatusBarHeight())) {
                    mLayoutParams.y += deltaY;
                }
                mWidthProportion = (float) mLayoutParams.x / mDisplayMetrics.widthPixels;
                mHeightProportion = (float) mLayoutParams.y / mDisplayMetrics.heightPixels;
                mTopButtonBarView.move(mLayoutParams.x, mLayoutParams.y);
            }
            mWindowManager.updateViewLayout(this, mLayoutParams);
        }
    }

    private void handleUpAction(MotionEvent event, int totalDeltaX, int totalDeltaY) {
        isMoving = false;

        if (event.getPointerCount() == 1) {

            boolean tap = Math.abs(totalDeltaX) < sThreshold
                    && Math.abs(totalDeltaY) < sThreshold;
            if (tap) {
                if (mTopButtonBarView.getVisibility() == VISIBLE) {
                    mTopButtonBarView.hide();
                    playMainButtonRotateAnimation(300, 180, 0);
                } else {
                    mTopButtonBarView.show(mLayoutParams.x, mLayoutParams.y);
                    playMainButtonRotateAnimation(300, 0, 180);
                }
            }
        }
    }

    private void setBackgroundCompat(View view, Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(drawable);
        } else {
            view.setBackgroundDrawable(drawable);
        }
    }

    private void playMainButtonRotateAnimation(int duration, int fromAngle, int toAngle) {
        AnimatorSet expand = new AnimatorSet().setDuration(duration);
        LayerDrawable layerDrawable = (LayerDrawable) getResources().getDrawable(R.drawable.background_main_button);
        assert layerDrawable != null;
        RotatingDrawable drawable = new RotatingDrawable(layerDrawable.findDrawableByLayerId(R.id.main_button_background));
        ObjectAnimator animator = ObjectAnimator.ofFloat(drawable, "rotation", fromAngle, toAngle);
        animator.start();
        expand.play(animator);
        layerDrawable.setDrawableByLayerId(R.id.main_button_background, drawable);
        setBackgroundCompat(mMainImageButton, layerDrawable);
    }

    public TopButtonBarView getButtonsBar() {
        return mTopButtonBarView;
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation != mCurrentOrientation) {
            savePositionAfterScreenRotation();
            if (mTopButtonBarView.getVisibility() == VISIBLE) {
                mTopButtonBarView.show(mLayoutParams.x, mLayoutParams.y);
            }
        }
    }

}
