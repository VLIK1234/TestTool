package amtt.epam.com.amtt.ui.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.database.util.LocalContent;
import amtt.epam.com.amtt.topbutton.service.TopButtonService;
import amtt.epam.com.amtt.topbutton.view.TopButtonBarView;
import amtt.epam.com.amtt.util.ActiveUser;
import amtt.epam.com.amtt.util.FileUtil;

/**
 * @author IvanBakach
 * @version on 21.09.2015
 */
public class TaskNameActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final View view = ((LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.dialog_edit_text, null);
        final EditText editDrawText = (EditText) view.findViewById(R.id.et_draw_text);
        final AlertDialog alertDialog = new AlertDialog.Builder(TaskNameActivity.this)
                .setTitle(getString(R.string.titke_task_name_dialog))
                .setView(view)
                .setPositiveButton(R.string.ok, null)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        TopButtonService.sendActionChangeTopButtonVisibility(true);
                    }
                })
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        finish();
                        TopButtonService.sendActionChangeTopButtonVisibility(true);
                    }
                })
                .create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button okButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int haveReservedChars = -1;
                        String taskName = String.valueOf(editDrawText.getText());
                        final String[] reservedChars = {"|", "\\", "/", "?", "*", "<", "\"", ":", ">"};
                        for (String reserved: reservedChars) {
                            haveReservedChars = Math.max(taskName.indexOf(reserved), haveReservedChars);
                        }
                        if (!TextUtils.isEmpty(taskName)&& haveReservedChars==-1) {
                            TopButtonBarView.setIsRecordStarted(true);
                            FileUtil.setTaskName(taskName);
                            ActiveUser.getInstance().setRecord(true);
                            LocalContent.removeAllSteps();

                            Intent intentLogs = new Intent();
                            intentLogs.setAction("TAKE_LOGS");
                            getBaseContext().sendOrderedBroadcast(intentLogs, null);
                            Toast.makeText(getBaseContext(), getString(R.string.label_start_record), Toast.LENGTH_SHORT).show();
                            alertDialog.dismiss();
                        } else if (haveReservedChars!=-1) {
                            Toast.makeText(getBaseContext(), R.string.message_resolved_char_task_name_dialog, Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(getBaseContext(), R.string.message_empty_task_name_dialog, Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        });
        alertDialog.show();
    }
}
