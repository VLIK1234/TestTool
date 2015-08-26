package amtt.epam.com.amtt.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.googleapi.bo.GEntryWorksheet;
import amtt.epam.com.amtt.util.Logger;

/**
 * @author Iryna Monchanka
 * @version on 27.05.2015
 */

public class ExpectedResultsAdapter extends RecyclerView.Adapter<ExpectedResultsAdapter.ViewHolder> {

    private final String TAG = this.getClass().getSimpleName();
    private final List<GEntryWorksheet> mTestcases;
    private final int mItemLayout;
    private final ViewHolder.ClickListener mClickListener;

    public ExpectedResultsAdapter(List<GEntryWorksheet> testcases, int itemLayout, ViewHolder.ClickListener clickListener) {
        this.mTestcases = testcases;
        this.mItemLayout = itemLayout;
        this.mClickListener = clickListener;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        viewGroup.setHorizontalScrollBarEnabled(true);
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(mItemLayout, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        viewHolder.setClickListener(mClickListener);
        return viewHolder;
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        GEntryWorksheet testcase = mTestcases.get(i);
        if (testcase != null) {
            Logger.d(TAG, testcase.getTestCaseNameGSX());
            viewHolder.mTestcaseName.setText(testcase.getTestCaseNameGSX());
            viewHolder.mPriority.setText(testcase.getPriorityGSX());
            viewHolder.mSteps.setText(testcase.getTestStepsGSX());
        }
    }

    @Override
    public int getItemCount() {
        return mTestcases == null ? 0 : mTestcases.size();
    }

    public ArrayList<String> getIdTestcaseList() {
        ArrayList<String> idTestcaseList = new ArrayList<>();
        for (GEntryWorksheet testcase : mTestcases) {
            idTestcaseList.add(testcase.getIdLink());
        }
        return idTestcaseList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView mTestcaseName;
        public final TextView mPriority;
        public final TextView mSteps;
        public final ImageButton mBugButton;
        private ClickListener mListener;

        public ViewHolder(View itemView) {
            super(itemView);
            mTestcaseName = (TextView) itemView.findViewById(R.id.tv_testcase_name);
            mPriority = (TextView) itemView.findViewById(R.id.tv_priority);
            mSteps = (TextView) itemView.findViewById(R.id.tv_steps);
            mBugButton = (ImageButton) itemView.findViewById(R.id.btn_bug);
            mBugButton.setOnClickListener(this);
            itemView.findViewById(R.id.result_card).setOnClickListener(this);
        }
        public void setClickListener(ClickListener clickListener) {
            mListener = clickListener;
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                if (v.getId() == R.id.result_card) {
                    mListener.onShowCard(getAdapterPosition());
                } else if (v.getId() == R.id.btn_bug) {
                    mListener.onShowCreationTicket(getAdapterPosition());
                }
            }
        }

        public interface ClickListener {
            void onShowCard(int position);
            void onShowCreationTicket(int position);
        }
    }

}
