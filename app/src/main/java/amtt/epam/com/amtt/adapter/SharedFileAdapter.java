package amtt.epam.com.amtt.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.util.FileUtil;

/**
 * @author IvanBakach
 * @version on 25.09.2015
 */
public class SharedFileAdapter extends RecyclerView.Adapter<SharedFileAdapter.ViewHolder> {

    public interface IItemClickListener {
        void onCheckFile(String checkedFile, boolean isChecked);
    }

    private ArrayList<String> mSharedFiles;
    private IItemClickListener mListener;

    public SharedFileAdapter(ArrayList<String> sharedFiles, IItemClickListener listener) {
        mSharedFiles = sharedFiles;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        parent.setHorizontalScrollBarEnabled(true);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_shared_file, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.fileName.setText(mSharedFiles.get(position).replace(FileUtil.getUsersCacheDir(), "/"));

        holder.checkFile.setChecked(true);
        holder.checkFile.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mListener.onCheckFile(mSharedFiles.get(position), isChecked);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mSharedFiles == null ? 0 : mSharedFiles.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView fileName;
//        public final ImageView fileIcon;
        public final CheckBox checkFile;

        public ViewHolder(View itemView) {
            super(itemView);
            fileName = (TextView) itemView.findViewById(R.id.tv_file_name);
//            fileIcon = (ImageView) itemView.findViewById(R.id.iv_file_icon);
            checkFile = (CheckBox) itemView.findViewById(R.id.cb_share_this);
        }
    }

}
