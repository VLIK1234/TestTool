package amtt.epam.com.amtt.topbutton.view;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.util.ArrayList;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.api.JiraApi;
import amtt.epam.com.amtt.api.JiraApiConst;
import amtt.epam.com.amtt.api.JiraCallback;
import amtt.epam.com.amtt.api.JiraTask;
import amtt.epam.com.amtt.api.exception.AmttException;
import amtt.epam.com.amtt.api.exception.ExceptionHandler;
import amtt.epam.com.amtt.api.rest.RestMethod;
import amtt.epam.com.amtt.api.rest.RestResponse;
import amtt.epam.com.amtt.app.CreateIssueActivity;
import amtt.epam.com.amtt.bo.issue.createmeta.JMetaResponse;
import amtt.epam.com.amtt.processing.ProjectsProcessor;
import amtt.epam.com.amtt.util.Converter;
import amtt.epam.com.amtt.util.PreferenceUtils;
import amtt.epam.com.amtt.util.UtilConstants;

/**
 * Created by Ivan_Bakach on 12.05.2015.
 */
public class CreateTicketView extends TopUnitView implements JiraCallback<JMetaResponse> {
    public CreateTicketView(Context context) {
        super(context, context.getString(R.string.label_create_ticket));
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onTouchAction() {
        Toast.makeText(getContext(), getContext().getString(R.string.label_create_ticket), Toast.LENGTH_LONG).show();
        RestMethod<JMetaResponse> searchMethod = JiraApi.getInstance().buildDataSearch(JiraApiConst.USER_PROJECTS_PATH, new ProjectsProcessor());
        new JiraTask.Builder<JMetaResponse>()
                .setRestMethod(searchMethod)
                .setCallback(CreateTicketView.this)
                .createAndExecute();
    }
    @Override
    public void onRequestStarted() {

    }

    @Override
    public void onRequestPerformed(RestResponse<JMetaResponse> restResponse) {
        JMetaResponse jiraMetaResponse = restResponse.getResultObject();
        ArrayList<String> projectsNames = jiraMetaResponse.getProjectsNames();
        ArrayList<String> projectsKeys = jiraMetaResponse.getProjectsKeys();
        PreferenceUtils.putSet(UtilConstants.SharedPreference.PROJECTS_NAMES, Converter.arrayListToSet(projectsNames));
        PreferenceUtils.putSet(UtilConstants.SharedPreference.PROJECTS_KEYS, Converter.arrayListToSet(projectsKeys));
        Intent intent = new Intent(getContext(), CreateIssueActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getContext().getApplicationContext().startActivity(intent);
    }

    @Override
    public void onRequestError(AmttException e) {
        ExceptionHandler.getInstance().processError(e).showDialog(getContext(), CreateTicketView.this);
    }
}
