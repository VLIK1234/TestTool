package amtt.epam.com.amtt.adapter;

import android.content.Context;
import android.graphics.Bitmap;
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

import amtt.epam.com.amtt.CoreApplication;
import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.adapter.contentobserver.StepScreenshotObserver;
import amtt.epam.com.amtt.bo.ticket.Step.ScreenshotState;
import amtt.epam.com.amtt.bo.ticket.Attachment;
import amtt.epam.com.amtt.contentprovider.LocalUri;
import amtt.epam.com.amtt.http.MimeType;
import amtt.epam.com.amtt.util.Logger;

/**
 * @author Iryna Monchanka
 * @version on 27.05.2015
 */
public class AttachmentAdapter extends RecyclerView.Adapter<AttachmentAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public interface ClickListener {

            void onItemRemove(int position);

            void onItemShow(int position);

        }

        public interface DataChangedListener{
            void onReloadData();
        }

        public interface ScreenshotStateListener{
            void onShowMessage();
        }

        public final ImageView mScreenshotImage;
        public final TextView mScreenshotName;
        public final ImageView mScreenshotClose;
        public final ProgressBar mProgress;
        private ScreenshotState mScreenshotState;
        private final Context mContext;
        private ClickListener mListener;
        private DataChangedListener mDataChangedListener;
        private ScreenshotStateListener mStateListener;

        public ViewHolder(Context context, View itemView) {
            super(itemView);
            mContext = context;
            mScreenshotImage = (ImageView) itemView.findViewById(R.id.iv_screenImage);
            mScreenshotImage.setOnClickListener(this);
            mScreenshotName = (TextView) itemView.findViewById(R.id.tv_screenName);
            mScreenshotClose = (ImageView) itemView.findViewById(R.id.iv_close);
            mProgress = (ProgressBar) itemView.findViewById(android.R.id.progress);
            mScreenshotClose.setOnClickListener(this);
        }

        public void setClickListener(ClickListener clickListener) {
            mListener = clickListener;
        }

        public void setDataChangedListener(DataChangedListener dataChangedListener) {
            mDataChangedListener = dataChangedListener;
        }

        public void setScreenshotStateListener(ScreenshotStateListener screenshotStateListener) {
            mStateListener = screenshotStateListener;
        }

        @Override
        public void onClick(View v) {
            if (mScreenshotState == ScreenshotState.IS_BEING_WRITTEN) {
                mStateListener.onShowMessage();
            }
            if (mListener != null) {
                switch (v.getId()) {
                    case R.id.iv_close:
                        mListener.onItemRemove(getAdapterPosition());
                        break;
                    case R.id.iv_screenImage:
                        mListener.onItemShow(getAdapterPosition());
                        break;
                }
            }
        }
    }

    private final String TAG = this.getClass().getSimpleName();
    private final List<Attachment> mAttachments;
    private final int mRowLayout;
    private final Context mContext;
    private final ViewHolder.ClickListener mListener;
    public final ViewHolder.DataChangedListener mDataChangedListener;
    private final ViewHolder.ScreenshotStateListener mStateListener;

    public AttachmentAdapter(Context context, List<Attachment> screenshots, int rowLayout, ViewHolder.DataChangedListener dataChangedListener, ViewHolder.ScreenshotStateListener stateListener) {
        mContext = context;
        mDataChangedListener = dataChangedListener;
        mListener = (ViewHolder.ClickListener) context;
        mAttachments = screenshots;
        mRowLayout = rowLayout;
        mStateListener = stateListener;
        CoreApplication.getContext().getContentResolver().registerContentObserver(LocalUri.STEP.get(), true, new StepScreenshotObserver(new Handler(), this));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        viewGroup.setHorizontalScrollBarEnabled(true);
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(mRowLayout, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(mContext, v);
        viewHolder.setClickListener(mListener);
        viewHolder.setDataChangedListener(mDataChangedListener);
        viewHolder.setScreenshotStateListener(mStateListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        if (mAttachments != null && mAttachments.size() != 0) {
            Attachment attachment = mAttachments.get(i);
            viewHolder.mScreenshotState = attachment.getScreenshotState();
            Logger.d(TAG, attachment.getFileName());
            viewHolder.mScreenshotName.setText(attachment.getFileName());
            if (attachment.getFilePath().contains(MimeType.IMAGE_PNG.getFileExtension()) ||
                    attachment.getFilePath().contains(MimeType.IMAGE_JPG.getFileExtension()) ||
                    attachment.getFilePath().contains(MimeType.IMAGE_JPEG.getFileExtension()) ||
                    attachment.getFilePath().contains(MimeType.IMAGE_GIF.getFileExtension())) {
                if (attachment.getScreenshotState() == ScreenshotState.WRITTEN) {
                    if (viewHolder.mScreenshotImage.getDrawable() == null) {
                        ImageLoader.getInstance().displayImage("file:///" + attachment.getFilePath(), viewHolder.mScreenshotImage, new ImageLoadingListener() {
                            @Override
                            public void onLoadingStarted(String imageUri, View view) {
                                viewHolder.mProgress.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {}

                            @Override
                            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                viewHolder.mProgress.setVisibility(View.GONE);
                            }

                            @Override
                            public void onLoadingCancelled(String imageUri, View view) {}
                        });
                    }
                } else {
                    viewHolder.mProgress.setVisibility(View.VISIBLE);
                }
            } else if (attachment.getFilePath().contains(MimeType.TEXT_PLAIN.getFileExtension())) {
                viewHolder.mScreenshotImage.setImageDrawable(CoreApplication.getContext().getResources().getDrawable(R.drawable.text_file_preview));
            }
            viewHolder.mScreenshotClose.setEnabled(true);
        }
    }

    @Override
    public int getItemCount() {
        return mAttachments == null ? 0 : mAttachments.size();
    }

    public void addItem(int position, Attachment data) {
        mAttachments.add(position, data);
        notifyItemInserted(position);
    }

    public ArrayList<String> getAttachmentFilePathList() {
        ArrayList<String> filePathList = new ArrayList<>();
        for (Attachment attachment : mAttachments) {
            filePathList.add(attachment.getFilePath());
        }
        return filePathList;
    }

    public List<Attachment> getAttachments() {
        return mAttachments;
    }

    public int getStepId(int position) {
        return mAttachments.get(position).getStepId();
    }
}
