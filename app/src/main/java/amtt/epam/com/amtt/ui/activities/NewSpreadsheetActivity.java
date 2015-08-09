package amtt.epam.com.amtt.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.api.GetContentCallback;
import amtt.epam.com.amtt.api.loadcontent.ContentFromDatabase;
import amtt.epam.com.amtt.bo.user.JUserInfo;
import amtt.epam.com.amtt.database.object.IResult;
import amtt.epam.com.amtt.database.util.StepUtil;
import amtt.epam.com.amtt.googleapi.api.GoogleApiConst;
import amtt.epam.com.amtt.googleapi.api.loadcontent.GSpreadsheetContent;
import amtt.epam.com.amtt.googleapi.bo.GSpreadsheet;
import amtt.epam.com.amtt.ui.views.TextInput;
import amtt.epam.com.amtt.util.ActiveUser;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_spreadsheet);
        initViews();
    }

    private void initViews(){
        mSpreadsheetKeyTextInput = (TextInput) findViewById(R.id.ti_spreadsheet_key);
        mSpreadsheetKeyTextInput.setText("14QM-1dtNoqjcxD4pQen0MvTYyOujbhYxwk4mq29jPfM");
        Button checkKeyButton = (Button) findViewById(R.id.btn_check_spreadsheet_key);
        checkKeyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key = mSpreadsheetKeyTextInput.getText().toString();
                if (!key.equals("")) {
                    mIdLink = GoogleApiConst.SPREADSHEET_PATH + key + GoogleApiConst.PATH_POSTFIX;
                    GSpreadsheetContent.getInstance().getSpreadsheet(mIdLink, new GetContentCallback<GSpreadsheet>() {
                        @Override
                        public void resultOfDataLoading(final GSpreadsheet result) {
                            NewSpreadsheetActivity.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    if (result != null) {
                                        ActiveUser.getInstance().setSpreadsheetLink(mIdLink);
                                        mAddSpreadsheet.setEnabled(true);
                                    } else {
                                        Toast.makeText(NewSpreadsheetActivity.this, "Invalide key", Toast.LENGTH_LONG).show();
                                        mAddSpreadsheet.setEnabled(false);
                                    }
                                }
                            });
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
                    StepUtil.checkUser(ActiveUser.getInstance().getUserName(), ActiveUser.getInstance().getUrl(), new IResult<List<JUserInfo>>() {
                        @Override
                        public void onResult(List<JUserInfo> result) {
                            for (JUserInfo userInfo : result) {
                                if (userInfo.getName().equals(ActiveUser.getInstance().getUserName()) && userInfo.getUrl().equals(ActiveUser.getInstance().getUrl())) {
                                    JUserInfo user = result.get(0);
                                    user.setLastSpreadsheetUrl(ActiveUser.getInstance().getSpreadsheetLink());
                                    ContentFromDatabase.updateUser(user, new IResult<Integer>() {
                                        @Override
                                        public void onResult(final Integer res) {
                                            NewSpreadsheetActivity.this.runOnUiThread(new Runnable() {
                                                public void run() {
                                                    if (res >= 0) {
                                                        Intent intent = new Intent();
                                                        setResult(RESULT_OK, intent);
                                                        finish();
                                                    } else {
                                                        Logger.e(TAG, "Error update user");
                                                    }
                                                }
                                            });
                                        }

                                        @Override
                                        public void onError(Exception e) {
                                            Logger.e(TAG, e.getMessage(), e);
                                        }
                                    });
                                }
                            }
                        }

                        @Override
                        public void onError(Exception e) {
                            Logger.e(TAG, e.getMessage(), e);
                        }
                    });
                }
            }
        });
    }
}
