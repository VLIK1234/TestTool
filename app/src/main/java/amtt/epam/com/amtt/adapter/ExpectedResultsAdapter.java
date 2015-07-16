package amtt.epam.com.amtt.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.excel.bo.GoogleEntryWorksheet;
import amtt.epam.com.amtt.util.Constants;
import amtt.epam.com.amtt.util.Logger;

/**
 * @author Iryna Monchanka
 * @version on 27.05.2015
 */

public class ExpectedResultsAdapter extends RecyclerView.Adapter<ExpectedResultsAdapter.ViewHolder> {

    private final String TAG = this.getClass().getSimpleName();
    private List<GoogleEntryWorksheet> mTestcases;
    private int mItemLayout;
    private ViewHolder.ClickListener mClickListener;

    public ExpectedResultsAdapter(List<GoogleEntryWorksheet> testcases, int itemLayout, ViewHolder.ClickListener clickListener) {
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
        GoogleEntryWorksheet testcase = mTestcases.get(i);
        Logger.d(TAG, testcase.getTestCaseNameGSX());
        viewHolder.mLabel.setText(testcase.getLabelGSX());
        viewHolder.mTestcaseName.setText(testcase.getTestCaseNameGSX() + Constants.Symbols.ID_LEFT_BRACKET
                + testcase.getIdGSX() + Constants.Symbols.ID_RIGHT_BRACKET);
        viewHolder.mPriority.setText(testcase.getPriorityGSX());
        viewHolder.mSteps.setText(testcase.getTestStepsGSX());
    }

    @Override
    public int getItemCount() {
        return mTestcases == null ? 0 : mTestcases.size();
    }

    public ArrayList<String> getIdTestcaseList() {
        ArrayList<String> idTestcaseList = new ArrayList<>();
        for (GoogleEntryWorksheet testcase : mTestcases) {
            idTestcaseList.add(testcase.getIdGSX());
        }
        return idTestcaseList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mLabel;
        public TextView mTestcaseName;
        public TextView mPriority;
        public TextView mSteps;
        private ClickListener mListener;

        public ViewHolder(View itemView) {
            super(itemView);
            mLabel = (TextView) itemView.findViewById(R.id.tv_label);
            mTestcaseName = (TextView) itemView.findViewById(R.id.tv_testcase_name);
            mPriority = (TextView) itemView.findViewById(R.id.tv_priority);
            mSteps = (TextView) itemView.findViewById(R.id.tv_steps);
            itemView.setOnClickListener(this);
        }
        public void setClickListener(ClickListener clickListener) {
            mListener = clickListener;
        }
        @Override
        public void onClick(View v) {
            if (mListener != null) {
                if(v.getId()==R.id.result_card){
                mListener.onItemSelected(getAdapterPosition());}
            }
        }

        public interface ClickListener {
            void onItemSelected(int position);
        }
    }

}
