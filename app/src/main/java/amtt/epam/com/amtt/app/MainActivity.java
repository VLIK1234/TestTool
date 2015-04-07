package amtt.epam.com.amtt.app;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.asynctask.ShowUserDataTask;
import amtt.epam.com.amtt.bo.issue.createmeta.JMetaResponse;
import amtt.epam.com.amtt.callbacks.ShowUserDataCallback;
import amtt.epam.com.amtt.crash.AmttExceptionHandler;
import amtt.epam.com.amtt.database.DbClearTask;
import amtt.epam.com.amtt.database.StepSavingCallback;
import amtt.epam.com.amtt.database.StepSavingResult;
import amtt.epam.com.amtt.database.StepSavingTask;
import amtt.epam.com.amtt.service.TopButtonService;
import amtt.epam.com.amtt.util.Converter;
import io.fabric.sdk.android.Fabric;


public class MainActivity extends BaseActivity implements StepSavingCallback, ShowUserDataCallback {
    private SharedPreferences sharedPreferences;
    private Boolean accessCreateIssue;
    private Button issueButton;
    private int mScreenNumber = 1;
    private boolean newStepsSequence = false;
    private static final String USER_NAME = "username";
    private static final String PASSWORD = "password";
    private static final String URL = "url";
    private static final String NAME_SP = "data";
    private static final String VOID = "";
    private static final String PROJECTS_NAMES = "projectsNames";
    private static final String ACCESS = "access";
    private static final String PROJECTS_KEYS = "projectsKeys";

    private PopupWindow mPopupWindow;
    private View mPopupLayout;
    private TextView mCrashHeadTextView;
    private TextView mCrashTextView;
    private String mCrashFilePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);

        startService(new Intent(this, TopButtonService.class));
        TopButtonService.show(this);
        Button loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);

            }
        });

        Button crashButton = (Button) findViewById(R.id.crash_button);
        crashButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                throw new IllegalStateException("stakkato caused crash");
            }
        });

        Thread.currentThread().setUncaughtExceptionHandler(new AmttExceptionHandler(this));


        Button clearDbbutton = (Button) findViewById(R.id.clear_db_button);
        clearDbbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DbClearTask(MainActivity.this).execute();
            }
        });
        sharedPreferences = getSharedPreferences(NAME_SP, MODE_PRIVATE);
        accessCreateIssue = sharedPreferences.getBoolean(ACCESS, false);
        issueButton = (Button) findViewById(R.id.issue_act_button);
        issueButton.setEnabled(accessCreateIssue);
        Button stepButton = (Button) findViewById(R.id.step_button);
        stepButton.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                              View rootView = getWindow().getDecorView();
                                              rootView.setDrawingCacheEnabled(true);
                                              Bitmap bitmap = rootView.getDrawingCache();
                                              Rect rect = new Rect();
                                              getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);

                                              new StepSavingTask(MainActivity.this, MainActivity.this, bitmap, rect, MainActivity.this.getComponentName(), newStepsSequence).execute();
                                              newStepsSequence = false;
                                          }
                                      }

        );

        Button clearDbButton = (Button) findViewById(R.id.clear_db_button);
        clearDbButton.setOnClickListener(new View.OnClickListener()

                                         {
                                             @Override
                                             public void onClick(View v) {
                                                 new DbClearTask(MainActivity.this).execute();
                                             }
                                         }

        );

        Button showStepsButton = (Button) findViewById(R.id.show_steps_button);
        showStepsButton.setOnClickListener(new View.OnClickListener()

                                           {
                                               @Override
                                               public void onClick(View v) {
                                                   startActivity(new Intent(MainActivity.this, StepsActivity.class));
                                                   newStepsSequence = true;
                                               }
                                           }

        );

        final Button showCrashInfoButton = (Button) findViewById(R.id.show_crash_info);
        showCrashInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPopupWindow();
            }
        });

        mCrashFilePath = getFilesDir().getPath() + "/crash.txt";
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        accessCreateIssue = sharedPreferences.getBoolean(ACCESS, false);
        issueButton = (Button) findViewById(R.id.issue_act_button);
        issueButton.setEnabled(accessCreateIssue);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return id == R.id.action_settings || super.onOptionsItemSelected(item);

    }

