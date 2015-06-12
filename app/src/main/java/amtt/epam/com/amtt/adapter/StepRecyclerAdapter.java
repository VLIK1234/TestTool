package amtt.epam.com.amtt.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
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

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.bo.database.Step;
import amtt.epam.com.amtt.database.object.DbObjectManger;
import amtt.epam.com.amtt.database.table.ActivityInfoTable;
import amtt.epam.com.amtt.util.ContextHolder;

/**
 * Created by Ivan_Bakach on 10.06.2015.
 */
public class StepRecyclerAdapter extends RecyclerView.Adapter<StepRecyclerAdapter.ViewHolder> {

    private ArrayList<Step> listStep = new ArrayList<>();
    private ViewHolder.ClickListener clickListener;

    public StepRecyclerAdapter(ArrayList<Step> listStep, ViewHolder.ClickListener clickListener){
        this.listStep = listStep;
        this.clickListener = clickListener;
//        this.listStep.add(new Step(getClass().getName(),"/storage/sdcard0/Pictures/Screenshots/Screenshot_2015-06-10-16-50-22.png"));
//        this.listStep.add(new Step(getClass().getName(),"/storage/sdcard0/Pictures/Screenshots/Screenshot_2015-06-10-16-50-22.png"));
//        this.listStep.add(new Step(getClass().getName(),"/storage/sdcard0/Pictures/Screenshots/Screenshot_2015-06-10-16-50-22.png"));
    }

    @Override
    public StepRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        parent.setHorizontalScrollBarEnabled(true);
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_step_recycler, parent, false);
        return new ViewHolder(v, clickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Step step = listStep.get(position);
        holder.step.setText(ContextHolder.getContext().getString(R.string.label_step) + (position + 1));
        Context context = ContextHolder.getContext();
        SpannableStringBuilder info = new SpannableStringBuilder();
        info.append(Html.fromHtml("<b>" + context.getString(R.string.label_activity) + "</b>" + "<small>" + step.getActivity() + "</small>" + "<br />" +
                "<b>" + context.getString(R.string.label_screen_orientation) + "</b>" + "<small>" + step.getOreintation() + "</small>" + "<br />" +
                "<b>" + context.getString(R.string.label_package_name) + "</b>" + "<small>" + step.getPackageName() + "</small>" + "<br />"));
        holder.activityInfo.setText(info);
        if (!TextUtils.isEmpty(step.getScreenPath())) {
            ImageLoader.getInstance().displayImage("file:///"+step.getScreenPath(), holder.screenshotView);
        }else{
            holder.screenshotView.setImageDrawable(null);
        }
    }

    @Override
    public int getItemCount() {
        return listStep == null ? 0 : listStep.size();
    }

    public void removeItem(int position){
        DbObjectManger.INSTANCE.remove(listStep.get(position-1));
        listStep.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    public String getScreenPath(int position){
        return listStep.get(position).getScreenPath();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView screenshotView;
        public ImageView removeButton;
        public TextView activityInfo;
        public TextView step;
        private ClickListener listener;

        public ViewHolder(View itemView, ClickListener listener) {
            super(itemView);
            this.listener = listener;
            screenshotView = (ImageView)itemView.findViewById(R.id.screenshot_image);
            DisplayMetrics metrics = ContextHolder.getContext().getResources().getDisplayMetrics();
            screenshotView.setMaxWidth(metrics.widthPixels/3);
            screenshotView.setMaxHeight(metrics.heightPixels/3);
            screenshotView.setOnClickListener(this);
            removeButton = (ImageView)itemView.findViewById(R.id.iv_close);
            removeButton.setOnClickListener(this);
            activityInfo = (TextView)itemView.findViewById(R.id.activity_info_text);
            step = (TextView)itemView.findViewById(R.id.step_text);
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
