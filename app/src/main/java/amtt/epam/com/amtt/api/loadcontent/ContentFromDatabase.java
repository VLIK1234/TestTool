package amtt.epam.com.amtt.api.loadcontent;

import java.util.List;

import amtt.epam.com.amtt.bo.JPriorityResponse;
import amtt.epam.com.amtt.bo.JProjectsResponse;
import amtt.epam.com.amtt.bo.issue.createmeta.JIssueTypes;
import amtt.epam.com.amtt.bo.issue.createmeta.JProjects;
import amtt.epam.com.amtt.bo.project.JPriority;
import amtt.epam.com.amtt.common.Callback;
import amtt.epam.com.amtt.database.DataBaseApi;
import amtt.epam.com.amtt.database.table.IssuetypeTable;
import amtt.epam.com.amtt.database.table.PriorityTable;
import amtt.epam.com.amtt.database.table.ProjectTable;
import amtt.epam.com.amtt.database.util.DbSelectionUtil;

/**
 * @author Iryna Monchanka
 * @version on 6/10/2015
 */

public class ContentFromDatabase {

    private static final DataBaseApi mDataBaseApi = DataBaseApi.getInstance();

    public static void getProjects(String idUser, Callback<List<JProjects>> result) {
        String selection = DbSelectionUtil.equal(ProjectTable._ID_USER);
        mDataBaseApi.query(new JProjects(), null, selection, new String[]{idUser}, null, result);
    }

    public static void getIssueTypes(String projectKey, Callback<List<JIssueTypes>> result) {
        String selection = DbSelectionUtil.equal(IssuetypeTable._KEY_PROJECT);
        mDataBaseApi.query(new JIssueTypes(), null, selection, new String[]{projectKey}, null, result);
    }

    public static void getPriorities(String url, Callback<List<JPriority>> result) {
        String selection = DbSelectionUtil.equal(PriorityTable._URL);
        mDataBaseApi.query(new JPriority(), null, selection, new String[]{url}, null, result);
    }

    public static void setPriorities(JPriorityResponse priorities, Callback<Integer> result) {
        mDataBaseApi.bulkInsert(priorities.getPriorities(), result);
    }

    public static void setProjects(JProjectsResponse projects, Callback<Integer> result) {
        mDataBaseApi.bulkInsert(projects.getProjects(), result);
    }

    public static void setIssueTypes(JProjects project, Callback<Integer> result) {
        mDataBaseApi.bulkInsert(project.getIssueTypes(), result);
    }
}
