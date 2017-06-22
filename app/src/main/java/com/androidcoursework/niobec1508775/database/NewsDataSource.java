package com.androidcoursework.niobec1508775.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.androidcoursework.niobec1508775.rssreader.RssNews;

import java.util.ArrayList;

public class NewsDataSource extends DataSource<RssNews> {
    public static final String TABLE_NAME = "news";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DESC = "desc";
    //public static final String COLUMN_DATE = "date";
    public static final String COLUMN_URLSITE= "urlsite";


    // Database creation sql statement
    public static final String CREATE_COMMAND = "create table " + TABLE_NAME
            + "(" + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_TITLE + " text not null, "
            + COLUMN_DESC + " text not null, "
            //+ COLUMN_DATE + " text not null, "
            + COLUMN_URLSITE + " text not null );";


    public NewsDataSource(SQLiteDatabase database) {
        super(database);
    }

    @Override
    public boolean insert(RssNews entity) {
        if (entity == null) {
            return false;
        }
        long result = mDatabase.insert(TABLE_NAME, null,
                generateContentValuesFromObject(entity));
        return result != -1;
    }

    @Override
    public boolean delete(RssNews entity) {
        if (entity == null) {
            return false;
        }
        int result = mDatabase.delete(TABLE_NAME,
                COLUMN_ID + " = " + entity.getId(), null);
        return result != 0;
    }

    @Override
    public boolean update(RssNews entity) {
        if (entity == null) {
            return false;
        }
        int result = mDatabase.update(TABLE_NAME,
                generateContentValuesFromObject(entity), COLUMN_ID + " = "
                        + entity.getId(), null);
        return result != 0;

    }

    @Override
    public ArrayList<RssNews> read() {
        Cursor cursor = mDatabase.query(TABLE_NAME, getAllColumns(), null,
                null, null, null, null);
        ArrayList<RssNews> tasks = new ArrayList();
        if (cursor != null && cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                tasks.add(generateObjectFromCursor(cursor));
                cursor.moveToNext();
            }
            cursor.close();
        }
        return tasks;
    }


    public String[] getAllColumns() {
        //return new String[] { COLUMN_ID, COLUMN_TITLE, COLUMN_DESC, COLUMN_DATE, COLUMN_URLSITE };
        return new String[] { COLUMN_ID, COLUMN_TITLE, COLUMN_DESC, COLUMN_URLSITE };
    }

    public RssNews generateObjectFromCursor(Cursor cursor) {
        if (cursor == null) {
            return null;
        }
        RssNews rssNews = new RssNews();
        rssNews.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
        rssNews.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)));
        rssNews.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_DESC)));
        //rssNews.setDate(cursor.getString(cursor.getColumnIndex(COLUMN_DATE)));
        rssNews.setOriginalSite(cursor.getString(cursor.getColumnIndex(COLUMN_URLSITE)));
        return rssNews;
    }

    public ContentValues generateContentValuesFromObject(RssNews entity) {
        if (entity == null) {
            return null;
        }
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, entity.getTitle());
        values.put(COLUMN_DESC, entity.getDescription());
        //values.put(COLUMN_DATE, entity.getDatePublication());
        values.put(COLUMN_URLSITE, entity.getOriginalSite());
        return values;
    }

}