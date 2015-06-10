package amtt.epam.com.amtt.adapter;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.bo.database.Step;
import amtt.epam.com.amtt.database.object.DatabaseEntity;
import amtt.epam.com.amtt.database.object.DbObjectManger;
import amtt.epam.com.amtt.database.object.IResult;
import amtt.epam.com.amtt.util.ContextHolder;
import amtt.epam.com.amtt.util.Logger;

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
        holder.step.setText("Step " + (position+1));
        holder.activityInfo.setText(step.getActivity());
        ImageLoader.getInstance().displayImage("file:///"+step.getScreenPath(),holder.screenshotView);
    }

    @Override
    public int getItemCount() {
        return listStep == null ? 0 : listStep.size();
    }

    public void removeItem(int position){
        listStep.remove(position);
        notifyItemRemoved(position);
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
            screenshotView.setMaxWidth(metrics.widthPixels / 4);
            screenshotView.setMaxHeight(metrics.heightPixels/4);
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
