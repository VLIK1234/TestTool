package amtt.epam.com.amtt.topbutton.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.ui.activities.AskExitActivity;
import amtt.epam.com.amtt.ui.activities.CreateIssueActivity;
import amtt.epam.com.amtt.ui.activities.ExpectedResultsActivity;
import amtt.epam.com.amtt.ui.activities.HelpDialogActivity;
import amtt.epam.com.amtt.ui.activities.StepsActivity;
import amtt.epam.com.amtt.ui.activities.UserInfoActivity;
import amtt.epam.com.amtt.topbutton.service.TopButtonService;
import amtt.epam.com.amtt.util.ActivityMetaUtil;
import amtt.epam.com.amtt.database.util.StepUtil;
import amtt.epam.com.amtt.util.PreferenceUtils;
import amtt.epam.com.amtt.util.TestUtil;
import amtt.epam.com.amtt.util.UIUtil;

/**
 @author Artsiom_Kaliaha
 @version on 27.05.2015
 */

@SuppressLint("ViewConstructor")
public class TopButtonBarView extends FrameLayout {

    private final WindowManager mWindowManager;
    private static boolean isRecordStarted;
    private static boolean isShowAction;
    private WindowManager.LayoutParams mLayout;
    private LinearLayout mButtonsBar;
    private TopUnitView mButtonStartRecord;
    private TopUnitView mButtonCreateTicket;
    private TopUnitView mButtonOpenUserInfo;
    private TopUnitView mButtonExpectedResult;
    private TopUnitView mButtonStepWithScreen;
    private TopUnitView mButtonStepWithoutScreen;
    private TopUnitView mButtonStopRecord;
    private TopUnitView mButtonShowSteps;
    private TopUnitView mButtonCloseApp;
    private int mMainButtonHeight;
    private int mMainButtonWidth;
    private amtt.epam.com.amtt.topbutton.view.OnTouchListener mOnTouchListener;

    static {
        isRecordStarted = false;
    }

    public TopButtonBarView(Context context, int mainButtonHeight, int mainButtonWidth, amtt.epam.com.amtt.topbutton.view.OnTouchListener onTouchListener) {
        super(context);
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mMainButtonHeight = mainButtonHeight;
        mMainButtonWidth = mainButtonWidth;
        initLayout();
        initButtonsBar();
        initButtonsHandlers();
        setInitialButtons();
        mOnTouchListener = onTouchListener;
    }

