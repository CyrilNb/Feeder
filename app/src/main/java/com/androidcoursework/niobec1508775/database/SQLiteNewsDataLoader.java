package com.androidcoursework.niobec1508775.database;

import android.content.Context;

import com.androidcoursework.niobec1508775.rssreader.RssNews;

import java.util.ArrayList;

public class SQLiteNewsDataLoader extends AbstractDataLoader<ArrayList<RssNews>> {
    private DataSource<RssNews> mDataSource;


    public SQLiteNewsDataLoader(Context context, DataSource dataSource, String selection, String[] selectionArgs,
                                String groupBy, String having, String orderBy) {
        super(context);
        mDataSource = dataSource;

    }

    @Override
    protected ArrayList<RssNews> buildList() {
        ArrayList<RssNews> rssNewsLists = mDataSource.read();
        return rssNewsLists;
    }

    public void insert(RssNews entity) {
        new InsertTask(this).execute(entity);
    }

    public void update(RssNews entity,String newName) {
        new UpdateTask(this).execute(entity);

    }

    public void delete(RssNews entity) {
        new DeleteTask(this).execute(entity);
    }

    private class InsertTask extends ContentChangingTask<RssNews, Void, Void> {
        InsertTask(SQLiteNewsDataLoader loader) {
            super(loader);
        }

        @Override
        protected Void doInBackground(RssNews... params) {
            mDataSource.insert(params[0]);
            return (null);
        }
    }

    private class UpdateTask extends ContentChangingTask<RssNews, Void, Void> {
        UpdateTask(SQLiteNewsDataLoader loader) {
            super(loader);
        }

        @Override
        protected Void doInBackground(RssNews... params) {
            mDataSource.update(params[0]);
            return (null);
        }
    }

    private class DeleteTask extends ContentChangingTask<RssNews, Void, Void> {
        DeleteTask(SQLiteNewsDataLoader loader) {
            super(loader);
        }

        @Override
        protected Void doInBackground(RssNews... params) {
            mDataSource.delete(params[0]);
            return (null);
        }
    }
}
