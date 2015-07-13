package amtt.epam.com.amtt.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import amtt.epam.com.amtt.AmttApplication;
import amtt.epam.com.amtt.R;

/**
 * Created by Ivan_Bakach on 10.07.2015.
 */
public class LogAdapter extends RecyclerView.Adapter<LogAdapter.ViewHolder> {

    private ArrayList<CharSequence> mListLog = new ArrayList<>();

    public LogAdapter(ArrayList<CharSequence> listLog) {
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

    public void setColorTextView(TextView textView, CharSequence logLine){
        if (logLine.toString().contains("E/")) {
            textView.setTextColor(AmttApplication.getContext().getResources().getColor(android.R.color.holo_red_light));
        } else if (logLine.toString().contains("F/")) {
            textView.setTextColor(AmttApplication.getContext().getResources().getColor(android.R.color.holo_red_light));
        }  else if (logLine.toString().contains("W/")) {
            textView.setTextColor(AmttApplication.getContext().getResources().getColor(android.R.color.holo_orange_light));
        } else if (logLine.toString().contains("I/")) {
            textView.setTextColor(AmttApplication.getContext().getResources().getColor(android.R.color.holo_green_light));
        }else {
            textView.setTextColor(AmttApplication.getContext().getResources().getColor(android.R.color.black));
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView logLine;

        public ViewHolder(View itemView) {
            super(itemView);
            logLine = (TextView) itemView.findViewById(R.id.log_line);
        }
    }

}
