package amtt.epam.com.amtt.ticket;

/**
 * Created by shiza on 15.05.2015.
 */
public interface ContentLoadingCallback<Content> {
    void resultFromBackend(Content result, JiraContentConst tagResult, JiraGetContentCallback jiraGetContentCallback);
}
