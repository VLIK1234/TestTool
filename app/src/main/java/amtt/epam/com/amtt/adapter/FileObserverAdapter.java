package amtt.epam.com.amtt.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import amtt.epam.com.amtt.CoreApplication;
import amtt.epam.com.amtt.R;

/**
 * @author IvanBakach
 * @version on 22.09.2015
 */
public class FileObserverAdapter extends RecyclerView.Adapter<FileObserverAdapter.ViewHolder>{

    public interface IItemClickListener {
        void onFolderClick(File chooseFile);
        void onCheckFile(File checkedFile, boolean isChecked);
    }

    private File[] mListFile;
    private ArrayList<String> mSharedFiles;
    private IItemClickListener mListener;

    public FileObserverAdapter(File[] listFile, ArrayList<String> sharedFiles, IItemClickListener listener) {
        mListFile = listFile;
        mSharedFiles = sharedFiles;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        parent.setHorizontalScrollBarEnabled(true);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_file, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.fileName.setText(mListFile[position].getName());
        if (mListFile[position].isDirectory()) {
            holder.fileIcon.setImageDrawable(CoreApplication.getContext().getResources().getDrawable(R.drawable.ic_file_folder));
        } else {
            holder.fileIcon.setImageDrawable(CoreApplication.getContext().getResources().getDrawable(R.drawable.ic_file));
        }
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onFolderClick(mListFile[position]);
            }
        });

        for (String shareFilePath : mSharedFiles) {
            if (shareFilePath.equals(mListFile[position].getPath())) {
                holder.checkFile.setChecked(true);break;
            }
        }

        holder.checkFile.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mListener.onCheckFile(mListFile[position], isChecked);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListFile == null ? 0 : mListFile.length;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final RelativeLayout rootView;
        public final TextView fileName;
        public final ImageView fileIcon;
        public final CheckBox checkFile;

        public ViewHolder(View itemView) {
            super(itemView);
            rootView = (RelativeLayout) itemView.findViewById(R.id.rl_root_view);
            fileName = (TextView) itemView.findViewById(R.id.tv_file_name);
            fileIcon = (ImageView) itemView.findViewById(R.id.iv_file_icon);
            checkFile = (CheckBox) itemView.findViewById(R.id.cb_share_this);
        }
    }

}
