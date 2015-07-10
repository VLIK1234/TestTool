package amtt.epam.com.amtt.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import amtt.epam.com.amtt.AmttApplication;
import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.bo.database.Step;
import amtt.epam.com.amtt.bo.ticket.Attachment;
import amtt.epam.com.amtt.database.object.DbObjectManager;
import amtt.epam.com.amtt.ui.activities.PreviewActivity;
import amtt.epam.com.amtt.util.Logger;
import amtt.epam.com.amtt.util.PreferenceUtils;

/**
 * @author Iryna Monchanka
 * @version on 27.05.2015
 */

public class AttachmentAdapter extends RecyclerView.Adapter<AttachmentAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView mScreenshotImage;
        public TextView mScreenshotName;
        public ImageView mCloseImage;
        private AttachmentAdapter mAdapter;

        public ViewHolder(View itemView, AttachmentAdapter adapter) {
            super(itemView);
            mScreenshotImage = (ImageView) itemView.findViewById(R.id.iv_screenImage);
            mScreenshotImage.setOnClickListener(this);
            mScreenshotName = (TextView) itemView.findViewById(R.id.tv_screenName);
            mCloseImage = (ImageView) itemView.findViewById(R.id.iv_close);
            mCloseImage.setOnClickListener(this);
            mAdapter = adapter;
        }

        @Override
        public void onClick(View v) {
            if (mAdapter != null) {
                switch (v.getId()) {
                    case R.id.iv_close:
                        mAdapter.removeItem(getAdapterPosition());
                        break;
                    case R.id.iv_screenImage:
                        mAdapter.showItem(getAdapterPosition());
                        break;
                }
            }

        }

    }

    public interface AttachmentRemovalListener {

        void onItemRemoved(int stepId, boolean isStepWithActivityInfo);

    }

    private List<Attachment> mAttachments;
    private int mRowLayout;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private AttachmentRemovalListener mListener;

    public AttachmentAdapter(Context context, List<Attachment> screenshots, int rowLayout) {
        mContext = context;
        mAttachments = screenshots;
        mRowLayout = rowLayout;
        mListener = (AttachmentRemovalListener)mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        viewGroup.setHorizontalScrollBarEnabled(true);
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(mRowLayout, viewGroup, false);
        return new ViewHolder(v, this);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Attachment screenshot = mAttachments.get(i);
        viewHolder.mScreenshotName.setText(screenshot.mFileName);
        if (screenshot.mFilePath.contains(".png")) {
            ImageLoader.getInstance().displayImage("file:///" + screenshot.mFilePath, viewHolder.mScreenshotImage);
        } else if (screenshot.mFilePath.contains(".txt")) {
            viewHolder.mScreenshotImage.setImageDrawable(AmttApplication.getContext().getResources().getDrawable(R.drawable.text_file_preview));
        }

        viewHolder.mCloseImage.setEnabled(true);
    }

    @Override
    public int getItemCount() {
        return mAttachments == null ? 0 : mAttachments.size();
    }

    private void removeItem(final int position) {
        if (mLayoutInflater == null) {
            mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if (!PreferenceUtils.getBoolean(mContext.getString(R.string.key_step_deletion_dialog))) {
            View dialogView = mLayoutInflater.inflate(R.layout.dialog_step_deletion, null);
            CheckBox doNotShowAgain = (CheckBox) dialogView.findViewById(R.id.cb_do_not_show_again);
            doNotShowAgain.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    PreferenceUtils.putBoolean(mContext.getString(R.string.key_step_deletion_dialog), isChecked);
                }
            });

            new AlertDialog.Builder(mContext)
                    .setTitle(R.string.title_step_deletion)
                    .setMessage(R.string.message_step_deletion)
                    .setView(dialogView)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            removeStepFromDatabase(position);
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create()
                    .show();
        } else {
            removeStepFromDatabase(position);
        }
    }

    private void showItem(final int position) {
        Intent preview = new Intent(mContext, PreviewActivity.class);
        preview.putExtra(PreviewActivity.FILE_PATH, mAttachments.get(position).mFilePath);
        mContext.startActivity(preview);
    }

    private void removeStepFromDatabase(int position) {
        int stepId = mAttachments.get(position).mStepId;
        boolean isStepWithActivityInfo = mAttachments.get(position).isStepWithActivityInfo;
        DbObjectManager.INSTANCE.remove(new Step(stepId));
        mAttachments.remove(position);
        notifyItemRemoved(position);

        if (mListener != null) {
            mListener.onItemRemoved(stepId, isStepWithActivityInfo);
        }
    }

    public ArrayList<String> getAttachmentFilePathList() {
        ArrayList<String> filePathList = new ArrayList<>();
        for (Attachment attachment : mAttachments) {
            filePathList.add(attachment.mFilePath);
        }
        return filePathList;
    }

}
