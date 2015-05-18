package amtt.epam.com.amtt.ticket;

/**
 * Created by Iryna_Monchanka on 15.05.2015.
 */
public interface ContentLoadingCallback<Content> {
    void resultFromBackend(Content result, JiraContentConst tagResult, JiraGetContentCallback jiraGetContentCallback);
}
