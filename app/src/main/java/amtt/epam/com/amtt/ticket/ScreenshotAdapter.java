package amtt.epam.com.amtt.ticket;

import amtt.epam.com.amtt.util.Logger;
import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import amtt.epam.com.amtt.R;

public class ScreenshotAdapter extends RecyclerView.Adapter<ScreenshotAdapter.ViewHolder>{

    private final String TAG = this.getClass().getSimpleName();
    private List<Screenshot> screenshots;
    private int rowLayout;

    public ScreenshotAdapter(List<Screenshot> screenshots, int rowLayout) {
        this.screenshots = screenshots;
        this.rowLayout = rowLayout;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        viewGroup.setHorizontalScrollBarEnabled(true);
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(rowLayout, viewGroup, false);
        return new ViewHolder(v);
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Screenshot screenshot = screenshots.get(i);
        Logger.d(TAG, screenshot.name);
        viewHolder.screenshotImage.setImageURI(Uri.parse(screenshot.imageName));
    }

    @Override
    public int getItemCount() {
        return screenshots == null ? 0 : screenshots.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView screenshotImage;

        public ViewHolder(View itemView) {
            super(itemView);
            screenshotImage = (ImageView)itemView.findViewById(R.id.screenImage);
        }

    }
}
