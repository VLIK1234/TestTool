package amtt.epam.com.amtt.bo.issue.createmeta.util;

import amtt.epam.com.amtt.bo.issue.createmeta.JMetaResponse;
import amtt.epam.com.amtt.bo.issue.createmeta.JProjects;

import java.util.ArrayList;

/**
 * Created by Irina Monchenko on 01.04.2015.
 */
//This is how you should not write Util classes :)
public class CreateMetaObjectsHelper {
    public CreateMetaObjectsHelper() {
    }

    public static ArrayList<String> getProjectsNames(JMetaResponse jMetaResponse) {
        ArrayList<String> projectsNames = new ArrayList<>();
        int size = jMetaResponse.getProjects().size();
        for (int i = 0; i < size; i++) {
            projectsNames.add(jMetaResponse.getProjects().get(i).getName());
        }
        return projectsNames;
    }

    public static ArrayList<String> getIssueTypesNames(JProjects projects) {
        ArrayList<String> issueTypesNames = new ArrayList<>();
        int size = projects.getIssuetypes().size();
        for (int i = 0; i < size; i++) {
            issueTypesNames.add(projects.getIssuetypes().get(i).getName());
        }
        return issueTypesNames;
    }
}
