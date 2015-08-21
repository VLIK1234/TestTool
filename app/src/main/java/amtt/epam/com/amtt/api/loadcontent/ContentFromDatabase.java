package amtt.epam.com.amtt.api.loadcontent;

import java.util.List;

import amtt.epam.com.amtt.bo.JPriorityResponse;
import amtt.epam.com.amtt.bo.JProjectsResponse;
import amtt.epam.com.amtt.bo.issue.createmeta.JIssueTypes;
import amtt.epam.com.amtt.bo.issue.createmeta.JProjects;
import amtt.epam.com.amtt.bo.project.JPriority;
import amtt.epam.com.amtt.common.Callback;
import amtt.epam.com.amtt.database.object.DbObjectManager;
import amtt.epam.com.amtt.database.table.IssuetypeTable;
import amtt.epam.com.amtt.database.table.PriorityTable;
import amtt.epam.com.amtt.database.table.ProjectTable;

/**
 * @author Iryna Monchanka
 * @version on 6/10/2015
 */

public class ContentFromDatabase {

    private static DbObjectManager mManager = DbObjectManager.INSTANCE;

    public static void getProjects(String idUser, Callback<List<JProjects>> result) {
        mManager.query(new JProjects(), null, new String[]{ProjectTable._ID_USER}, new String[]{idUser}, result);
    }

    public static void getIssueTypes(String projectKey, Callback<List<JIssueTypes>> result) {
        mManager.query(new JIssueTypes(), null, new String[]{IssuetypeTable._KEY_PROJECT}, new String[]{projectKey}, result);
    }

    public static void getPriorities(String url, Callback<List<JPriority>> result) {
        mManager.query(new JPriority(), null, new String[]{PriorityTable._URL}, new String[]{url}, result);
    }

    public static void setPriorities(JPriorityResponse priorities, Callback<Integer> result){
        mManager.add(priorities.getPriorities(), result);
    }

    public static void setProjects(JProjectsResponse projects, Callback<Integer> result){
        mManager.add(projects.getProjects(), result);
    }

    public static void setIssueTypes(JProjects project, Callback<Integer> result){
        mManager.add(project.getIssueTypes(), result);
    }

}
