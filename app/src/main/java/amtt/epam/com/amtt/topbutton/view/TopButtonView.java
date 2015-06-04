package amtt.epam.com.amtt.topbutton.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.app.CreateIssueActivity;
import amtt.epam.com.amtt.app.HelpDialogActivity;
import amtt.epam.com.amtt.app.StepsActivity;
import amtt.epam.com.amtt.app.UserInfoActivity;
import amtt.epam.com.amtt.util.StepUtil;
import amtt.epam.com.amtt.topbutton.service.TopButtonService;
import amtt.epam.com.amtt.util.ActivityMetaUtil;

/**
 @author Ivan_Bakach
 @version on 23.03.2015
 */

public class TopButtonView extends FrameLayout{

    private final static String LOG_TAG = "TAG";

    private WindowManager windowManager;
    private WindowManager.LayoutParams layoutParams;
    public LinearLayout buttonsBar;
    public ImageButton mainButton;
    private DisplayMetrics metrics;
    private int currentOrientation;
    private float widthProportion;
    private float heightProportion;

    private int firstX;
    private int firstY;
    private int lastX;
    private int lastY;
    public boolean moving;
    private int xButton = 0;
    private int yButton = 0;
    private static final int threshold = 10;

    private RelativeLayout topButtonLayout;
    private static boolean isRecordStarted = false;

    private TopUnitView startRecordView;
    private TopUnitView createTicketView;
    private TopUnitView openAmttView;
    private TopUnitView expectedResultView;
    private TopUnitView screenshotView;
    private TopUnitView activityInfoView;
    private TopUnitView stepView;
    private TopUnitView cancelRecordView;
    private TopUnitView mCloseApp;

    //Database fields
    private static int sStepNumber; //responsible for steps ordering in database

