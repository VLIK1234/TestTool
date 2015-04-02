package amtt.epam.com.amtt.view;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

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
    private Context context;
    private int orientation;
    private float widthProportion;
    private float heightProportion;

    public TopButtonView(Context context, WindowManager windowManager, WindowManager.LayoutParams layoutParams, DisplayMetrics displayMetrics) {
        super(context);
        this.context = context;
        initComponent();
        this.windowManager = windowManager;
        this.layoutParams = layoutParams;
        this.metrics = displayMetrics;
        orientation = getResources().getConfiguration().orientation;
        widthProportion = (float) layoutParams.x / metrics.widthPixels;
        heightProportion = (float) layoutParams.y / metrics.heightPixels;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int overWidth;
        int overHeight;
        if (getResources().getConfiguration().orientation != orientation) {
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
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private void initComponent() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.top_button_layout, this, true);
        body = (ViewGroup) findViewById(R.id.body);
        imageView = (ImageView) findViewById(R.id.plus_button);
        imageView.setImageResource(R.drawable.ic_top_button);
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
                        if ((layoutParams.x + deltaX) > 0 && (layoutParams.x + deltaX) <= (metrics.widthPixels - imageView.getWidth())) {
                            layoutParams.x += deltaX;
                        }
                        if ((layoutParams.y + deltaY) > 0 && (layoutParams.y + deltaY) <= (metrics.heightPixels - imageView.getHeight() * 1.5)) {
                            layoutParams.y += deltaY;
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
                        if (body.getVisibility() == GONE) {
                            body.setVisibility(VISIBLE);
                        } else if (body.getVisibility() == VISIBLE) {
                            body.setVisibility(GONE);
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
