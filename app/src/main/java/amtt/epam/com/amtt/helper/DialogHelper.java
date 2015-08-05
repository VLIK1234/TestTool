package amtt.epam.com.amtt.helper;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.util.PreferenceUtil;

/**
 * @author Ivan_Bakach
 * @version on 05.08.2015
 */

public class DialogHelper {
    public interface IDialogButtonClick{
        void positiveButtonClick();
        void negativeButtonClick();
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
}
