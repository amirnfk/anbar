package com.oshanak.mobilemarket.Activity.LocalDB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by mfarahani.
 */

public class DBHandler extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "sevenDB";
    private static final String TABLE_PARAM = "Param";

    // Table Columns names
    private static final String ParamName = "ParamName";
    private static final String ParamValue = "ParamValue";

    public DBHandler(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String CREATE_PARAM_TABLE = "CREATE TABLE Param ("
        + " ParamName TEXT PRIMARY KEY,"
        + " ParamValue TEXT" + ")";
        db.execSQL(CREATE_PARAM_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PARAM);
//        onCreate(db);
    }

    private void insertParam(Param param)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ParamName, param.getParamName());
        values.put(ParamValue, param.getParamValue());

        db.insert(TABLE_PARAM, null, values);
        db.close();
    }
    private int updateParam(Param param)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ParamValue, param.getParamValue());
        int count = db.update(TABLE_PARAM
                , values
                , ParamName + " = ?"
                , new String[]{param.getParamName()});
        db.close();
        return count;
    }

    private Param selectParam(String paramName)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String select = "select ParamName, ParamValue from Param where ParamName = '"+ paramName + "'";
        Cursor cursor = db.rawQuery(select, null);

        if (cursor == null || cursor.getCount() == 0) return null;

        cursor.moveToFirst();
        Param param = new Param( cursor.getString(0), cursor.getString(1));
        db.close();
        return param;
    }


    public void setParamValue(String paramName, String paramValue)
    {
        Param p = new Param(paramName,  paramValue);
        int rowCount = updateParam(p);
        if(rowCount < 1)
        {
            insertParam(p);
        }
    }
    public String getParamValue(String paramName)
    {
        Param p =selectParam(paramName);
        return (p != null) ? p.getParamValue() : "";
    }
}
