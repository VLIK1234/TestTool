package amtt.epam.com.amtt.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.adapter.FileBrowserAdapter;
import amtt.epam.com.amtt.util.FileUtil;

/**
 * @author IvanBakach
 * @version on 22.09.2015
 */
public class BrowserFilesFragment extends Fragment implements FileBrowserAdapter.IItemClickListener {

    public static final String SHARE_FILE_LIST_KEY = "share_file_list";

    public interface IFileShareBrowser {
        void openFolder(String folderPath);
        void shareFile(File sharedFile, boolean isChecked);
    }

    private static final int SPAN_COUNT = 3;
    public static final String FOLDER_PATH_KEY = "folder_path_key";
    private FileBrowserAdapter mFileObserverAdapter;
    private RecyclerView mRecyclerView;
    private TextView mEmptyView;
    private IFileShareBrowser mIFileShareBrowser;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_browser, container, false);
        mRecyclerView = (RecyclerView)view.findViewById(R.id.rv_file_browser);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), SPAN_COUNT));
        mEmptyView = (TextView) view.findViewById(R.id.tv_empty_view);
        return view;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final File browserFolder = new File(getFolderPath());
        final File[] sortedFiles = FileUtil.sortArray(browserFolder.listFiles());
        mFileObserverAdapter = new FileBrowserAdapter(sortedFiles, getShareFileList(), this);
        mRecyclerView.setAdapter(mFileObserverAdapter);
        if (mFileObserverAdapter.getItemCount()==0) {
            mEmptyView.setVisibility(View.VISIBLE);
        } else {
            mEmptyView.setVisibility(View.GONE);
        }
    }

    public void setIFileShareBrowser(IFileShareBrowser iFileShareBrowser) {
        mIFileShareBrowser = iFileShareBrowser;
    }

    public static BrowserFilesFragment newInstance(String folderPath, ArrayList<String> shareFilesList, IFileShareBrowser iFileShareBrowser){
        BrowserFilesFragment browserFilesFragment = new BrowserFilesFragment();
        Bundle args = new Bundle();
        args.putString(FOLDER_PATH_KEY, folderPath);
        args.putStringArrayList(SHARE_FILE_LIST_KEY, shareFilesList);
        browserFilesFragment.setArguments(args);
        browserFilesFragment.setIFileShareBrowser(iFileShareBrowser);
        return browserFilesFragment;
    }

    public String getFolderPath() {
        return getArguments().getString(FOLDER_PATH_KEY);
    }

    public ArrayList<String> getShareFileList() {
        return getArguments().getStringArrayList(SHARE_FILE_LIST_KEY);
    }

    @Override
    public void onFolderClick(File chooseFile) {
        if (chooseFile.isDirectory()) {
            mFileObserverAdapter = new FileBrowserAdapter(chooseFile.listFiles(), getShareFileList(), this);
            mRecyclerView.setAdapter(mFileObserverAdapter);
            mIFileShareBrowser.openFolder(chooseFile.getPath());
        }

    }

    @Override
    public void onCheckFile(File checkedFile, boolean isChecked) {
        mIFileShareBrowser.shareFile(checkedFile, isChecked);
    }
}
