package amtt.epam.com.amtt.api;

/**
 @author Iryna Monchanka
 @version on 5/13/2015
 */

public interface GetContentCallback<Content> {
    void resultOfDataLoading(Content result);
}
