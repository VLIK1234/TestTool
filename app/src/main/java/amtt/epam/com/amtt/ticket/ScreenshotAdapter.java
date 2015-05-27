package amtt.epam.com.amtt.ticket;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import amtt.epam.com.amtt.R;

public class ScreenshotAdapter extends RecyclerView.Adapter<ScreenshotAdapter.ViewHolder>{

    private List<Screenshot> screenshots;
    private int rowLayout;
    private Context mContext;

    public ScreenshotAdapter(List<Screenshot> screenshots, int rowLayout, Context context) {
        this.screenshots = screenshots;
        this.rowLayout = rowLayout;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(rowLayout, viewGroup, false);
        return new ViewHolder(v);
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Screenshot screenshot = screenshots.get(i);
        viewHolder.screenshotName.setText(screenshot.name);
        viewHolder.screenshotImage.setImageURI(Uri.parse(screenshot.imageName));
    }

    @Override
    public int getItemCount() {
        return screenshots == null ? 0 : screenshots.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView screenshotName;
        public ImageView screenshotImage;

        public ViewHolder(View itemView) {
            super(itemView);
            screenshotName = (TextView) itemView.findViewById(R.id.screenName);
            screenshotImage = (ImageView)itemView.findViewById(R.id.screenImage);
        }

    }
}
