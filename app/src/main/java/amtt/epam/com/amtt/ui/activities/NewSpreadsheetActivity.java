package amtt.epam.com.amtt.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.api.GetContentCallback;
import amtt.epam.com.amtt.bo.user.JUserInfo;
import amtt.epam.com.amtt.common.Callback;
import amtt.epam.com.amtt.database.util.LocalContent;
import amtt.epam.com.amtt.googleapi.api.GoogleApiConst;
import amtt.epam.com.amtt.googleapi.api.loadcontent.GSpreadsheetContent;
import amtt.epam.com.amtt.googleapi.bo.GSpreadsheet;
import amtt.epam.com.amtt.topbutton.service.TopButtonService;
import amtt.epam.com.amtt.ui.views.TextInput;
import amtt.epam.com.amtt.util.ActiveUser;
import amtt.epam.com.amtt.util.InputsUtil;
import amtt.epam.com.amtt.util.Logger;

/**
 * @author Iryna Monchanka
 * @version on 06.08.2015
 */

public class NewSpreadsheetActivity extends BaseActivity {

    private static final String TAG = NewSpreadsheetActivity.class.getSimpleName();
    private TextInput mSpreadsheetKeyTextInput;
    private Button mAddSpreadsheet;
    private String mIdLink;
    private ActiveUser mUser = ActiveUser.getInstance();
    private GSpreadsheetContent mSpreadsheetContent = GSpreadsheetContent.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_spreadsheet);
        TopButtonService.sendActionChangeTopButtonVisibility(false);
        initViews();
    }

    @Override
    protected void onPause() {
        super.onPause();
        TopButtonService.sendActionChangeTopButtonVisibility(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        TopButtonService.sendActionChangeTopButtonVisibility(false);
    }

    private void initViews(){
        mSpreadsheetKeyTextInput = (TextInput) findViewById(R.id.ti_spreadsheet_key);
        mSpreadsheetKeyTextInput.setText("14QM-1dtNoqjcxD4pQen0MvTYyOujbhYxwk4mq29jPfM");
        Button checkKeyButton = (Button) findViewById(R.id.btn_check_spreadsheet_key);
        checkKeyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key = mSpreadsheetKeyTextInput.getText().toString();
                if (!InputsUtil.isEmpty(key)) {
                    showProgress(true);
                    mIdLink = GoogleApiConst.SPREADSHEET_PATH + key + GoogleApiConst.PATH_POSTFIX;
                    mSpreadsheetContent.getSpreadsheet(mIdLink, new GetContentCallback<GSpreadsheet>() {
                        @Override
                        public void resultOfDataLoading(final GSpreadsheet result) {
                            if (result != null) {
                                mUser.setSpreadsheetLink(mIdLink);
                                mAddSpreadsheet.setEnabled(true);
                            } else {
                                Toast.makeText(NewSpreadsheetActivity.this, "Invalide key", Toast.LENGTH_LONG).show();
                                mAddSpreadsheet.setEnabled(false);
                            }
                            showProgress(false);
                        }
                    });
                }
            }
        });
        mAddSpreadsheet = (Button) findViewById(R.id.btn_add_spreadsheet_key);
        mAddSpreadsheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIdLink != null) {
                    showProgress(true);
                    LocalContent.checkUser(mUser.getUserName(), mUser.getUrl(), new Callback<List<JUserInfo>>() {
                        @Override
                        public void onLoadStart() {}

                        @Override
                        public void onLoadExecuted(List<JUserInfo> users) {
                            for (JUserInfo userInfo : users) {
                                if (userInfo.getName().equals(mUser.getUserName()) && userInfo.getUrl().equals(mUser.getUrl())) {
                                    JUserInfo user = users.get(0);
                                    user.setLastSpreadsheetUrl(mUser.getSpreadsheetLink());
                                    LocalContent.updateUser(mUser.getId(), user, new Callback<Integer>() {
                                        @Override
                                        public void onLoadStart() {}

                                        @Override
                                        public void onLoadExecuted(final Integer integer) {
                                            if (integer >= 0) {
                                                new Handler().postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Intent intent = new Intent();
                                                        setResult(RESULT_OK, intent);
                                                        showProgress(false);
                                                        finish();
                                                    }
                                                }, 1000);
                                            } else {
                                                Logger.e(TAG, "Error update user");
                                            }
                                        }

                                        @Override
                                        public void onLoadError(Exception e) {
                                            Logger.e(TAG, e.getMessage(), e);
                                        }
                                    });
                                }
                            }
                        }

                        @Override
                        public void onLoadError(Exception e) {
                            Logger.e(TAG, e.getMessage(), e);
                        }
                    });
                }
            }
        });
    }
}
