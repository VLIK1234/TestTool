package amtt.epam.com.amtt.topbutton.view;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.IOException;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.app.CreateIssueActivity;
import amtt.epam.com.amtt.app.HelpDialogActivity;
import amtt.epam.com.amtt.app.StepsActivity;
import amtt.epam.com.amtt.app.UserInfoActivity;
import amtt.epam.com.amtt.database.task.DataBaseCRUD;
import amtt.epam.com.amtt.database.task.DataBaseCallback;
import amtt.epam.com.amtt.database.task.DataBaseMethod;
import amtt.epam.com.amtt.database.task.DataBaseTask;
import amtt.epam.com.amtt.topbutton.service.TopButtonService;
import amtt.epam.com.amtt.util.ActivityMetaUtil;
import amtt.epam.com.amtt.util.UIUtil;

/**
 * Created by Artsiom_Kaliaha on 27.05.2015.
 */
public class TopButtonBarView extends FrameLayout implements DataBaseCallback {

    private WindowManager.LayoutParams mLayout;
    private final WindowManager mWindowManager;
    private LinearLayout mButtonsBar;
    private int mMainButtonHeight;
    private int mMainButtonWidth;

    private TopUnitView mButtonStartRecord;
    private TopUnitView mButtonCreateTicket;
    private TopUnitView mButtonOpenUserInfo;
    private TopUnitView mButtonExpectedResult;
    private TopUnitView mButtonTakeScreenshot;
    private TopUnitView mButtonActivityInfo;
    private TopUnitView mButtonTakeStep;
    private TopUnitView mButtonStopRecord;
    private TopUnitView mButtonShowSteps;
    private TopUnitView mButtonCloseApp;

    private static int sStepNumber; //responsible for steps ordering in database
    private static boolean isRecordStarted;

    static {
        isRecordStarted = false;
    }

    public TopButtonBarView(Context context, int mainButtonHeight, int mainButtonWidth) {
        super(context);
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mMainButtonHeight = mainButtonHeight;
        mMainButtonWidth = mainButtonWidth;
        initLayout();
        initButtonsBar();
        initButtonsHandlers();
        setInitialButtons();
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
    }

