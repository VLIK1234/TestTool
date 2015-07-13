package amtt.epam.com.amtt.adapter;

import amtt.epam.com.amtt.AmttApplication;
import amtt.epam.com.amtt.bo.ticket.Attachment;
import amtt.epam.com.amtt.util.FileUtil;

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
    private List<Attachment> mAttachments;
    private int mRowLayout;
    private ViewHolder.ClickListener mClickListener;

        public AttachmentAdapter(List<Attachment> attachments, int rowLayout, ViewHolder.ClickListener clickListener) {
        this.mAttachments = attachments;
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
        Attachment screenshot = mAttachments.get(i);
        viewHolder.screenshotName.setText(screenshot.name);
        if (FileUtil.isPicture(screenshot.filePath)) {
            ImageLoader.getInstance().displayImage("file:///" + screenshot.filePath, viewHolder.screenshotImage);
        }else if (FileUtil.isText(screenshot.filePath)){
            viewHolder.screenshotImage.setImageDrawable(AmttApplication.getContext().getResources().getDrawable(R.drawable.text_file_preview));
        }

        viewHolder.screenshotClose.setEnabled(true);
    }

    @Override
    public int getItemCount() {
        return mAttachments == null ? 0 : mAttachments.size();
    }

    public void addItem(Attachment data) {
        mAttachments.add(data);
        notifyItemInserted(getItemCount()-1);
    }

    public void removeItem(int position) {
        mAttachments.remove(position);
        notifyItemRemoved(position);
    }

    public ArrayList<String> getAttachmentFilePathList(){
        ArrayList<String> filePathList = new ArrayList<>();
        for (Attachment attachment: mAttachments) {
            filePathList.add(attachment.filePath);
        }
        return filePathList;
    }

    public boolean contains(Attachment filePath) {
        for (Attachment attachment: mAttachments) {
            if (attachment.filePath.equals(filePath.filePath)) {
                return true;
            }
        }
        return false;

    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView screenshotImage;
        public TextView screenshotName;
        public ImageView screenshotClose;
        private ClickListener listener;


        public ViewHolder(View itemView, ClickListener listener) {
            super(itemView);
            screenshotImage = (ImageView)itemView.findViewById(R.id.iv_screenImage);
            screenshotImage.setOnClickListener(this);
            screenshotName = (TextView)itemView.findViewById(R.id.tv_screenName);
            screenshotClose = (ImageView)itemView.findViewById(R.id.iv_close);
            this.listener = listener;
            screenshotClose.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_close:
                    if (listener != null) {
                        listener.onItemRemove(getAdapterPosition());
                    }
                    break;
                case R.id.iv_screenImage:
                    if (listener != null) {
                        listener.onItemShow(getAdapterPosition());
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
