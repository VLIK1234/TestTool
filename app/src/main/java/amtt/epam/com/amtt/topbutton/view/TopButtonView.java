package amtt.epam.com.amtt.topbutton.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
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

    private WindowManager windowManager;
    private WindowManager.LayoutParams layoutParams;
    public ImageButton mainButton;
    private DisplayMetrics metrics;
    private int currentOrientation;
    private float widthProportion;
    private float heightProportion;

    private int firstX;
    private int firstY;
    private int lastX;
    private int lastY;
    public boolean moving;
    private static final int threshold = 10;

    private TopButtonBarView mButtonsBar;

    public TopButtonView(Context context, WindowManager.LayoutParams layoutParams) {
        super(context);
        initComponent();
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        metrics = getContext().getResources().getDisplayMetrics();
        currentOrientation = getResources().getConfiguration().orientation;
        this.layoutParams = layoutParams;
        widthProportion = (float) layoutParams.x / metrics.widthPixels;
        heightProportion = (float) layoutParams.y / metrics.heightPixels;
    }

    @SuppressWarnings("unchecked")
    private void initComponent() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.top_button_layout, this, true);
        mainButton = (ImageButton)findViewById(R.id.main_button);
        mainButton.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                mainButton.getViewTreeObserver().removeOnPreDrawListener(this);
                mButtonsBar = new TopButtonBarView(getContext(), mainButton.getHeight(), mainButton.getWidth());
                return true;
            }
        });
    }

    private void savePositionAfterScreenRotation() {
        int overWidth;
        int overHeight;
        if (Math.round(metrics.widthPixels * widthProportion) + mainButton.getWidth() < metrics.widthPixels) {
            layoutParams.x = Math.round(metrics.widthPixels * widthProportion);
        } else {
            overWidth = Math.round(metrics.widthPixels * widthProportion) + mainButton.getWidth() - metrics.widthPixels;
            layoutParams.x = Math.round(metrics.widthPixels * widthProportion) - overWidth - UIUtil.getStatusBarHeight();
        }

        if (Math.round(metrics.heightPixels * heightProportion) + mainButton.getWidth() < metrics.heightPixels - UIUtil.getStatusBarHeight()) {
            layoutParams.y = Math.round(metrics.heightPixels * heightProportion);
        } else {
            overHeight = Math.round(metrics.heightPixels * heightProportion) + mainButton.getHeight() - metrics.heightPixels;
            layoutParams.y = Math.round(metrics.heightPixels * heightProportion) - overHeight - UIUtil.getStatusBarHeight();
        }
        windowManager.updateViewLayout(this, layoutParams);
        currentOrientation = getResources().getConfiguration().orientation;
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        int totalDeltaX = lastX - firstX;
        int totalDeltaY = lastY - firstY;

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
        lastX = (int) event.getRawX();
        lastY = (int) event.getRawY();

        firstX = lastX;
        firstY = lastY;
    }

    private void handleMoveAction(MotionEvent event, int totalDeltaX, int totalDeltaY) {
        int deltaX = (int) event.getRawX() - lastX;
        int deltaY = (int) event.getRawY() - lastY;

        lastX = (int) event.getRawX();
        lastY = (int) event.getRawY();

        if (moving || Math.abs(totalDeltaX) >= threshold || Math.abs(totalDeltaY) >= threshold) {
            moving = true;

            // update the position of the view
            if (event.getPointerCount() == 1) {
                if ((layoutParams.x + deltaX) > 0 && (layoutParams.x + deltaX) <= (metrics.widthPixels - getWidth())) {
                    layoutParams.x += deltaX;
                }
                if ((layoutParams.y + deltaY) > 0 && (layoutParams.y + deltaY) <= (metrics.heightPixels - getHeight() - UIUtil.getStatusBarHeight())) {
                    layoutParams.y += deltaY;
                }
                widthProportion = (float) layoutParams.x / metrics.widthPixels;
                heightProportion = (float) layoutParams.y / metrics.heightPixels;
                mButtonsBar.move(layoutParams.x, layoutParams.y);
            }
            windowManager.updateViewLayout(this, layoutParams);
        }
    }

    private void handleUpAction(MotionEvent event, int totalDeltaX, int totalDeltaY) {
        moving = false;

        if (event.getPointerCount() == 1) {

            boolean tap = Math.abs(totalDeltaX) < threshold
                    && Math.abs(totalDeltaY) < threshold;
            if (tap) {
                if (mButtonsBar.getVisibility() == VISIBLE) {
                    mButtonsBar.hide();
                    playMainButtonRotateAnimation(300, 180, 0);
                } else {
                    mButtonsBar.show(layoutParams.x, layoutParams.y);
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
        setBackgroundCompat(mainButton, layerDrawable);
    }

    public TopButtonBarView getButtonsBar() {
        return mButtonsBar;
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation != currentOrientation) {
            savePositionAfterScreenRotation();
            if (mButtonsBar.getVisibility() == VISIBLE) {
                mButtonsBar.show(layoutParams.x, layoutParams.y);
            }
        }
    }

}