//    @Override
//    public void onImageSaved(ImageSavingResult result) {
//        mScreenNumber++;
//        int resultMessage = result == ImageSavingResult.ERROR ? R.string.image_saving_error : R.string.image_saving_success;
//        Toast.makeText(this, resultMessage, Toast.LENGTH_SHORT).show();
//    }

    @Override
    public int getScreenNumber() {
        return mScreenNumber;
    }


    @Override
    public void onStepSaved(StepSavingResult result) {
        int resultMessage = result == StepSavingResult.ERROR ? R.string.step_saving_error : R.string.step_saving_success;
        Toast.makeText(this, resultMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void incrementScreenNumber() {
        mScreenNumber++;
    }

    public void onIssueClick(View view) {
        String username, password, url;
        username = sharedPreferences.getString(USER_NAME, VOID);
        password = sharedPreferences.getString(PASSWORD, VOID);
        url = sharedPreferences.getString(URL, VOID);
        new ShowUserDataTask(username, password, url, MainActivity.this).execute();
    }


    @Override
    public void onShowUserDataResult(JMetaResponse result) {
        ArrayList<String> projectsNames = result.getProjectsNames();
        ArrayList<String> projectsKeys = result.getProjectsKeys();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(PROJECTS_NAMES, Converter.arrayListToSet(projectsNames));
        editor.putStringSet(PROJECTS_KEYS, Converter.arrayListToSet(projectsKeys));
        editor.apply();
        startActivity(new Intent(this, CreateIssueActivity.class));
    }

    private void setPopupWindow() {
        if (mPopupWindow == null) {
            setPopupLayout();
        }

        if (setPopupCrashText()) {
            return;
        }

        setPopupLayoutDimension();
    }

    private void setPopupLayout() {
        ViewGroup viewGroup = (LinearLayout) findViewById(R.id.popup_window);
        mPopupLayout = LayoutInflater.from(MainActivity.this).inflate(R.layout.popup_window_crash_info, viewGroup);
        mPopupWindow = new PopupWindow(mPopupLayout, ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT, true);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.popup_window_drawable));

        Button popupCloseButton = (Button) mPopupLayout.findViewById(R.id.close_button);
        popupCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
            }
        });

        Button popupDeleteButton = (Button) mPopupLayout.findViewById(R.id.delete_crash_button);
        popupDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File crashFile = new File(mCrashFilePath);
                crashFile.delete();
                mPopupWindow.dismiss();
            }
        });

        mCrashHeadTextView = (TextView) mPopupLayout.findViewById(R.id.crash_text_head);
        mCrashTextView = (TextView) mPopupLayout.findViewById(R.id.crash_text);
    }

    private boolean setPopupCrashText() {
        String crashHeadText;
        StringBuilder crashText = new StringBuilder();
        String buffer;
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(mCrashFilePath));
            crashHeadText = bufferedReader.readLine();
            while ((buffer = bufferedReader.readLine()) != null) {
                crashText.append(buffer);
            }
        } catch (IOException e) {
            Toast.makeText(MainActivity.this, R.string.lack_of_crashes_text, Toast.LENGTH_SHORT).show();
            return true;
        }

        mCrashHeadTextView.setText(crashHeadText);
        mCrashTextView.setText(crashText);
        return false;
    }

    private void setPopupLayoutDimension() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        mPopupLayout.findViewById(R.id.scroll_container).setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height / 2));
        mPopupWindow.showAtLocation(mPopupLayout, Gravity.CENTER, 0, 0);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams((int)(width * 0.9), ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupLayout.setLayoutParams(layoutParams);
    }

}
