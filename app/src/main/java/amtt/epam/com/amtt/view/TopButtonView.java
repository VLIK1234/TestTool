package amtt.epam.com.amtt.view;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.app.BaseActivity;
import amtt.epam.com.amtt.app.SecondActivity;
import amtt.epam.com.amtt.app.StepsActivity;

/**
 * Created by Ivan_Bakach on 23.03.2015.
 */
public class TopButtonView extends FrameLayout {

    private final static String LOG_TAG = "TAG";

    private WindowManager windowManager;
    private WindowManager.LayoutParams layoutParams;
    private LinearLayout buttonsBar;
    private ImageView mainButton;
    private DisplayMetrics metrics;
    private int currentOrientation;
    private float widthProportion;
    private float heightProportion;
    //TODO you can convert it field to local variable
    private RelativeLayout.LayoutParams topButtonLayoutParams;

    public TopButtonView(Context context) {
        //TODO what happen if you try use this constructor?
        super(context, null);
    }

    public TopButtonView(Context context, WindowManager.LayoutParams layoutParams) {
        super(context);
        initComponent();

        buttonsBar.setOrientation(LinearLayout.VERTICAL);

        this.windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        //TODO why you use operator "this" in one case
        this.metrics = getContext().getResources().getDisplayMetrics();
        //TODO and don't use in the same
        currentOrientation = getResources().getConfiguration().orientation;
        this.layoutParams = layoutParams;
        widthProportion = (float) layoutParams.x / metrics.widthPixels;
        heightProportion = (float) layoutParams.y / metrics.heightPixels;
    }

    private void initComponent() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.top_button_layout, this, true);
        buttonsBar = (LinearLayout) findViewById(R.id.buttons_bar);
        mainButton = (ImageView) findViewById(R.id.plus_button);
        mainButton.setImageResource(R.drawable.ic_top_button);
        buttonsBar.setVisibility(GONE);

        Button buttonAuth = (Button) findViewById(R.id.button_auth);
        buttonAuth.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "AUTH", Toast.LENGTH_LONG).show();
            }
        });
        Button buttonUserInfo = (Button) findViewById(R.id.button_user_info);
        buttonUserInfo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "INFO", Toast.LENGTH_LONG).show();
            }
        });
        Button buttonAddStep = (Button) findViewById(R.id.button_add_step);
        buttonAddStep.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "STEP", Toast.LENGTH_LONG).show();
                Intent intentATS = new Intent(BaseActivity.ACTION_TAKE_SCREENSHOT);
                getContext().sendBroadcast(intentATS);
            }
        });
        Button buttonShowStep = (Button) findViewById(R.id.button_show_step);
        buttonShowStep.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "SHOW", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getContext(), StepsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().getApplicationContext().startActivity(intent);
            }
        });
        Button buttonBugRep = (Button) findViewById(R.id.button_bug_rep);
        buttonBugRep.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "BUG_REP", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getContext(), SecondActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().getApplicationContext().startActivity(intent);
            }
        });
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (getResources().getConfiguration().orientation != currentOrientation) {
            changeProportinalPosition();
            buttonsBar.setVisibility(GONE);
        }
    }

    private void checkFreeSpace() {
        topButtonLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            buttonsBar.setOrientation(LinearLayout.HORIZONTAL);
            topButtonLayoutParams.addRule(RelativeLayout.RIGHT_OF, mainButton.getId());
            buttonsBar.setLayoutParams(topButtonLayoutParams);
        } else {
            buttonsBar.setOrientation(LinearLayout.VERTICAL);
            topButtonLayoutParams.addRule(RelativeLayout.BELOW, mainButton.getId());
            buttonsBar.setLayoutParams(topButtonLayoutParams);
        }
        //TODO please give friendly name for variable
        ViewTreeObserver vto = buttonsBar.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                buttonsBar.getViewTreeObserver().removeOnPreDrawListener(this);

                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    //TODO every time you do extra checks. If you don't use first "if", please move logic from "else" to "if"
                    if (layoutParams.x < metrics.widthPixels / 2) {
                        //Right
                    } else {
                        //Left
                        layoutParams.x -= (layoutParams.x + buttonsBar.getWidth() - metrics.widthPixels + buttonsBar.getHeight());
                        windowManager.updateViewLayout(TopButtonView.this, layoutParams);
                    }
                } else {
                    if (layoutParams.y + body.getWidth() / 2 < metrics.heightPixels / 2) {
                        //Down
                    } else {
                        //Up
                        //TODO what is the coefficient 1.5?
                        layoutParams.y -= (layoutParams.y + buttonsBar.getHeight() - metrics.heightPixels + buttonsBar.getWidth() * 1.5);
                        windowManager.updateViewLayout(TopButtonView.this, layoutParams);
                    }
                }
                return true;
            }
        });

    }

    //TODO please give method more firendly name
    private void changeProportinalPosition() {
        int overWidth;
        int overHeight;
        if (Math.round(metrics.widthPixels * widthProportion) + mainButton.getWidth() < metrics.widthPixels) {
            layoutParams.x = Math.round(metrics.widthPixels * widthProportion);
        } else {
            overWidth = Math.round(metrics.widthPixels * widthProportion) + mainButton.getWidth() - metrics.widthPixels;
            layoutParams.x = Math.round(metrics.widthPixels * widthProportion) - overWidth - getStatusBarHeight();
        }

        if (Math.round(metrics.heightPixels * heightProportion) + mainButton.getWidth() < metrics.heightPixels - getStatusBarHeight()) {
            layoutParams.y = Math.round(metrics.heightPixels * heightProportion);
        } else {
            overHeight = Math.round(metrics.heightPixels * heightProportion) + mainButton.getHeight() - metrics.heightPixels;
            layoutParams.y = Math.round(metrics.heightPixels * heightProportion) - overHeight - getStatusBarHeight();
        }
        windowManager.updateViewLayout(this, layoutParams);
        currentOrientation = getResources().getConfiguration().orientation;
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    //TODO why you declare variables on this place?
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
                            //TODO what is the coefficient 1.5?
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
                        if (buttonsBar.getVisibility() == VISIBLE) {
                            buttonsBar.setVisibility(GONE);
                        } else {
                            buttonsBar.setVisibility(VISIBLE);
                            checkFreeSpace();
                        }
                    }
                }
                break;
        }
        return true;
    }
}
