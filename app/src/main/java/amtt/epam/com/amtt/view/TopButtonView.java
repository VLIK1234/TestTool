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

import java.util.ArrayList;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.app.BaseActivity;
import amtt.epam.com.amtt.app.CreateIssueActivity;
import amtt.epam.com.amtt.app.LoginActivity;
import amtt.epam.com.amtt.app.SecondActivity;
import amtt.epam.com.amtt.app.StepsActivity;
import amtt.epam.com.amtt.app.UserInfoActivity;
import amtt.epam.com.amtt.asynctask.ShowUserDataTask;
import amtt.epam.com.amtt.bo.issue.TypeSearchedData;
import amtt.epam.com.amtt.bo.issue.createmeta.JMetaResponse;
import amtt.epam.com.amtt.callbacks.ShowUserDataCallback;
import amtt.epam.com.amtt.service.TopButtonService;
import amtt.epam.com.amtt.util.Constants;
import amtt.epam.com.amtt.util.Converter;
import amtt.epam.com.amtt.util.CredentialsManager;
import amtt.epam.com.amtt.util.PreferenceUtils;

/**
 * Created by Ivan_Bakach on 23.03.2015.
 */
public class TopButtonView extends FrameLayout implements ShowUserDataCallback {

    private final static String LOG_TAG = "TAG";

    private WindowManager windowManager;
    private WindowManager.LayoutParams layoutParams;
    private LinearLayout buttonsBar;
    private ImageView mainButton;
    private DisplayMetrics metrics;
    private int currentOrientation;
    private float widthProportion;
    private float heightProportion;
    private boolean isAccess = false;
    public Button buttonAuth;
    public Button buttonBugRep;
    public Button buttonUserInfo;

    private int firstX;
    private int firstY;
    private int lastX;
    private int lastY;
    public boolean moving;

    public TopButtonView(Context context, WindowManager.LayoutParams layoutParams) {
        super(context);
        initComponent();

        buttonsBar.setOrientation(LinearLayout.VERTICAL);

        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        metrics = getContext().getResources().getDisplayMetrics();
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

        buttonAuth = (Button) findViewById(R.id.button_auth);
        buttonAuth.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!CredentialsManager.getInstance().getAccessState()) {
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getContext().getApplicationContext().startActivity(intent);
                }else{
                }
            }
        });
        buttonUserInfo = (Button) findViewById(R.id.button_user_info);
        buttonUserInfo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), UserInfoActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().getApplicationContext().startActivity(intent);
            }
        });
        Button buttonAddStep = (Button) findViewById(R.id.button_add_step);
        buttonAddStep.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "STEP", Toast.LENGTH_LONG).show();
//                Intent intentATS = new Intent(BaseActivity.ACTION_SAVE_STEP);
//                getContext().sendBroadcast(intentATS);
            }
        });
        Button buttonShowStep = (Button) findViewById(R.id.button_show_step);
        buttonShowStep.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "SHOW", Toast.LENGTH_LONG).show();
//                Intent intent = new Intent(getContext(), StepsActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                getContext().getApplicationContext().startActivity(intent);
            }
        });
        buttonBugRep = (Button) findViewById(R.id.button_bug_rep);
        buttonBugRep.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new ShowUserDataTask(TypeSearchedData.SEARCH_ISSUE, TopButtonView.this).execute();
            }
        });
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (getResources().getConfiguration().orientation != currentOrientation) {
            savePositionAfterTurnScreen();
            buttonsBar.setVisibility(GONE);
        }
    }

    private void checkFreeSpace() {
        RelativeLayout.LayoutParams topButtonLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
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

        ViewTreeObserver viewTreeObserver = buttonsBar.getViewTreeObserver();
        viewTreeObserver.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                buttonsBar.getViewTreeObserver().removeOnPreDrawListener(this);
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    if (layoutParams.x+mainButton.getWidth()+buttonsBar.getWidth() > metrics.widthPixels) {
                        layoutParams.x -= (layoutParams.x + mainButton.getWidth()+ buttonsBar.getWidth() - metrics.widthPixels);
                        windowManager.updateViewLayout(TopButtonView.this, layoutParams);
                    }
                } else {
                    if (layoutParams.y + mainButton.getHeight() + buttonsBar.getHeight() > metrics.heightPixels - getStatusBarHeight()) {
                        layoutParams.y -= (layoutParams.y + mainButton.getHeight() + buttonsBar.getHeight() - metrics.heightPixels + getStatusBarHeight());
                        windowManager.updateViewLayout(TopButtonView.this, layoutParams);
                    }
                }
                return true;
            }
        });

    }

    private void savePositionAfterTurnScreen() {
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int threshold = 10;
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
                            if ((layoutParams.y + deltaY) > 0 && (layoutParams.y + deltaY) <= (metrics.heightPixels - getHeight() - getStatusBarHeight())) {
                                layoutParams.y += deltaY;
                            }
                        } else {
                            if ((layoutParams.x + deltaX) > 0 && (layoutParams.x + deltaX) <= (metrics.widthPixels - getWidth())) {
                                layoutParams.x += deltaX;
                            }
                            if ((layoutParams.y + deltaY) > 0 && (layoutParams.y + deltaY) <= (metrics.heightPixels - getHeight() - getStatusBarHeight())) {
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

    @Override
    public void onShowUserDataResult(JMetaResponse result) {
        ArrayList<String> projectsNames = result.getProjectsNames();
        ArrayList<String> projectsKeys = result.getProjectsKeys();
        PreferenceUtils.putSet(Constants.SharedPreferenceKeys.PROJECTS_NAMES, Converter.arrayListToSet(projectsNames));
        PreferenceUtils.putSet(Constants.SharedPreferenceKeys.PROJECTS_KEYS, Converter.arrayListToSet(projectsKeys));
        Intent intent = new Intent(getContext(), CreateIssueActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getContext().getApplicationContext().startActivity(intent);
    }
}
