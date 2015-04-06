package amtt.epam.com.amtt.view;

import amtt.epam.com.amtt.R;
import android.content.Context;
import android.graphics.PixelFormat;
import android.util.DisplayMetrics;
import android.view.*;
import android.widget.FrameLayout;
import android.widget.ImageView;

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
    private int orientation;
    private float widthProportion;
    private float heightProportion;

    public TopButtonView(Context context, WindowManager.LayoutParams layoutParams) {
        super(context);
        initComponent();
        this.windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        this.metrics = getContext().getResources().getDisplayMetrics();
        this.layoutParams = layoutParams;
        orientation = getResources().getConfiguration().orientation;
        widthProportion = (float) layoutParams.x / metrics.widthPixels;
        heightProportion = (float) layoutParams.y / metrics.heightPixels;
    }

    public TopButtonView(Context context) {
        super(context);
        initComponent();
        this.windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        this.metrics = getContext().getResources().getDisplayMetrics();

        layoutParams = new WindowManager.LayoutParams();
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
            | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FORMAT_CHANGED;
        layoutParams.format = PixelFormat.TRANSLUCENT;
        layoutParams.gravity = Gravity.TOP | Gravity.LEFT;

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
                        if (body.getVisibility() == VISIBLE) {
                            body.setVisibility(GONE);
                        } else {
                            body.setVisibility(VISIBLE);
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
