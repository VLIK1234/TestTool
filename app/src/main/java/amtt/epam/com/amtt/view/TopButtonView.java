package amtt.epam.com.amtt.view;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.api.JiraCallback;
import amtt.epam.com.amtt.api.JiraTask;
import amtt.epam.com.amtt.api.JiraTask.JiraTaskType;
import amtt.epam.com.amtt.app.CreateIssueActivity;
import amtt.epam.com.amtt.app.LoginActivity;
import amtt.epam.com.amtt.app.UserInfoActivity;
import amtt.epam.com.amtt.api.rest.RestResponse;
import amtt.epam.com.amtt.bo.issue.createmeta.JMetaResponse;
import amtt.epam.com.amtt.api.result.UserDataResult;
import amtt.epam.com.amtt.util.Constants;
import amtt.epam.com.amtt.util.Converter;
import amtt.epam.com.amtt.util.CredentialsManager;
import amtt.epam.com.amtt.util.PreferenceUtils;

/**
 * Created by Ivan_Bakach on 23.03.2015.
 */
public class TopButtonView extends FrameLayout implements JiraCallback<UserDataResult, JMetaResponse> {

    private final static String LOG_TAG = "TAG";

    private WindowManager windowManager;
    private WindowManager.LayoutParams layoutParams;
    private LinearLayout buttonsBar;
    public ImageView mainButton;
    private DisplayMetrics metrics;
    private int currentOrientation;
    private float widthProportion;
    private float heightProportion;
    private Button[] buttonsArray;
    public Button buttonAuth;
    public Button buttonBugRep;
    public Button buttonUserInfo;

    private int firstX;
    private int firstY;
    private int lastX;
    private int lastY;
    public boolean moving;
    private TextView[] textViewArray;
    public TextView textUserInfo;
    public TextView textBugRep;
    private int xButton = 0;
    private int yButton = 0;
    private RelativeLayout topButtonLayout;

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
        topButtonLayout = (RelativeLayout)findViewById(R.id.top_button_layout);
    }

    @SuppressWarnings("unchecked")
    private void initComponent() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.top_button_layout, this, true);
        buttonsBar = (LinearLayout) findViewById(R.id.buttons_bar);
        mainButton = (ImageView) findViewById(R.id.plus_button);
        buttonsBar.setVisibility(GONE);
        TextView textAuth = (TextView) findViewById(R.id.text_auth);
        textUserInfo = (TextView) findViewById(R.id.text_user_info);
        TextView textAddStep = (TextView) findViewById(R.id.text_add_step);
        TextView textShowStep = (TextView) findViewById(R.id.text_show_step);
        textBugRep = (TextView) findViewById(R.id.text_bug_rep);
        textViewArray = new TextView[]{textAuth, textUserInfo, textAddStep, textShowStep, textBugRep};

        buttonAuth = (Button) findViewById(R.id.button_auth);
        buttonUserInfo = (Button) findViewById(R.id.button_user_info);
        Button buttonAddStep = (Button) findViewById(R.id.button_add_step);
        Button buttonShowStep = (Button) findViewById(R.id.button_show_step);
        buttonBugRep = (Button) findViewById(R.id.button_bug_rep);
        buttonsArray = new Button[]{buttonAuth, buttonUserInfo, buttonAddStep, buttonShowStep, buttonBugRep};

        OnClickListener listenerAuth = new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!CredentialsManager.getInstance().getAccessState()) {
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getContext().getApplicationContext().startActivity(intent);
                } else {
                    //logout logic
                }
            }
        };
        buttonAuth.setOnClickListener(listenerAuth);
        textAuth.setOnClickListener(listenerAuth);
        OnClickListener listenerUserInfo = new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), UserInfoActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().getApplicationContext().startActivity(intent);
            }
        };
        buttonUserInfo.setOnClickListener(listenerUserInfo);
        textUserInfo.setOnClickListener(listenerUserInfo);

        OnClickListener listenerAddStep = new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "STEP", Toast.LENGTH_LONG).show();
//                Intent intentATS = new Intent(BaseActivity.ACTION_SAVE_STEP);
//                getContext().sendBroadcast(intentATS);
            }
        };
        buttonAddStep.setOnClickListener(listenerAddStep);
        textAddStep.setOnClickListener(listenerAddStep);

        OnClickListener listnerShowStep = new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "SHOW", Toast.LENGTH_LONG).show();
