package amtt.epam.com.amtt.view;

import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import amtt.epam.com.amtt.R;

/**
 * Created by Ivan_Bakach on 23.03.2015.
 */
public class TopButtonView extends FrameLayout {
    private final static String LOG_TAG = "TAG";

    private WindowManager windowManager;
    private WindowManager.LayoutParams layoutParams;
    private ViewGroup body;
    private ImageView imageView;
    private DisplayMetrics metrics;
    private Display display;
    private int orientation;
    private float widthProportion;
    private float heightProportion;
    private RelativeLayout.LayoutParams reParams;

    public TopButtonView(Context context) {
        super(context, null);
    }

    public TopButtonView(Context context, WindowManager.LayoutParams layoutParams) {
        super(context);
        initComponent();

        ((LinearLayout) body).setOrientation(LinearLayout.VERTICAL);

        this.windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        this.metrics = getContext().getResources().getDisplayMetrics();
        orientation = getResources().getConfiguration().orientation;
        display = windowManager.getDefaultDisplay();
        this.layoutParams = layoutParams;
        widthProportion = (float) layoutParams.x / metrics.widthPixels;
        heightProportion = (float) layoutParams.y / metrics.heightPixels;
    }

    private void initComponent() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.top_button_layout, this, true);
        body = (ViewGroup) findViewById(R.id.body);
        imageView = (ImageView) findViewById(R.id.plus_button);
        imageView.setImageResource(R.drawable.ic_top_button);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        Log.d(LOG_TAG, "Show body:" + body.getWidth() + " " + body.getHeight());
