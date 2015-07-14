package amtt.epam.com.amtt.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import amtt.epam.com.amtt.AmttApplication;
import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.bo.database.Step;
import amtt.epam.com.amtt.database.object.DbObjectManager;
import amtt.epam.com.amtt.database.util.StepUtil;

/**
 * @author Ivan_Bakach
 * @version on 10.06.2015
 */

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.ViewHolder> {

    private ArrayList<Step> listStep = new ArrayList<>();
    private ViewHolder.ClickListener clickListener;
    private final static int IMAGE_SIZE_RATIO = 3;

    public StepsAdapter(ArrayList<Step> listStep, ViewHolder.ClickListener clickListener) {
        this.listStep = listStep;
        this.clickListener = clickListener;
    }

    @Override
    public StepsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        parent.setHorizontalScrollBarEnabled(true);
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_step, parent, false);
        return new ViewHolder(v, clickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Step step = listStep.get(position);
        holder.step.setText(AmttApplication.getContext().getString(R.string.label_step) + (position + 1));
        if (step.getActivity() != null) {
            SpannableStringBuilder info = new SpannableStringBuilder();
            info.append(StepUtil.getStepInfo(step));
            holder.activityInfo.setVisibility(View.VISIBLE);
            holder.activityInfo.setText(info);
        }
        if (!TextUtils.isEmpty(step.getScreenshotPath())) {
            ImageLoader.getInstance().displayImage("file:///" + step.getScreenshotPath(), holder.screenshotView);
        } else {
            holder.screenshotView.setImageDrawable(null);
        }
    }

    @Override
    public int getItemCount() {
        return listStep == null ? 0 : listStep.size();
    }

    public void removeItem(int position) {
        DbObjectManager.INSTANCE.remove(listStep.get(position));
        listStep.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    public String getScreenPath(int position) {
        return listStep.get(position).getScreenshotPath();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView screenshotView;
        public ImageView removeButton;
        public TextView activityInfo;
        public TextView step;
        private ClickListener listener;

        public ViewHolder(View itemView, ClickListener listener) {
            super(itemView);
            this.listener = listener;
            screenshotView = (ImageView) itemView.findViewById(R.id.screenshot_image);
            DisplayMetrics metrics = AmttApplication.getContext().getResources().getDisplayMetrics();
            screenshotView.setMaxWidth(metrics.widthPixels / IMAGE_SIZE_RATIO);
            screenshotView.setMaxHeight(metrics.heightPixels / IMAGE_SIZE_RATIO);
            screenshotView.setOnClickListener(this);
            removeButton = (ImageView) itemView.findViewById(R.id.iv_close);
            removeButton.setOnClickListener(this);
            activityInfo = (TextView) itemView.findViewById(R.id.activity_info_text);
            step = (TextView) itemView.findViewById(R.id.step_text);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_close:
                    if (listener != null) {
                        listener.onItemRemove(getAdapterPosition());
                    }
                    break;
                case R.id.screenshot_image:
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
