package amtt.epam.com.amtt.ticket;

/**
 * @author Iryna Monchanka
 * @version on 6/10/2015
 */

public class ContentFromDatabase {

    private static class ContentFromDatabaseHolder {
        public static final ContentFromDatabase INSTANCE = new ContentFromDatabase();
    }

    public static ContentFromDatabase getInstance() {
        return ContentFromDatabaseHolder.INSTANCE;
    }

    public void getProjects(final JiraGetContentCallback jiraGetContentCallback) {

    }

    public void getVersions(String projectsKey, final JiraGetContentCallback jiraGetContentCallback) {
    }

    public void getPriority(final JiraGetContentCallback jiraGetContentCallback) {

    }
}