//        checkFreeSpace(layoutParams.x,layoutParams.y, getWidth(), getHeight());
        if (getResources().getConfiguration().orientation != orientation) {
            changeProportinalPosition();
            body.setVisibility(GONE);
        }
    }

    private void checkFreeSpace(int x, int y, int bodyWeight, int bodyHeight) {
        Log.d(LOG_TAG, "Init:" + body.getWidth() + " " + body.getHeight() + " " + bodyWeight + " " + bodyHeight);
        reParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            ((LinearLayout) body).setOrientation(LinearLayout.HORIZONTAL);
            reParams.addRule(RelativeLayout.RIGHT_OF, imageView.getId());
            body.setLayoutParams(reParams);
            if (x < metrics.widthPixels / 2) {
                Log.d(LOG_TAG, "Right");
            } else {
                Log.d(LOG_TAG, "Left");
                Log.d(LOG_TAG, x + " " + body.getWidth() + " " + body.getHeight() + " " + metrics.widthPixels);
                if (x + getWidth() - getHeight() > metrics.widthPixels) {
                    Log.d(LOG_TAG, (x + getWidth() - getHeight()) + " > " + metrics.widthPixels + " x = " + x);
                    layoutParams.x = x - getWidth() - getHeight();
                    windowManager.updateViewLayout(this, layoutParams);
                }
            }
        } else {
            ((LinearLayout) body).setOrientation(LinearLayout.VERTICAL);
            reParams.addRule(RelativeLayout.BELOW, imageView.getId());
            body.setLayoutParams(reParams);
            if (y < metrics.heightPixels / 2) {
                Log.d(LOG_TAG, "Down");
            } else {
                Log.d(LOG_TAG, "Up");
//                reParams.addRule(RelativeLayout.BELOW, 0);
//                reParams.addRule(RelativeLayout.ALIGN_TOP);
//                body.setLayoutParams(reParams);
//                reParams.addRule(RelativeLayout.BELOW, body.getId());
//                imageView.setLayoutParams(reParams);
                if (y + getHeight() > metrics.heightPixels - getHeight() - getWidth() * 1.5) {
                    layoutParams.y = y - getHeight();
                    windowManager.updateViewLayout(this, layoutParams);
                }
            }
        }
    }

    private void changeProportinalPosition() {
        int overWidth;
        int overHeight;
        if (Math.round(metrics.widthPixels * widthProportion) + imageView.getWidth() < metrics.widthPixels) {
            layoutParams.x = Math.round(metrics.widthPixels * widthProportion);
        } else {
            overWidth = Math.round(metrics.widthPixels * widthProportion) + imageView.getWidth() - metrics.widthPixels;
            layoutParams.x = Math.round(metrics.widthPixels * widthProportion) - overWidth - getStatusBarHeight();
        }

        if (Math.round(metrics.heightPixels * heightProportion) + imageView.getWidth() < metrics.heightPixels - getStatusBarHeight()) {
            layoutParams.y = Math.round(metrics.heightPixels * heightProportion);
        } else {
            overHeight = Math.round(metrics.heightPixels * heightProportion) + imageView.getHeight() - metrics.heightPixels;
            layoutParams.y = Math.round(metrics.heightPixels * heightProportion) - overHeight - getStatusBarHeight();
        }
        windowManager.updateViewLayout(this, layoutParams);
        orientation = getResources().getConfiguration().orientation;
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private int firstX;
    private int firstY;

    private int lastX;
    private int lastY;

    public boolean moving;

    public int threshold = 10;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int totalDeltaX = lastX - firstX;
        int totalDeltaY = lastY - firstY;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = (int) event.getRawX();
                lastY = (int) event.getRawY();

                firstX = lastX;
                firstY = lastY;
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX = (int) event.getRawX() - lastX;
                int deltaY = (int) event.getRawY() - lastY;

                lastX = (int) event.getRawX();
                lastY = (int) event.getRawY();

                if (moving
                        || Math.abs(totalDeltaX) >= threshold
                        || Math.abs(totalDeltaY) >= threshold) {
                    moving = true;

                    // update the position of the view
                    if (event.getPointerCount() == 1) {
                        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                            if ((layoutParams.x + deltaX) > 0 && (layoutParams.x + deltaX) <= (metrics.widthPixels - getWidth())) {
                                layoutParams.x += deltaX;
                            }
                            if ((layoutParams.y + deltaY) > 0 && (layoutParams.y + deltaY) <= (metrics.heightPixels - getHeight() - getWidth() / 2)) {
                                layoutParams.y += deltaY;
                            }
                        } else {
                            if ((layoutParams.x + deltaX) > 0 && (layoutParams.x + deltaX) <= (metrics.widthPixels - getWidth())) {
                                layoutParams.x += deltaX;
                            }
                            if ((layoutParams.y + deltaY) > 0 && (layoutParams.y + deltaY) <= (metrics.heightPixels - getHeight() * 1.5)) {
                                layoutParams.y += deltaY;
                            }
                        }

                        widthProportion = (float) layoutParams.x / metrics.widthPixels;
                        heightProportion = (float) layoutParams.y / metrics.heightPixels;
                    }
                    windowManager.updateViewLayout(this, layoutParams);
                }
                break;
            case MotionEvent.ACTION_UP:
                moving = false;

                if (event.getPointerCount() == 1) {

                    boolean tap = Math.abs(totalDeltaX) < threshold
                            && Math.abs(totalDeltaY) < threshold;
                    if (tap) {
//                        if (mOnClickListener != null) {
//                            mOnClickListener.onClick(this);
//                        }
                        if (body.getVisibility() == VISIBLE) {
                            body.setVisibility(GONE);
                        } else {
                            body.setVisibility(VISIBLE);
                            Log.d(LOG_TAG, "Show body:" + body.getWidth() + " " + body.getHeight());
                            checkFreeSpace(layoutParams.x, layoutParams.y, body.getWidth(), body.getHeight());
                        }
                    }
                }
                break;
        }
        return true;
    }

    private OnClickListener mOnClickListener;

    @Override
    public void setOnClickListener(OnClickListener l) {
        mOnClickListener = l;

    }

}
