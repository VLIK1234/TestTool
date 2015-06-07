package amtt.epam.com.amtt.ticket;

import amtt.epam.com.amtt.app.CreateIssueActivity;
import amtt.epam.com.amtt.broadcastreceiver.GlobalBroadcastReceiver;
import amtt.epam.com.amtt.loader.InternalStorageImageLoader;
import amtt.epam.com.amtt.util.ContextHolder;
import amtt.epam.com.amtt.util.Logger;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import amtt.epam.com.amtt.R;

/**
@author Iryna Monchanka
@version on 27.05.2015
 */

public class ScreenshotAdapter extends RecyclerView.Adapter<ScreenshotAdapter.ViewHolder>{

    private final String TAG = this.getClass().getSimpleName();
    private ArrayList<Attachment> screenshots;
    private int rowLayout;
    private static final InternalStorageImageLoader sImageLoader;
    public static final int IMAGE_VIEW_WIDTH = 360;
    public static final int IMAGE_VIEW_HEIGHT = 640;
    private ViewHolder.ClickListener clickListener;

    static {
        sImageLoader = new InternalStorageImageLoader(10, IMAGE_VIEW_WIDTH, IMAGE_VIEW_HEIGHT);
    }

        public ScreenshotAdapter(ArrayList<Attachment> screenshots, int rowLayout, ViewHolder.ClickListener clickListener) {
        this.screenshots = screenshots;
        this.rowLayout = rowLayout;
        this.clickListener = clickListener;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        viewGroup.setHorizontalScrollBarEnabled(true);
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(rowLayout, viewGroup, false);
        return new ViewHolder(v, clickListener);
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Attachment screenshot = screenshots.get(i);
        Logger.d(TAG, screenshot.name);
        viewHolder.screenshotName.setText(screenshot.name);
        if (screenshot.filePath.contains(".png")) {
            sImageLoader.load(viewHolder.screenshotImage, screenshot.filePath);
        }else if (screenshot.filePath.contains(".txt")){
            viewHolder.screenshotImage.setImageDrawable(ContextHolder.getContext().getResources().getDrawable(R.drawable.text_file_preview));
        }

        viewHolder.screenshotClose.setEnabled(true);
    }

    @Override
    public int getItemCount() {
        return screenshots == null ? 0 : screenshots.size();
    }

    public void addItem(int position, Attachment data) {
        screenshots.add(position, data);
        notifyItemInserted(position);
    }

    public void removeItem(int position) {
        screenshots.remove(position);
        notifyItemRemoved(position);
        Logger.d(TAG, String.valueOf(position));
    }

    public ArrayList<String> getAttachmentFilePathList(){
        ArrayList<String> filePathList = new ArrayList<>();
        for (Attachment attachment:screenshots) {
            filePathList.add(attachment.filePath);
        }
        return filePathList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView screenshotImage;
        public TextView screenshotName;
        public ImageView screenshotClose;
        private ClickListener listener;


        public ViewHolder(View itemView, ClickListener listener) {
            super(itemView);
            screenshotImage = (ImageView)itemView.findViewById(R.id.iv_screenImage);
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
                        listener.onItemClicked(getAdapterPosition());
                    }
                    break;

            }
        }
        public interface ClickListener {
            void onItemClicked(int position);
        }
    }

}
