package amtt.epam.com.amtt.ui.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.adapter.SharedFileAdapter;
import amtt.epam.com.amtt.helper.DialogHelper;
import amtt.epam.com.amtt.helper.SharingHelper;
import amtt.epam.com.amtt.ui.fragments.BrowserFilesFragment;
import amtt.epam.com.amtt.util.ActiveUser;
import amtt.epam.com.amtt.util.FileUtil;
import amtt.epam.com.amtt.util.ZipUtil;

/**
 * @author IvanBakach
 * @version on 22.09.2015
 */
public class ShareFilesActivity extends BaseActivity implements BrowserFilesFragment.IFileShareBrowser, SharedFileAdapter.IItemClickListener {

    private static final String SHARE_FOLDER_NAME = "Share";
    public static final String KEY_EXTRA_SHARE_FILES = "key_extra_share_files";
    private String mShareFolderName;
    private LinkedList<String> mFolderPaths = new LinkedList<>();
    private ArrayList<String> mSharedFilePaths = new ArrayList<>();
    private ScreenSlidePagerAdapter mPagerAdapter;
    private ViewPager mPager;
    private ViewPager.OnPageChangeListener mOnPageChangeListener;
    private ArrayList<String> mSharedFiles;
    private Button mBackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_file);
        String initFolderPath = FileUtil.getUsersCacheDir();
        final TextView folderPathView = (TextView) findViewById(R.id.tv_folder_path);
        folderPathView.setText(initFolderPath.replace(FileUtil.getUsersCacheDir(), "/"));

        mPager = (ViewPager) findViewById(R.id.vp_folders_layout);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    mBackButton.setVisibility(View.GONE);
                } else {
                    mBackButton.setVisibility(View.VISIBLE);
                }
                folderPathView.setText(mFolderPaths.get(position).replace(FileUtil.getUsersCacheDir(), "/"));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };
        mPager.addOnPageChangeListener(mOnPageChangeListener);

        mBackButton = (Button) findViewById(R.id.bt_back_folder);
        mBackButton.setVisibility(View.GONE);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPager.getCurrentItem() != 0) {
                    mPager.setCurrentItem(mPager.getCurrentItem() - 1, true);
                }
            }
        });
        addBrowserFilesFragment(initFolderPath);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPager.removeOnPageChangeListener(mOnPageChangeListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_share_files, menu);
        MenuItem item = menu.findItem(R.id.action_add_share_to_issue);
        if (ActiveUser.getInstance().getUserName() != null) {
            item.setVisible(true);
            invalidateOptionsMenu();
        } else {
            item.setVisible(false);
            invalidateOptionsMenu();
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete: {
                new AlertDialog.Builder(ShareFilesActivity.this)
                        .setTitle(getString(R.string.title_delete_select_files))
                        .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mSharedFiles = mSharedFilePaths;
                                ArrayList<String> removeFolderPaths = new ArrayList<>();
                                for (String filePath : mSharedFiles) {
                                    if (new File(filePath).isDirectory()) {
                                        removeFolderPaths.add(filePath);
                                    }
                                    FileUtil.deleteRecursive(filePath);
                                }
                                for (String folderPath : removeFolderPaths) {
                                    mFolderPaths.remove(folderPath);
                                }
                                mPagerAdapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton(getString(R.string.cancel), null)
                        .create()
                        .show();
                return true;
            }
            case R.id.action_share: {
                mSharedFiles = deleteFoldersPath(mSharedFilePaths);
                DialogHelper.getShareFileDialog(ShareFilesActivity.this, mSharedFiles, new DialogHelper.IShareAction() {
                    @Override
                    public void shareTo(String shareFolderName, ArrayList<String> sharedFilesWithoutFolder) {
                        String zipFilePath = ZipUtil.createZipFile(shareFolderName, sharedFilesWithoutFolder);
                        SharingHelper.senAttachmentFile(ShareFilesActivity.this, zipFilePath);
                    }
                }).show();
                return true;
            }
            case R.id.action_add_share_to_issue:
                mSharedFiles = deleteFoldersPath(mSharedFilePaths);
                DialogHelper.getShareFileDialog(ShareFilesActivity.this, mSharedFiles, new DialogHelper.IShareAction() {
                    @Override
                    public void shareTo(String shareFolderName, ArrayList<String> sharedFilesWithoutFolder) {
                        if (getIntent().getStringExtra(CreateIssueActivity.KEY_START_ACTIVITY_FOR_RESULT)!=null) {
                            Intent shareIntent = new Intent();
                            shareIntent.putExtra(CreateIssueActivity.KEY_LIST_ADD_SHARED_FILE, sharedFilesWithoutFolder);
                            setResult(RESULT_OK, shareIntent);
                        } else {
                            Intent shareIntent = new Intent(ShareFilesActivity.this, CreateIssueActivity.class);
                            shareIntent.putExtra(KEY_EXTRA_SHARE_FILES, sharedFilesWithoutFolder);
                            startActivity(shareIntent);
                        }
                        finish();
                    }
                }).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if(mPager.getCurrentItem()!=0) {
            mPager.setCurrentItem(mPager.getCurrentItem()-1, true);
        } else {
            super.onBackPressed(); // This will pop the Activity from the stack.
        }
    }

    private void addBrowserFilesFragment(String folderPath) {
        int addIndex = mFolderPaths.size()<1?mPager.getCurrentItem():mPager.getCurrentItem() + 1;
        mFolderPaths.add(addIndex, folderPath);
        mPagerAdapter.notifyDataSetChanged();
        //remove all item after addIndex if addIndex isn't last
        mPager.setCurrentItem(mPager.getCurrentItem() + 1, true);
        if (mPager.getCurrentItem()+1<=mFolderPaths.size()-1) {
            for (int i = mPager.getCurrentItem()+1; i < mFolderPaths.size();i++) {
                mFolderPaths.remove(i);
            }
        }
        mPagerAdapter.notifyDataSetChanged();
    }

    private ArrayList<String> deleteFoldersPath(final ArrayList<String> sharedFiles) {
        ArrayList<String> sharedFilesWithoutFolder = new ArrayList<>(sharedFiles);
        for (String filePath : sharedFiles) {
            if (new File(filePath).isDirectory()) {
                sharedFilesWithoutFolder.remove(filePath);
            }
        }
        return sharedFilesWithoutFolder;
    }


    @Override
    public void openFolder(String folderPath) {
        addBrowserFilesFragment(folderPath);
    }

    @Override
    public void shareFile(File sharedFile, boolean isChecked) {
        if (isChecked&&!mSharedFilePaths.contains(sharedFile.getPath())) {
            if (sharedFile.isDirectory()) {
                mSharedFilePaths.add(sharedFile.getPath());
                for (File file : FileUtil.getListWithDirFiles(sharedFile)) {
                    mSharedFilePaths.add(file.getPath());
                }
            } else {
                mSharedFilePaths.add(sharedFile.getPath());
            }
        } else {
            if (sharedFile.isDirectory()) {
                mSharedFilePaths.remove(sharedFile.getPath());
                for (File file : FileUtil.getListWithDirFiles(sharedFile)) {
                    mSharedFilePaths.remove(file.getPath());
                }
            } else {
                mSharedFilePaths.remove(sharedFile.getPath());
            }
        }
    }

    @Override
    public void onCheckFile(String checkedFile, boolean isChecked) {
        if (isChecked && !mSharedFiles.contains(checkedFile)) {
            mSharedFiles.add(checkedFile);
        } else {
            mSharedFiles.remove(checkedFile);
        }
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public BrowserFilesFragment getItem(int position) {
            return BrowserFilesFragment.newInstance(mFolderPaths.get(position), mSharedFilePaths, ShareFilesActivity.this);
        }

        @Override
        public int getCount() {
            return mFolderPaths.size();
        }
    }
}
