package amtt.epam.com.amtt.ui.fragments;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.adapter.FileObserverAdapter;

/**
 * @author IvanBakach
 * @version on 22.09.2015
 */
public class BrowserFragment extends Fragment {

    private static final int SPAN_COUNT = 3;
    private FileObserverAdapter mFileObserverAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_browser, container, false);
        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.rv_file_browser);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), SPAN_COUNT));
        File currentDirectory = Environment.getExternalStorageDirectory();
        for (File file: currentDirectory.listFiles()) {
            Log.d("List", file.getName());
        }

        mFileObserverAdapter = new FileObserverAdapter(currentDirectory.listFiles());
        recyclerView.setAdapter(mFileObserverAdapter);
        return view;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public static BrowserFragment newInstance(){
        BrowserFragment browserFragment = new BrowserFragment();
        return browserFragment;
    }
}
