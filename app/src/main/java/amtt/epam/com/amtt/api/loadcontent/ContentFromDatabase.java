package amtt.epam.com.amtt.api.loadcontent;

import java.util.List;

import amtt.epam.com.amtt.bo.JPriorityResponse;
import amtt.epam.com.amtt.bo.JProjectsResponse;
import amtt.epam.com.amtt.bo.issue.createmeta.JIssueTypes;
import amtt.epam.com.amtt.bo.issue.createmeta.JProjects;
import amtt.epam.com.amtt.bo.project.JPriority;
import amtt.epam.com.amtt.bo.user.JUserInfo;
import amtt.epam.com.amtt.database.constant.BaseColumns;
import amtt.epam.com.amtt.database.object.DbObjectManager;
import amtt.epam.com.amtt.database.object.IResult;
import amtt.epam.com.amtt.database.table.IssuetypeTable;
import amtt.epam.com.amtt.database.table.PriorityTable;
import amtt.epam.com.amtt.database.table.ProjectTable;

/**
 * @author Iryna Monchanka
 * @version on 6/10/2015
 */

public class ContentFromDatabase {

    public static void getProjects(String idUser, IResult<List<JProjects>> result) {
        DbObjectManager.INSTANCE.query(new JProjects(), null, new String[]{ProjectTable._ID_USER}, new String[]{idUser}, result);
    }

    public static void getIssueTypes(String projectKey, IResult<List<JIssueTypes>> result) {
        DbObjectManager.INSTANCE.query(new JIssueTypes(), null, new String[]{IssuetypeTable._KEY_PROJECT}, new String[]{projectKey}, result);
    }

    public static void getPriorities(String url, IResult<List<JPriority>> result) {
        DbObjectManager.INSTANCE.query(new JPriority(), null, new String[]{PriorityTable._URL}, new String[]{url}, result);
    }

    public static void setPriorities(JPriorityResponse priorities, IResult<Integer> result){
        DbObjectManager.INSTANCE.add(priorities.getPriorities(), result);
    }

    public static void setProjects(JProjectsResponse projects, IResult<Integer> result){
        DbObjectManager.INSTANCE.add(projects.getProjects(), result);
    }

    public static void setIssueTypes(JProjects project, IResult<Integer> result){
        DbObjectManager.INSTANCE.add(project.getIssueTypes(), result);
    }

    public static void updateUser(int userId, JUserInfo user, IResult<Integer> result) {
        DbObjectManager.INSTANCE.update(user, BaseColumns._ID + "=" + userId, null, result);
    }

}
