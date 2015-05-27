package amtt.epam.com.amtt.ticket;

/**
 @author Iryna Monchanka
 @version on 15.05.2015
 */

public interface ContentLoadingCallback<Content> {
    void resultFromBackend(Content result, JiraContentConst tag, JiraGetContentCallback jiraGetContentCallback);
}