//                Intent intent = new Intent(getContext(), StepsActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                getContext().getApplicationContext().startActivity(intent);
            }
        };
        buttonShowStep.setOnClickListener(listnerShowStep);
        textShowStep.setOnClickListener(listnerShowStep);

        OnClickListener listenerBugRep = new OnClickListener() {
            @Override
            public void onClick(View v) {
                new JiraTask.Builder<UserDataResult,JMetaResponse>()
                        .setOperationType(JiraTaskType.SEARCH)
                        .setSearchType(JiraTask.JiraSearchType.ISSUE)
                        .setCallback(TopButtonView.this)
                        .create()
                        .execute();
            }
        };
        buttonBugRep.setOnClickListener(listenerBugRep);
        textBugRep.setOnClickListener(listenerBugRep);
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
                if (layoutParams.x + buttonsBar.getWidth() > metrics.widthPixels) {
                    layoutParams.x -= (layoutParams.x + buttonsBar.getWidth() - metrics.widthPixels);
                    windowManager.updateViewLayout(TopButtonView.this, layoutParams);
                }
                if (layoutParams.y + mainButton.getHeight() + buttonsBar.getHeight() > metrics.heightPixels - getStatusBarHeight()) {
                    layoutParams.y -= (layoutParams.y + mainButton.getHeight() + buttonsBar.getHeight() - metrics.heightPixels + getStatusBarHeight());
                    windowManager.updateViewLayout(TopButtonView.this, layoutParams);
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
                        if ((layoutParams.x + deltaX) > 0 && (layoutParams.x + deltaX) <= (metrics.widthPixels - getWidth())) {
                            layoutParams.x += deltaX;
                            xButton = layoutParams.x;
                        }
                        if ((layoutParams.y + deltaY) > 0 && (layoutParams.y + deltaY) <= (metrics.heightPixels - getHeight() - getStatusBarHeight())) {
                            layoutParams.y += deltaY;
                            yButton = layoutParams.y;
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
                            Animation reverseRotate = AnimationUtils.loadAnimation(getContext(), R.anim.reverse_rotate);
                            mainButton.startAnimation(reverseRotate);
                            reverseRotate.setFillAfter(true);
                            final Animation translateUp = AnimationUtils.loadAnimation(getContext(), R.anim.abc_fade_out);
                            translateUp.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {
                                    Animation translateMainButton = new TranslateAnimation(0, xButton - layoutParams.x, 0, yButton - layoutParams.y);
                                    translateMainButton.setDuration(300);
                                    translateMainButton.setInterpolator(new DecelerateInterpolator());
                                    translateMainButton.setAnimationListener(new Animation.AnimationListener() {
                                        @Override
                                        public void onAnimationStart(Animation animation) {

                                        }

                                        @Override
                                        public void onAnimationEnd(Animation animation) {
                                            layoutParams.x = xButton;
                                            layoutParams.y = yButton;
                                            windowManager.updateViewLayout(TopButtonView.this, layoutParams);
                                        }

                                        @Override
                                        public void onAnimationRepeat(Animation animation) {

                                        }
                                    });
                                    topButtonLayout.startAnimation(translateMainButton);
                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    buttonsBar.setVisibility(GONE);
                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {

                                }
                            });
                            buttonsBar.startAnimation(translateUp);

                        } else {
                            buttonsBar.setVisibility(VISIBLE);
                            xButton = layoutParams.x;
                            yButton = layoutParams.y;
                            Animation rotate = AnimationUtils.loadAnimation(getContext(), R.anim.rotate);
                            rotate.setFillAfter(true);
                            mainButton.startAnimation(rotate);
                            Animation translate = AnimationUtils.loadAnimation(getContext(), R.anim.translate);
                            buttonsBar.startAnimation(translate);
                            Animation combination = AnimationUtils.loadAnimation(getContext(), R.anim.combination);
                            long durationAnimation = combination.getDuration();
                            for (int i = 0; i < buttonsArray.length; i++, durationAnimation+=100) {
                                combination.setDuration(durationAnimation);
                                buttonsArray[i].startAnimation(combination);
                            }
                            Animation alpha = AnimationUtils.loadAnimation(getContext(), R.anim.alpha);
                            durationAnimation = 300;
                            for (int i = 0; i < textViewArray.length; i++, durationAnimation+=100) {
                                alpha.setDuration(durationAnimation);
                                textViewArray[i].startAnimation(alpha);
                            }

                            checkFreeSpace();
                        }
                    }
                }
                break;
        }
        return true;
    }

    @Override
    public void onJiraRequestPerformed(RestResponse<UserDataResult,JMetaResponse> restResponse) {
        if (restResponse.getResult() == UserDataResult.SUCCESS) {
            JMetaResponse jiraMetaResponse = restResponse.getResultObject();
            ArrayList<String> projectsNames = jiraMetaResponse.getProjectsNames();
            ArrayList<String> projectsKeys = jiraMetaResponse.getProjectsKeys();
            PreferenceUtils.putSet(Constants.SharedPreferenceKeys.PROJECTS_NAMES, Converter.arrayListToSet(projectsNames));
            PreferenceUtils.putSet(Constants.SharedPreferenceKeys.PROJECTS_KEYS, Converter.arrayListToSet(projectsKeys));
            Intent intent = new Intent(getContext(), CreateIssueActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getContext().getApplicationContext().startActivity(intent);
        } else {
            Toast.makeText(getContext(), restResponse.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation != currentOrientation) {
            savePositionAfterTurnScreen();
            buttonsBar.setVisibility(GONE);
        }
    }

}
