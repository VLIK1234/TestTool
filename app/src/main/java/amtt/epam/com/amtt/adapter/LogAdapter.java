package amtt.epam.com.amtt.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.style.QuoteSpan;
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

    private ArrayList<String> listLog = new ArrayList<>();

    public LogAdapter(ArrayList<String> listLog) {
        this.listLog = listLog;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        parent.setHorizontalScrollBarEnabled(true);
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_log, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.logLine.setText(listLog.get(position));
        setColorTextView(holder.logLine, listLog.get(position));
    }

    @Override
    public int getItemCount() {
        return listLog == null ? 0 : listLog.size();
    }

    public void setColorTextView(TextView textView, String logLine){
        if (logLine.contains("E/")) {
            textView.setTextColor(AmttApplication.getContext().getResources().getColor(android.R.color.holo_red_light));
        } else if (logLine.contains("F/")) {
            textView.setTextColor(AmttApplication.getContext().getResources().getColor(android.R.color.holo_red_light));
        }  else if (logLine.contains("W/")) {
            textView.setTextColor(AmttApplication.getContext().getResources().getColor(android.R.color.holo_orange_light));
        } else if (logLine.contains("I/")) {
            textView.setTextColor(Color.GREEN);
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
