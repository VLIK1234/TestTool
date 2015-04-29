package amtt.epam.com.amtt.app;

import android.os.Bundle;
import android.app.FragmentManager;

import amtt.epam.com.amtt.database.task.DataBaseCallback;
import amtt.epam.com.amtt.database.task.DataBaseOperationType;
import amtt.epam.com.amtt.database.task.DataBaseResponse;
import amtt.epam.com.amtt.database.task.DataBaseTask;
import amtt.epam.com.amtt.database.task.DataBaseTaskResult;

@SuppressWarnings("unchecked")
public class LoginActivity extends BaseActivity implements DataBaseCallback<Boolean> {

    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragmentManager = getFragmentManager();
        checkUsersInDB();
    }

    private void checkUsersInDB() {
        new DataBaseTask.Builder()
                .setContext(this)
                .setCallback(this)
                .setOperationType(DataBaseOperationType.CHECK_USERS_AVAILABILITY)
                .create()
                .execute();
    }

    @Override
    public void onDataBaseActionDone(DataBaseResponse<Boolean> dataBaseResponse) {
        if (dataBaseResponse.getTaskResult() == DataBaseTaskResult.DONE) {
            if (dataBaseResponse.getValueResult()) {
                mFragmentManager.beginTransaction().add()
            }
        }
    }
}