    @SuppressWarnings("unchecked")
    private void initButtonsHandlers() {
        mButtonStartRecord = new TopUnitView(getContext(), getContext().getString(R.string.label_start_record), R.drawable.background_start_record, new ITouchAction() {
            @Override
            public void TouchAction() {
                isRecordStarted = true;
                hide();
                Toast.makeText(getContext(), getContext().getString(R.string.label_start_record), Toast.LENGTH_LONG).show();
            }
        });
        mButtonCreateTicket = new TopUnitView(getContext(), getContext().getString(R.string.label_create_ticket), R.drawable.background_create_ticket, new ITouchAction() {
            @Override
            public void TouchAction() {
                Toast.makeText(getContext(), getContext().getString(R.string.label_create_ticket), Toast.LENGTH_LONG).show();
                Intent intentTicket = new Intent(getContext(), CreateIssueActivity.class);
                intentTicket.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().getApplicationContext().startActivity(intentTicket);
            }
        });
        mButtonOpenUserInfo = new TopUnitView(getContext(), getContext().getString(R.string.label_open_amtt), R.drawable.background_user_info, new ITouchAction() {
            @Override
            public void TouchAction() {
                Intent userInfoIntent = new Intent(getContext(), UserInfoActivity.class);
                userInfoIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().getApplicationContext().startActivity(userInfoIntent);
            }
        });
        mButtonExpectedResult = new TopUnitView(getContext(), getContext().getString(R.string.label_expected_result), R.drawable.background_expected_result, new ITouchAction() {
            @Override
            public void TouchAction() {
                Toast.makeText(getContext(), getContext().getString(R.string.label_expected_result), Toast.LENGTH_LONG).show();
            }
        });
        mButtonTakeScreenshot = new TopUnitView(getContext(), getContext().getString(R.string.label_screenshot), R.drawable.background_take_screenshot, new ITouchAction() {
            @Override
            public void TouchAction() {
                try {
                    DataBaseMethod<Void> activityMetaSaving = DataBaseCRUD.getInstance().buildActivityMetaSaving();
                    new DataBaseTask.Builder()
                            .setCallback(TopButtonBarView.this)
                            .setMethod(activityMetaSaving)
                            .createAndExecute();
                    DataBaseMethod<Void> stepSaving = DataBaseCRUD.getInstance().buildStepSaving(++sStepNumber);
                    new DataBaseTask.Builder()
                            .setCallback(TopButtonBarView.this)
                            .setMethod(stepSaving)
                            .createAndExecute();
                } catch (PackageManager.NameNotFoundException e) {
                    Toast.makeText(getContext(), R.string.activity_info_unavailable, Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    Toast.makeText(getContext(), R.string.screenshot_saving_error, Toast.LENGTH_SHORT).show();
                }
                Intent intentHideView = new Intent(getContext(), TopButtonService.class).setAction(TopButtonService.ACTION_HIDE_VIEW);
                intentHideView.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().getApplicationContext().startService(intentHideView);

                Intent intentHelp = new Intent(getContext(), HelpDialogActivity.class);
                intentHelp.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().getApplicationContext().startActivity(intentHelp);
            }
        });
        mButtonActivityInfo = new TopUnitView(getContext(), getContext().getString(R.string.label_activity_info), R.drawable.background_activity_info, new ITouchAction() {
            @Override
            public void TouchAction() {
                String topActivityName = "Not found";
                try {
                    topActivityName = ActivityMetaUtil.getTopActivityInfo().name;
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                Toast.makeText(getContext(), topActivityName, Toast.LENGTH_SHORT).show();
            }
        });
        mButtonTakeStep = new TopUnitView(getContext(), getContext().getString(R.string.label_step_view), R.drawable.background_add_step, new ITouchAction() {
            @Override
            public void TouchAction() {
                Toast.makeText(getContext(), getContext().getString(R.string.label_screenshot) + " Vova what will be here?", Toast.LENGTH_LONG).show();
            }
        });
        mButtonShowSteps = new TopUnitView(getContext(), getContext().getString(R.string.label_show_steps), R.drawable.background_show_step, new ITouchAction() {
            @Override
            public void TouchAction() {
                getContext().getApplicationContext().startActivity(new Intent(getContext(), StepsActivity.class));
            }
        });
        mButtonStopRecord = new TopUnitView(getContext(), getContext().getString(R.string.label_cancel_record), R.drawable.background_stop_record, new ITouchAction() {
            @Override
            public void TouchAction() {
                isRecordStarted = false;
                hide();
                Toast.makeText(getContext(), getContext().getString(R.string.label_cancel_record), Toast.LENGTH_LONG).show();
            }
        });
        mButtonCloseApp = new TopUnitView(getContext(), getContext().getString(R.string.label_close_app), R.drawable.background_close, new ITouchAction() {
            @Override
            public void TouchAction() {
                TopButtonService.close(getContext());
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
        mWindowManager.updateViewLayout(this, mLayout);
    }

    private void setRecordButtons() {
        mButtonsBar.removeAllViews();
        mButtonsBar.addView(mButtonTakeScreenshot);
        mButtonsBar.addView(mButtonActivityInfo);
        mButtonsBar.addView(mButtonTakeStep);
        mButtonsBar.addView(mButtonExpectedResult);
        mButtonsBar.addView(mButtonShowSteps);
        mButtonsBar.addView(mButtonCreateTicket);
        mButtonsBar.addView(mButtonStopRecord);
        mButtonsBar.addView(mButtonOpenUserInfo);
        mWindowManager.updateViewLayout(this, mLayout);
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
                if (isRecordStarted) {
                    setRecordButtons();
                } else {
                    setInitialButtons();
                }
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

    public void move(int x, int y) {
        if (mButtonsBar.getVisibility() == VISIBLE) {
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
    }

    public static boolean isRecordStarted() {
        return isRecordStarted;
    }

    @Override
    public int getVisibility() {
        if (mButtonsBar != null) {
            return mButtonsBar.getVisibility();
        }
        return GONE;
    }

    //Database callback
    @Override
    public void onDataBaseRequestPerformed(DataBaseTask.DataBaseResponse dataBaseResponse) {
        Toast.makeText(getContext(), R.string.data_base_action_done, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDataBaseRequestError(Exception e) {
        Toast.makeText(getContext(), R.string.database_operation_error, Toast.LENGTH_SHORT).show();
    }

}
