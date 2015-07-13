package amtt.epam.com.amtt.adapter;

import amtt.epam.com.amtt.AmttApplication;
import amtt.epam.com.amtt.bo.ticket.Attachment;
import amtt.epam.com.amtt.util.Logger;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import amtt.epam.com.amtt.R;

/**
@author Iryna Monchanka
@version on 27.05.2015
 */

public class AttachmentAdapter extends RecyclerView.Adapter<AttachmentAdapter.ViewHolder>{

    private final String TAG = this.getClass().getSimpleName();
    private List<Attachment> mScreenshots;
    private int mRowLayout;
    private ViewHolder.ClickListener mClickListener;

        public AttachmentAdapter(List<Attachment> screenshots, int rowLayout, ViewHolder.ClickListener clickListener) {
        this.mScreenshots = screenshots;
        this.mRowLayout = rowLayout;
        this.mClickListener = clickListener;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        viewGroup.setHorizontalScrollBarEnabled(true);
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(mRowLayout, viewGroup, false);
        return new ViewHolder(v, mClickListener);
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Attachment screenshot = mScreenshots.get(i);
        Logger.d(TAG, screenshot.mName);
        viewHolder.mScreenshotName.setText(screenshot.mName);
        if (screenshot.mFilePath.contains(".png")) {
            ImageLoader.getInstance().displayImage("file:///" + screenshot.mFilePath, viewHolder.mScreenshotImage);
        }else if (screenshot.mFilePath.contains(".txt")){
            viewHolder.mScreenshotImage.setImageDrawable(AmttApplication.getContext().getResources().getDrawable(R.drawable.text_file_preview));
        }

        viewHolder.mScreenshotClose.setEnabled(true);
    }

    @Override
    public int getItemCount() {
        return mScreenshots == null ? 0 : mScreenshots.size();
    }

    public void addItem(int position, Attachment data) {
        mScreenshots.add(position, data);
        notifyItemInserted(position);
    }

    public void removeItem(int position) {
        mScreenshots.remove(position);
        notifyItemRemoved(position);
        Logger.d(TAG, String.valueOf(position));
    }

    public ArrayList<String> getAttachmentFilePathList(){
        ArrayList<String> filePathList = new ArrayList<>();
        for (Attachment attachment: mScreenshots) {
            filePathList.add(attachment.mFilePath);
        }
        return filePathList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView mScreenshotImage;
        public TextView mScreenshotName;
        public ImageView mScreenshotClose;
        private ClickListener mListener;


        public ViewHolder(View itemView, ClickListener listener) {
            super(itemView);
            mScreenshotImage = (ImageView)itemView.findViewById(R.id.iv_screenImage);
            mScreenshotImage.setOnClickListener(this);
            mScreenshotName = (TextView)itemView.findViewById(R.id.tv_screenName);
            mScreenshotClose = (ImageView)itemView.findViewById(R.id.iv_close);
            this.mListener = listener;
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

}
