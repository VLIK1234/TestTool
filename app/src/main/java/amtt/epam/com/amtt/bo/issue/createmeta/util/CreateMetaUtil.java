package amtt.epam.com.amtt.bo.issue.createmeta.util;

import android.util.Log;

import java.util.ArrayList;

import amtt.epam.com.amtt.bo.issue.createmeta.JIssueTypes;
import amtt.epam.com.amtt.bo.issue.createmeta.JMetaResponse;
import amtt.epam.com.amtt.bo.issue.createmeta.JProjects;

/**
 * Created by shiza on 01.04.2015.
 */
//This is how you should not write Util classes :)
public class CreateMetaUtil {

    //todo why we need those? delete, use local variables instead
    private ArrayList<JIssueTypes> issueTypes;
    private ArrayList<String> projectsNames = new ArrayList<String>();
    private ArrayList<String> issueTypesNames = new ArrayList<String>();

    public CreateMetaUtil() {
    }

    //todo why not static?
    public ArrayList<String> getProjectsNames(JMetaResponse jMetaResponse) {

        int size = jMetaResponse.getProjects().size();
        Log.d("CreateMetaUtil_getPN", String.valueOf(size));
        for (int i = 0; i < size; i++) {
            projectsNames.add(jMetaResponse.getProjects().get(i).getName());
            Log.d("CreateMetaUtil_getPN", jMetaResponse.getProjects().get(i).getName());
        }

        return projectsNames;
    }

    public ArrayList<String> getIssueTypesNames(JProjects projects) {

        int size = projects.getIssuetypes().size();
        Log.d("CreateMetaUtil_getITN", String.valueOf(size));
        for (int i = 0; i < size; i++) {
            issueTypesNames.add(projects.getIssuetypes().get(i).getName());
        }

        return issueTypesNames;
    }


}