    public TopButtonView(Context context, WindowManager.LayoutParams layoutParams) {
        super(context);
        initComponent();
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        metrics = getContext().getResources().getDisplayMetrics();
        currentOrientation = getResources().getConfiguration().orientation;
        this.layoutParams = layoutParams;
        widthProportion = (float) layoutParams.x / metrics.widthPixels;
        heightProportion = (float) layoutParams.y / metrics.heightPixels;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @SuppressWarnings("unchecked")
    private void initComponent() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.top_button_layout, this, true);
        topButtonLayout = (RelativeLayout) findViewById(R.id.top_button_layout);
        buttonsBar = (LinearLayout) findViewById(R.id.buttons_bar);
        mainButton = (ImageButton) findViewById(R.id.main_button);
        buttonsBar.setOrientation(LinearLayout.VERTICAL);
        buttonsBar.setVisibility(GONE);
        startRecordView = new TopUnitView(getContext(), getContext().getString(R.string.label_start_record), R.drawable.background_start_record, new ITouchAction() {
            @Override
            public void TouchAction() {
                TopButtonView.setStartRecord(true);
                StepUtil.clearAllStep();
                Toast.makeText(getContext(), getContext().getString(R.string.label_start_record), Toast.LENGTH_LONG).show();
            }
        });
        createTicketView = new TopUnitView(getContext(), getContext().getString(R.string.label_create_ticket), R.drawable.background_create_ticket, new ITouchAction() {
            @Override
            public void TouchAction() {
                Toast.makeText(getContext(), getContext().getString(R.string.label_create_ticket), Toast.LENGTH_LONG).show();
                Intent intentTicket = new Intent(getContext(), CreateIssueActivity.class);
                intentTicket.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().getApplicationContext().startActivity(intentTicket);
            }
        });
        openAmttView = new TopUnitView(getContext(), getContext().getString(R.string.label_open_amtt), R.drawable.background_user_info, new ITouchAction() {
            @Override
            public void TouchAction() {
                Intent userInfoIntent = new Intent(getContext(), UserInfoActivity.class);
                userInfoIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().getApplicationContext().startActivity(userInfoIntent);
            }
        });
        expectedResultView = new TopUnitView(getContext(), getContext().getString(R.string.label_expected_result), R.drawable.background_expected_result, new ITouchAction() {
            @Override
            public void TouchAction() {
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SHOW");
                broadcastIntent.addCategory("android.intent.category.DEFAULT");
                getContext().sendBroadcast(broadcastIntent);
                broadcastIntent.setAction("LOG");
                getContext().sendBroadcast(broadcastIntent);
//                Toast.makeText(getContext(), getContext().getString(R.string.label_expected_result), Toast.LENGTH_LONG).show();
            }
        });
        screenshotView = new TopUnitView(getContext(), getContext().getString(R.string.label_screenshot), R.drawable.background_take_screenshot, new ITouchAction() {
            @Override
            public void TouchAction() {
                try {
                    StepUtil.saveActivityMeta(ActivityMetaUtil.createMeta());
                } catch (NameNotFoundException e) {
                    Toast.makeText(getContext(), R.string.activity_info_unavailable, Toast.LENGTH_SHORT).show();
                }
                Intent intentHideView = new Intent(getContext(), TopButtonService.class).setAction(TopButtonService.ACTION_HIDE_VIEW);
                intentHideView.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().getApplicationContext().startService(intentHideView);

                Intent intentHelp = new Intent(getContext(), HelpDialogActivity.class);
                intentHelp.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().getApplicationContext().startActivity(intentHelp);
            }
        });
        activityInfoView = new TopUnitView(getContext(), getContext().getString(R.string.label_activity_info),R.drawable.background_activity_info, new ITouchAction() {
            @Override
            public void TouchAction() {
                Toast.makeText(getContext(), getContext().getString(R.string.label_activity_info), Toast.LENGTH_LONG).show();
                ScheduledExecutorService worker =
                        Executors.newSingleThreadScheduledExecutor();
                Runnable task = new Runnable() {
                    public void run() {
                        try {
                            StepUtil.saveActivityMeta(ActivityMetaUtil.createMeta());
                        } catch (NameNotFoundException e) {
                            Toast.makeText(getContext(), R.string.activity_info_unavailable, Toast.LENGTH_SHORT).show();
                        }
                        StepUtil.saveStep(ActivityMetaUtil.getTopActivityComponent(), null);
                    }
                };
                worker.schedule(task, 1, TimeUnit.SECONDS);
            }
        });
        stepView = new TopUnitView(getContext(), getContext().getString(R.string.label_step_view), R.drawable.background_add_step, new ITouchAction() {
            @Override
            public void TouchAction() {
                Toast.makeText(getContext(), getContext().getString(R.string.label_screenshot) + " Vova what will be here?", Toast.LENGTH_LONG).show();
                Intent intentStep = new Intent(getContext(), StepsActivity.class);
                intentStep.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().getApplicationContext().startActivity(intentStep);
            }
        });
        cancelRecordView = new TopUnitView(getContext(), getContext().getString(R.string.label_cancel_record), R.drawable.background_stop_record, new ITouchAction() {
            @Override
            public void TouchAction() {
                TopButtonView.setStartRecord(false);
                StepUtil.clearAllStep();
                Toast.makeText(getContext(), getContext().getString(R.string.label_cancel_record), Toast.LENGTH_LONG).show();
            }
        });
        mCloseApp = new TopUnitView(getContext(), getContext().getString(R.string.label_close_app), R.drawable.background_close, new ITouchAction() {
            @Override
            public void TouchAction() {
                TopButtonService.close(getContext());
            }
        });
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
                            playRotateAnimationMainButton(300, 180, 0);
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
                            if (!isRecordStarted) {
                                startRecordState();
                            } else {
                                cancelRecordState();
                            }
                            buttonsBar.setVisibility(VISIBLE);
                            xButton = layoutParams.x;
                            yButton = layoutParams.y;
                            playRotateAnimationMainButton(300, 0, 180);
                            Animation translate = AnimationUtils.loadAnimation(getContext(), R.anim.translate);
                            buttonsBar.startAnimation(translate);
                            checkFreeSpace();
                        }
                    }
                }
                break;
        }
        return true;
    }

    private void setBackgroundCompat(View view, Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(drawable);
        } else {
            view.setBackgroundDrawable(drawable);
        }
    }

    private void playRotateAnimationMainButton(int duration, int fromAngle, int toAngle) {
        AnimatorSet expand = new AnimatorSet().setDuration(duration);
        LayerDrawable layerDrawable = (LayerDrawable) getResources().getDrawable(R.drawable.background_main_button);
        RotatingDrawable drawable = new RotatingDrawable(layerDrawable.findDrawableByLayerId(R.id.main_button_background));
        ObjectAnimator animator = ObjectAnimator.ofFloat(drawable, "rotation", fromAngle, toAngle);
        animator.start();
        expand.play(animator);
        layerDrawable.setDrawableByLayerId(R.id.main_button_background, drawable);
        setBackgroundCompat(mainButton, layerDrawable);
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation != currentOrientation) {
            savePositionAfterTurnScreen();
            buttonsBar.setVisibility(GONE);
        }
    }

    private static class RotatingDrawable extends LayerDrawable {
        public RotatingDrawable(Drawable drawable) {
            super(new Drawable[]{drawable});
        }

        private float mRotation;

        @SuppressWarnings("UnusedDeclaration")
        public float getRotation() {
            return mRotation;
        }

        @SuppressWarnings("UnusedDeclaration")
        public void setRotation(float rotation) {
            mRotation = rotation;
            invalidateSelf();
        }

        @Override
        public void draw(Canvas canvas) {
            canvas.save();
            canvas.rotate(mRotation, getBounds().centerX(), getBounds().centerY());
            super.draw(canvas);
            canvas.restore();
        }
    }

    public static void setStartRecord(boolean isStartRecord) {
        TopButtonView.isRecordStarted = isStartRecord;
    }

    public static boolean getStartRecord() {
        return isRecordStarted;
    }

    public void startRecordState() {
        buttonsBar.removeAllViews();
        buttonsBar.addView(startRecordView);
        buttonsBar.addView(createTicketView);
        buttonsBar.addView(expectedResultView);
        buttonsBar.addView(openAmttView);
        buttonsBar.addView(mCloseApp);
    }

    public void cancelRecordState() {
        buttonsBar.removeAllViews();
        buttonsBar.addView(screenshotView);
        buttonsBar.addView(activityInfoView);
        buttonsBar.addView(stepView);
        buttonsBar.addView(expectedResultView);
        buttonsBar.addView(createTicketView);
        buttonsBar.addView(cancelRecordView);
        buttonsBar.addView(openAmttView);
    }

}
