package amtt.epam.com.amtt.bo.issue.createmeta.util;

import amtt.epam.com.amtt.bo.issue.createmeta.JIssueTypes;
import amtt.epam.com.amtt.bo.issue.createmeta.JMetaResponse;
import amtt.epam.com.amtt.bo.issue.createmeta.JProjects;

import java.util.ArrayList;

/**
 * Created by shiza on 01.04.2015.
 */
public class CreateMetaUtil {

    private ArrayList<JIssueTypes> issueTypes;
    private ArrayList<String> projectsNames;
    private ArrayList<String> issueTypesNames;

    public CreateMetaUtil() {
    }

    public ArrayList<String> getProjectsNames(JMetaResponse jMetaResponse) {

        int size = jMetaResponse.getProjects().size();
        for (int i = 0; i < size; i++) {
            projectsNames.add(jMetaResponse.getProjects().get(i).getName());
        }

        return projectsNames;
    }

    public ArrayList<String> getIssueTypesNames(JProjects projects) {

        int size = projects.getIssuetypes().size();
        for (int i = 0; i < size; i++) {
            issueTypesNames.add(projects.getIssuetypes().get(i).getName());
        }

        return issueTypesNames;
    }


}
