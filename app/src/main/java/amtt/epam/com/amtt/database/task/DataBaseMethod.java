package amtt.epam.com.amtt.database.task;

import android.content.Context;
import android.database.Cursor;

import amtt.epam.com.amtt.database.object.DbObjectManger;
import amtt.epam.com.amtt.database.object.DatabaseEntity;
import amtt.epam.com.amtt.processing.Processor;
import amtt.epam.com.amtt.util.ContextHolder;

/**
 * Created by Artyom on 16.05.2015.
 */
public class DataBaseMethod<ResultType> {

    enum DatabaseMethodType {

        ADD_OR_UPDATE,
        REMOVE,
        REMOVE_ALL,
        GET_BY_KEY,
        RAW_QUERY

    }

    public static class Builder<ResultType> {

        private DatabaseMethodType mMethodType;
        private DatabaseEntity mEntity;
        private String mSelection;
        private String[] mSelectionArgs;
        private Processor<ResultType, Cursor> mProcessor;

        public Builder setMethodType(DatabaseMethodType methodType) {
            mMethodType = methodType;
            return this;
        }

        public Builder setEntity(DatabaseEntity entity) {
            mEntity = entity;
            return this;
        }

        public Builder setSelection(String selection) {
            mSelection = selection;
            return this;
        }

        public Builder setSelectionArgs(String[] selectionArgs) {
            mSelectionArgs = selectionArgs;
            return this;
        }

        public Builder setProcessor(Processor<ResultType, Cursor> processor) {
            mProcessor = processor;
            return this;
        }

        public DataBaseMethod create() {
            DataBaseMethod dataBaseMethod = new DataBaseMethod();
            dataBaseMethod.mEntity = this.mEntity;
            dataBaseMethod.mMethodType = this.mMethodType;
            dataBaseMethod.mSelection = this.mSelection;
            dataBaseMethod.mSelectionArgs = this.mSelectionArgs;
            dataBaseMethod.mProcessor = this.mProcessor;
            return dataBaseMethod;
        }

    }

    private DatabaseMethodType mMethodType;
    private DatabaseEntity mEntity;
    private String mSelection;
    private String[] mSelectionArgs;
    private Processor<ResultType, Cursor> mProcessor;



    public DataBaseTask.DataBaseResponse<ResultType> execute(){
        ResultType result = null;
        Cursor cursor = null;
        switch (mMethodType) {
            case ADD_OR_UPDATE:
                result = (ResultType) DbObjectManger.INSTANCE.addOrUpdate(mEntity);
                break;
            case REMOVE:
                DbObjectManger.INSTANCE.remove(mEntity);
                break;
            case REMOVE_ALL:
                DbObjectManger.INSTANCE.removeAll(mEntity);
                break;
            case GET_BY_KEY:
                result = (ResultType) DbObjectManger.INSTANCE.getByKey(mEntity);
                break;
            case RAW_QUERY:
                cursor = ContextHolder.getContext().getContentResolver().query(mEntity.getUri(), null, mSelection + "=?", mSelectionArgs, null);
                break;
            default:
                break;
        }
        if (mProcessor != null) {
            result = mProcessor.process(cursor);
        }
        return new DataBaseTask.DataBaseResponse<>(result);
    }

}