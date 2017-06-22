package com.androidcoursework.niobec1508775.database;

import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public abstract class DataSource<T>{
    protected SQLiteDatabase mDatabase;
    public DataSource(SQLiteDatabase database) {
        mDatabase = database;
    }
    public abstract boolean insert(T entity);
    public abstract boolean delete(T entity);
    public abstract boolean update(T entity);
    public abstract ArrayList read();

}
