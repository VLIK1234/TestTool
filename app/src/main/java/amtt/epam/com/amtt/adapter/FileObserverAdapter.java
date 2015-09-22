package amtt.epam.com.amtt.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

import amtt.epam.com.amtt.CoreApplication;
import amtt.epam.com.amtt.R;

/**
 * @author IvanBakach
 * @version on 22.09.2015
 */
public class FileObserverAdapter extends RecyclerView.Adapter<FileObserverAdapter.ViewHolder>{

    private File[] mListFile;

    public FileObserverAdapter(File[] listFile) {
        mListFile = listFile;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        parent.setHorizontalScrollBarEnabled(true);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_file, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.fileName.setText(mListFile[position].getName());
        if (mListFile[position].isDirectory()) {
            holder.fileIcon.setImageDrawable(CoreApplication.getContext().getResources().getDrawable(R.drawable.ic_file_folder));
        } else {
            holder.fileIcon.setImageDrawable(CoreApplication.getContext().getResources().getDrawable(R.drawable.ic_file));
        }
    }

    @Override
    public int getItemCount() {
        return mListFile == null ? 0 : mListFile.length;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView fileName;
        public final ImageView fileIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            fileName = (TextView) itemView.findViewById(R.id.tv_file_name);
            fileIcon = (ImageView) itemView.findViewById(R.id.iv_file_icon);
        }
    }

}
