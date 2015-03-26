package amtt.epam.com.amtt.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.image.ImageSavingCallback;
import amtt.epam.com.amtt.image.ImageSavingResult;
import amtt.epam.com.amtt.image.ImageSavingTask;

/**
 * Created by Ivan_Bakach on 23.03.2015.
 */
public class TopButtonView extends FrameLayout implements ImageSavingCallback {

    private final static int NO_VIEW_FLAG = 0;

    private Button button;
    private FrameLayout body;
    private WindowManager windowManager;
    private WindowManager.LayoutParams layoutParams;
    private Context context;
    private Activity activity;
    private final static String LOG_TAG = "TAG";
    private int mScreenNumber = 1;
    public OnClickListener onClickListener;

    public TopButtonView(Context context, WindowManager windowManager, WindowManager.LayoutParams layoutParams) {
        super(context);
        initComponent();
        this.windowManager = windowManager;
        this.layoutParams = layoutParams;
        body = (FrameLayout) findViewById(R.id.body);

    }


    private void initComponent() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        inflater.inflate(R.layout.top_button_layout, this, true);
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
                        layoutParams.x += deltaX;
                        layoutParams.y += deltaY;
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
                        Log.d(LOG_TAG, "Click");
                        if(mOnClickListener!=null){
                            mOnClickListener.onClick(this);
                        }
                    }
                }
                break;
        }
        return true;
    }

    @Override
    public void onImageSaved(ImageSavingResult result) {
        mScreenNumber++;
        int resultMessage = result == ImageSavingResult.ERROR ? R.string.image_saving_error : R.string.image_saving_success;
        Toast.makeText(activity, resultMessage, Toast.LENGTH_SHORT).show();
    }

    private OnClickListener mOnClickListener;

    @Override
    public void setOnClickListener(OnClickListener l) {
        mOnClickListener = l;

    }

    @Override
    public int getScreenNumber() {
        return mScreenNumber;
    }
}
