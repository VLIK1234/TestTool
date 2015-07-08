package amtt.epam.com.amtt.adapter;

import android.database.ContentObserver;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

import amtt.epam.com.amtt.AmttApplication;
import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.bo.database.Step;
import amtt.epam.com.amtt.bo.database.Step.ScreenshotState;
import amtt.epam.com.amtt.bo.ticket.Attachment;
import amtt.epam.com.amtt.contentprovider.AmttUri;
import amtt.epam.com.amtt.database.object.DatabaseEntity;
import amtt.epam.com.amtt.database.object.DbObjectManager;
import amtt.epam.com.amtt.database.object.IResult;
import amtt.epam.com.amtt.http.MimeType;
import amtt.epam.com.amtt.util.AttachmentManager;
import amtt.epam.com.amtt.util.Logger;

/**
 * @author Iryna Monchanka
 * @version on 27.05.2015
 */

public class AttachmentAdapter extends RecyclerView.Adapter<AttachmentAdapter.ViewHolder> implements IResult<List<DatabaseEntity>> {

    private static class StepScreenshotObserver extends ContentObserver {

        private AttachmentAdapter mAdapter;

        public StepScreenshotObserver(Handler handler, AttachmentAdapter attachmentAdapter) {
            super(handler);
            mAdapter = attachmentAdapter;
        }

        @Override
        public boolean deliverSelfNotifications() {
            return super.deliverSelfNotifications();
        }

        @Override
        public void onChange(boolean selfChange) {
            onChange(selfChange, null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            mAdapter.reloadData();
        }

    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView mScreenshotImage;
        public TextView mScreenshotName;
        public ImageView mScreenshotClose;
        public ProgressBar mProgress;
        private ClickListener mListener;

        public ViewHolder(View itemView, ClickListener listener) {
            super(itemView);
            mScreenshotImage = (ImageView) itemView.findViewById(R.id.iv_screenImage);
            mScreenshotImage.setOnClickListener(this);
            mScreenshotName = (TextView) itemView.findViewById(R.id.tv_screenName);
            mScreenshotClose = (ImageView) itemView.findViewById(R.id.iv_close);
            mProgress = (ProgressBar) itemView.findViewById(android.R.id.progress);
            mListener = listener;
            mScreenshotClose.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_close:
                    if (mListener != null) {
                        mListener.onItemRemove(getAdapterPosition());
                    }
                    break;
                case R.id.iv_screenImage:
                    if (mListener != null) {
                        mListener.onItemShow(getAdapterPosition());
                    }
                    break;

            }
        }

        public interface ClickListener {
            void onItemRemove(int position);

            void onItemShow(int position);
        }

    }

    private final String TAG = this.getClass().getSimpleName();

    private List<Attachment> mAttachments;
    private int mRowLayout;
    private ViewHolder.ClickListener mClickListener;

    public AttachmentAdapter(List<Attachment> attachments, int rowLayout, ViewHolder.ClickListener clickListener) {
        mAttachments = attachments;
        mRowLayout = rowLayout;
        mClickListener = clickListener;
        AmttApplication.getContext().getContentResolver().registerContentObserver(AmttUri.STEP.get(), true, new StepScreenshotObserver(new Handler(), this));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        viewGroup.setHorizontalScrollBarEnabled(true);
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(mRowLayout, viewGroup, false);
        return new ViewHolder(v, mClickListener);
    }

    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        Attachment attachment = mAttachments.get(i);
        Logger.d(TAG, attachment.mName);
        viewHolder.mScreenshotName.setText(attachment.mName);
        if (attachment.mFilePath.contains(MimeType.IMAGE_PNG.getFileExtension()) ||
                attachment.mFilePath.contains(MimeType.IMAGE_JPG.getFileExtension()) ||
                attachment.mFilePath.contains(MimeType.IMAGE_JPEG.getFileExtension())) {
            if (attachment.mStepScreenshotState == ScreenshotState.WRITTEN) {
                if (viewHolder.mScreenshotImage.getDrawable() == null) {
                    ImageLoader.getInstance().displayImage("file:///" + attachment.mFilePath, viewHolder.mScreenshotImage, new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {
                            viewHolder.mProgress.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            viewHolder.mProgress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onLoadingCancelled(String imageUri, View view) {

                        }
                    });
                }
            } else {
                viewHolder.mProgress.setVisibility(View.VISIBLE);
            }
        } else if (attachment.mFilePath.contains(MimeType.TEXT_PLAIN.getFileExtension())) {
            viewHolder.mScreenshotImage.setImageDrawable(AmttApplication.getContext().getResources().getDrawable(R.drawable.text_file_preview));
        }

        viewHolder.mScreenshotClose.setEnabled(true);
    }

    @Override
    public int getItemCount() {
        return mAttachments == null ? 0 : mAttachments.size();
    }

    public void addItem(int position, Attachment data) {
        mAttachments.add(position, data);
        notifyItemInserted(position);
    }

    public void removeItem(int position) {
        mAttachments.remove(position);
        notifyItemRemoved(position);
        Logger.d(TAG, String.valueOf(position));
    }

    public ArrayList<String> getAttachmentFilePathList() {
        ArrayList<String> filePathList = new ArrayList<>();
        for (Attachment attachment : mAttachments) {
            filePathList.add(attachment.mFilePath);
        }
        return filePathList;
    }

    public String getStepId(int position) {
        return mAttachments.get(position).mStepId;
    }

    private void reloadData() {
        DbObjectManager.INSTANCE.getAll(new Step(), this);
    }

    //Callbacks
    //IResult
    @Override
    public void onResult(List<DatabaseEntity> result) {
        mAttachments = AttachmentManager.getInstance().getAttachmentList(result);
        notifyDataSetChanged();
    }

    @Override
    public void onError(Exception e) {

    }

}
