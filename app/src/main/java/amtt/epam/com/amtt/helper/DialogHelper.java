package amtt.epam.com.amtt.helper;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.adapter.SharedFileAdapter;
import amtt.epam.com.amtt.util.FileUtil;
import amtt.epam.com.amtt.util.PreferenceUtil;
import amtt.epam.com.amtt.util.ZipUtil;

/**
 * @author Ivan_Bakach
 * @version on 05.08.2015
 */

public class DialogHelper {
    public interface IDialogButtonClick{
        void positiveButtonClick();
        void negativeButtonClick();
    }

    public interface IShareAction {
        void shareTo(String shareFolderName, ArrayList<String> sharedFilesWithoutFolder);
    }
    public static AlertDialog getAreYouSureDialog(final Activity activity, CharSequence title, CharSequence message, final IDialogButtonClick iDialogButtonClick){
        return new AlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(message)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        iDialogButtonClick.negativeButtonClick();
                    }
                })
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        iDialogButtonClick.positiveButtonClick();
                        dialog.dismiss();
                    }
                })
                .create();
    }

    public static AlertDialog getIsntSaveGifDialog(Activity activity){
        return new AlertDialog.Builder(activity)
                .setTitle(R.string.title_gif_isnt_saved)
                .setMessage(R.string.message_gif_isnt_saved)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
    }
    public static AlertDialog getGifInfoDialog(final Activity activity){
        return new AlertDialog.Builder(activity)
                .setTitle(R.string.title_gif_info)
                .setMessage(R.string.message_gif_info)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PreferenceUtil.putBoolean(activity.getString(R.string.key_gif_info_dialog), true);
                        dialog.dismiss();
                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        PreferenceUtil.putBoolean(activity.getString(R.string.key_gif_info_dialog), true);
                    }
                })
                .create();
    }

    public static AlertDialog getStepDeletionDialog(final Activity activity, final IDialogButtonClick iDialogButtonClick){
        LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = layoutInflater.inflate(R.layout.dialog_step_deletion, null);
        CheckBox doNotShowAgain = (CheckBox) dialogView.findViewById(R.id.cb_do_not_show_again);
        doNotShowAgain.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PreferenceUtil.putBoolean(activity.getString(R.string.key_step_deletion_dialog), isChecked);
            }
        });
        AlertDialog dialog = new AlertDialog.Builder(activity)
                .setTitle(R.string.title_step_deletion)
                .setMessage(R.string.message_step_deletion)
                .setView(dialogView)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        iDialogButtonClick.positiveButtonClick();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        iDialogButtonClick.negativeButtonClick();
                        dialog.dismiss();
                    }
                })
                .create();
        return dialog;
    }

    public static AlertDialog getClearEnvironmentDialog(Activity activity, final IDialogButtonClick iDialogButtonClick){
        AlertDialog dialog = new AlertDialog.Builder(activity)
                .setTitle(R.string.label_clear_environment)
                .setMessage(R.string.message_clear_environment)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        iDialogButtonClick.positiveButtonClick();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        return dialog;
    }

    public static AlertDialog getShareFileDialog(final Activity activity, final ArrayList<String> sharedFiles, final IShareAction shareAction) {
        final View view = ((LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.dialog_share_files, null);
        RecyclerView listShareFile = (RecyclerView) view.findViewById(R.id.rv_list_share);
        listShareFile.setLayoutManager(new LinearLayoutManager(activity));
        final ArrayList<String> sharedFilesWithoutFolder = new ArrayList<>(sharedFiles);
        final SharedFileAdapter sharedFileAdapter = new SharedFileAdapter((ArrayList<String>) sharedFiles.clone(), new SharedFileAdapter.IItemClickListener() {
            @Override
            public void onCheckFile(String checkedFile, boolean isChecked) {
                if (isChecked && !sharedFilesWithoutFolder.contains(checkedFile)) {
                    sharedFilesWithoutFolder.add(checkedFile);
                } else {
                    sharedFilesWithoutFolder.remove(checkedFile);
                }
            }
        });
        listShareFile.setAdapter(sharedFileAdapter);

        TextView emptyView = (TextView) view.findViewById(R.id.tv_empty_view);
        if (sharedFileAdapter.getItemCount() > 0) {
            emptyView.setVisibility(View.GONE);
        } else {
            emptyView.setVisibility(View.VISIBLE);
        }

        return new AlertDialog.Builder(activity)
                .setTitle(activity.getString(R.string.title_share_dialog))
                .setView(view)
                .setNegativeButton(activity.getString(R.string.cancel), null)
                .setPositiveButton(activity.getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (sharedFileAdapter.getItemCount() > 0) {
                            String folderName = "Share";
                            String shareFolderName = folderName + FileUtil.getCurrentTimeInFormat();
                            shareAction.shareTo(shareFolderName, sharedFilesWithoutFolder);
                            activity.finish();
                        } else {
                            Toast.makeText(activity, R.string.error_message_share_file, Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .create();
    }
}