    private void initLayout() {
        int flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FORMAT_CHANGED;
        mLayout = new WindowManager.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 0, 0, WindowManager.LayoutParams.TYPE_PHONE,
                flags, PixelFormat.TRANSPARENT);
        mLayout.gravity = Gravity.TOP | Gravity.START;
        mWindowManager.addView(this, mLayout);
    }

    private void initButtonsBar() {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.top_button_bar_layout, this, true);
        mButtonsBar = (LinearLayout) findViewById(R.id.buttons_bar);
        findViewById(R.id.top_button_layout).addOnLayoutChangeListener(new OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (isShowAction) {
                    setDisplayPoint(mLayout.x, mLayout.y);
                    isShowAction = false;
                }
            }
        });
    }

    private void initButtonsHandlers() {
        mButtonStartRecord = new TopUnitView(getContext(), getContext().getString(R.string.label_start_record), R.drawable.background_start_record, new amtt.epam.com.amtt.topbutton.view.OnTouchListener() {
            @Override
            public void onTouch() {
                isRecordStarted = true;
                TestUtil.runTests();
                hide();
                StepUtil.clearAllStep();
                Toast.makeText(getContext(), getContext().getString(R.string.label_start_record), Toast.LENGTH_LONG).show();
                mOnTouchListener.onTouch();
            }
        });
        mButtonCreateTicket = new TopUnitView(getContext(), getContext().getString(R.string.label_create_ticket), R.drawable.background_create_ticket, new amtt.epam.com.amtt.topbutton.view.OnTouchListener() {
            @Override
            public void onTouch() {
                Intent intentTicket = new Intent(getContext(), CreateIssueActivity.class);
                intentTicket.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().getApplicationContext().startActivity(intentTicket);
                mOnTouchListener.onTouch();
            }
        });
        mButtonOpenUserInfo = new TopUnitView(getContext(), getContext().getString(R.string.label_open_amtt), R.drawable.background_user_info, new amtt.epam.com.amtt.topbutton.view.OnTouchListener() {
            @Override
            public void onTouch() {
                TopButtonService.sendActionChangeVisibilityTopbutton(false);
                Intent userInfoIntent = new Intent(getContext(), UserInfoActivity.class);
                userInfoIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().getApplicationContext().startActivity(userInfoIntent);
                mOnTouchListener.onTouch();
            }
        });
        mButtonExpectedResult = new TopUnitView(getContext(), getContext().getString(R.string.label_expected_result), R.drawable.background_expected_result, new amtt.epam.com.amtt.topbutton.view.OnTouchListener() {
            @Override
            public void onTouch() {
                TopButtonService.sendActionChangeVisibilityTopbutton(false);
                Intent intent = new Intent(getContext(), ExpectedResultsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().getApplicationContext().startActivity(intent);
                mOnTouchListener.onTouch();
            }
        });
        mButtonStepWithScreen = new TopUnitView(getContext(), getContext().getString(R.string.label_step_with_screen), R.drawable.background_step_with_screen, new amtt.epam.com.amtt.topbutton.view.OnTouchListener() {
            @Override
            public void onTouch() {
                if (!PreferenceUtils.getBoolean(getContext().getString(R.string.key_dialog_hide))) {
                    Intent intentHelp = new Intent(getContext(), HelpDialogActivity.class);
                    intentHelp.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getContext().getApplicationContext().startActivity(intentHelp);
                }else{
                    HelpDialogActivity.setIsCanTakeScreenshot(true);
                }
                TopButtonService.sendActionChangeVisibilityTopbutton(false);
                mOnTouchListener.onTouch();
            }
        });
        mButtonStepWithoutScreen = new TopUnitView(getContext(), getContext().getString(R.string.label_step_without_screen), R.drawable.background_step_without_screen, new amtt.epam.com.amtt.topbutton.view.OnTouchListener() {
            @Override
            public void onTouch() {
                Toast.makeText(getContext(), getContext().getString(R.string.label_added_step_without_screen), Toast.LENGTH_LONG).show();
                ScheduledExecutorService worker =
                        Executors.newSingleThreadScheduledExecutor();
                Runnable task = new Runnable() {
                    public void run() {
                        StepUtil.saveStep(ActivityMetaUtil.getTopActivityComponent(), null);
                    }
                };
                worker.schedule(task, 1, TimeUnit.SECONDS);
                mOnTouchListener.onTouch();
            }
        });
        mButtonShowSteps = new TopUnitView(getContext(), getContext().getString(R.string.label_show_steps), R.drawable.background_show_step, new amtt.epam.com.amtt.topbutton.view.OnTouchListener() {
            @Override
            public void onTouch() {
                Intent intent = new Intent(getContext(), StepsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().getApplicationContext().startActivity(intent);
                mOnTouchListener.onTouch();
            }
        });
        mButtonStopRecord = new TopUnitView(getContext(), getContext().getString(R.string.label_cancel_record), R.drawable.background_stop_record, new amtt.epam.com.amtt.topbutton.view.OnTouchListener() {
            @Override
            public void onTouch() {
                isRecordStarted = false;
                TestUtil.closeTest();
                hide();
                StepUtil.clearAllStep();
                Toast.makeText(getContext(), getContext().getString(R.string.label_cancel_record), Toast.LENGTH_LONG).show();
                mOnTouchListener.onTouch();
            }
        });
        mButtonCloseApp = new TopUnitView(getContext(), getContext().getString(R.string.label_close_app), R.drawable.background_close, new amtt.epam.com.amtt.topbutton.view.OnTouchListener() {
            @Override
            public void onTouch() {
                TopButtonService.sendActionChangeVisibilityTopbutton(false);
                Intent intentAsk = new Intent(getContext(), AskExitActivity.class);
                intentAsk.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().getApplicationContext().startActivity(intentAsk);
                mOnTouchListener.onTouch();
            }
        });
    }

    private void setInitialButtons() {
        mButtonsBar.removeAllViews();
        mButtonsBar.addView(mButtonStartRecord);
        mButtonsBar.addView(mButtonCreateTicket);
        mButtonsBar.addView(mButtonExpectedResult);
        mButtonsBar.addView(mButtonOpenUserInfo);
        mButtonsBar.addView(mButtonCloseApp);
    }

    private void setRecordButtons() {
        mButtonsBar.removeAllViews();
        mButtonsBar.addView(mButtonStepWithScreen);
        mButtonsBar.addView(mButtonStepWithoutScreen);
        mButtonsBar.addView(mButtonExpectedResult);
        mButtonsBar.addView(mButtonShowSteps);
        mButtonsBar.addView(mButtonCreateTicket);
        mButtonsBar.addView(mButtonStopRecord);
        mButtonsBar.addView(mButtonOpenUserInfo);
    }


    private void setBelow(int x, int y) {
        mLayout.x = x;
        mLayout.y = y + mMainButtonHeight;
    }

    private void setAbove(int x, int y) {
        mLayout.x = x;
        mLayout.y = y - mButtonsBar.getHeight();
    }

    private void setOnTheLeft(int x, int y) {
        mLayout.x = x - mButtonsBar.getWidth();
        mLayout.y = y;
    }

    private void setOnTheRight(int x, int y) {
        mLayout.x = x + mMainButtonWidth;
        mLayout.y = y;
    }

    public void show(final int x, final int y) {
        mButtonsBar.setVisibility(VISIBLE);
        Animation fadeIn = AnimationUtils.loadAnimation(getContext(), R.anim.abc_fade_in);
        mButtonsBar.startAnimation(fadeIn);
        mButtonsBar.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                mButtonsBar.getViewTreeObserver().removeOnPreDrawListener(this);
                mLayout.x = x;
                mLayout.y = y;
                isShowAction = true;
                if (isRecordStarted) {
                    setRecordButtons();
                } else {
                    setInitialButtons();
                }
                mWindowManager.updateViewLayout(TopButtonBarView.this, mLayout);
                return true;
            }
        });
    }

    public void hide() {
        Animation translateUp = AnimationUtils.loadAnimation(getContext(), R.anim.abc_fade_out);
        translateUp.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mButtonsBar.setVisibility(GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mButtonsBar.startAnimation(translateUp);
    }

    public void setIsRecordStarted(boolean isRecordStarted){
        TopButtonBarView.isRecordStarted = isRecordStarted;
    }

    public void move(int x, int y) {
        if (mButtonsBar.getVisibility() == VISIBLE) {
            setDisplayPoint(x, y);
        }
    }

    private void setDisplayPoint(int x, int y) {
        if (UIUtil.getOrientation() == Configuration.ORIENTATION_PORTRAIT) {
            mButtonsBar.setOrientation(LinearLayout.VERTICAL);
            if (y + mButtonsBar.getHeight() > UIUtil.getDisplayMetrics().heightPixels - UIUtil.getStatusBarHeight()) {
                setAbove(x, y);
            } else {
                setBelow(x, y);
            }
        } else {
            mButtonsBar.setOrientation(LinearLayout.HORIZONTAL);
            if (x + mButtonsBar.getWidth() > UIUtil.getDisplayMetrics().widthPixels) {
                setOnTheLeft(x, y);
            } else {
                setOnTheRight(x, y);
            }
        }
        mWindowManager.updateViewLayout(this, mLayout);
    }

    @Override
    public int getVisibility() {
        if (mButtonsBar != null) {
            return mButtonsBar.getVisibility();
        }
        return GONE;
    }

}
