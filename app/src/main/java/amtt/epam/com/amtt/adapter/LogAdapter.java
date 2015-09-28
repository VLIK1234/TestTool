package amtt.epam.com.amtt.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import amtt.epam.com.amtt.CoreApplication;
import amtt.epam.com.amtt.R;

/**
 @author Ivan_Bakach
 @version on 10.07.2015
 */

public class LogAdapter extends RecyclerView.Adapter<LogAdapter.ViewHolder> {

    private static final String ERROR_TAG = "E/";
    private static final String FATAL_TAG = "F/";
    private static final String WARNING_TAG = "W/";
    private static final String INFO_TAG = "I/";
    private static final int ERROR_COLOR = CoreApplication.getContext().getResources().getColor(android.R.color.holo_red_light);
    private static final int WARNING_COLOR = CoreApplication.getContext().getResources().getColor(android.R.color.holo_orange_light);
    private static final int INFO_COLOR = CoreApplication.getContext().getResources().getColor(android.R.color.holo_green_light);
    private static final int DEBUG_COLOR = CoreApplication.getContext().getResources().getColor(android.R.color.black);
    private ArrayList<Spanned> mListLog = new ArrayList<>();

    public LogAdapter(ArrayList<Spanned> listLog) {
        this.mListLog = listLog;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        parent.setHorizontalScrollBarEnabled(true);
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_log, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.logLine.setText(mListLog.get(position));
        setColorTextView(holder.logLine, mListLog.get(position));
    }

    @Override
    public int getItemCount() {
        return mListLog == null ? 0 : mListLog.size();
    }

    private void setColorTextView(TextView textView, CharSequence logLine){
        if (logLine.toString().contains(ERROR_TAG)) {
            textView.setTextColor(ERROR_COLOR);
        } else if (logLine.toString().contains(FATAL_TAG)) {
            textView.setTextColor(ERROR_COLOR);
        }  else if (logLine.toString().contains(WARNING_TAG)) {
            textView.setTextColor(WARNING_COLOR);
        } else if (logLine.toString().contains(INFO_TAG)) {
            textView.setTextColor(INFO_COLOR);
        }else {
            textView.setTextColor(DEBUG_COLOR);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView logLine;

        public ViewHolder(View itemView) {
            super(itemView);
            logLine = (TextView) itemView.findViewById(R.id.log_line);
        }
    }

}
