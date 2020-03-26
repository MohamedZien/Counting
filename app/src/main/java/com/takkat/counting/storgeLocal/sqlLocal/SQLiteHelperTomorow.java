package com.takkat.counting.storgeLocal.sqlLocal;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

public class SQLiteHelperTomorow extends SQLiteOpenHelper {


    public SQLiteHelperTomorow(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);


    }


    public void queryData(String sql){

        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql);

    }


    public void insertData(int name , String price){

        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO TOMOROW VALUES (NULL, ?, ?)";

        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        statement.bindDouble(1 ,name);
        statement.bindString(2 ,price);


        statement.executeInsert();
    }


    public void deleteData(int id){

        SQLiteDatabase database = getWritableDatabase();
        String sql = "DELETE FROM TOMOROW WHERE id = ?" ;
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindDouble(1 , (double) id);
        statement.execute();
        database.close();



    }

    public Cursor getData(String sql){

        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql , null);
    }


    public void deleteAll()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from TOMOROW");
        db.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
