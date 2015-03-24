package amtt.epam.com.amtt.view;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import amtt.epam.com.amtt.R;

/**
 * Created by Ivan_Bakach on 23.03.2015.
 */
public class TopButtonView extends FrameLayout {

    private final static int NO_VIEW_FLAG = 0;

    private Button button;
    private FrameLayout body;
    private WindowManager windowManager;
    private WindowManager.LayoutParams layoutParams;
    private final static String LOG_TAG = "TAG";

    public TopButtonView(Context context, WindowManager windowManager, WindowManager.LayoutParams layoutParams) {
        super(context);
        initComponent();
        this.windowManager = windowManager;
        this.layoutParams = layoutParams;
        body = (FrameLayout) findViewById(R.id.body);
    }


    private void initComponent() {
        Log.e(LOG_TAG, "initComponent");
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
                        Toast.makeText(getContext(),"Tap button!", Toast.LENGTH_SHORT).show();
                    }
                }

                break;
        }

        return true;
    }
}
