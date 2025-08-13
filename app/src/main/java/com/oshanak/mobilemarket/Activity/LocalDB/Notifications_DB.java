package com.oshanak.mobilemarket.Activity.LocalDB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.oshanak.mobilemarket.Activity.Models.Notification_Model;

public class Notifications_DB extends SQLiteOpenHelper {
    private static final int Version=1;

    private static final String DatabaseName="notifications_database";
    private static final String TableName="notifications_tbl";

    public Notifications_DB(@Nullable Context context) {
        super(context, DatabaseName, null, Version);
    }

    private static final String ID="id";

    private static final String NOTIFICATION_DATE="notification_date";
    private static final String NOTIFICATION_ID="notification_id";
    private static final String NOTIFICATION_STATUS="notification_status";


    @Override
    public void onCreate(SQLiteDatabase db) {

        String cQuery="CREATE TABLE "+TableName+" ( " +
                ID+" INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE," +
                NOTIFICATION_ID+" VARCHAR UNIQUE," +
                NOTIFICATION_DATE+" VARCHAR ," +
                NOTIFICATION_STATUS+" VARCHAR);" ;

        db.execSQL(cQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void insertNotificationToDatabase(Notification_Model notification_model){


        SQLiteDatabase idb=this.getWritableDatabase();
        ContentValues icv=new ContentValues();

//        icv.put(ID, recyclerView_log_model.id);
        icv.put(NOTIFICATION_ID, notification_model.getNotification_Id());
        icv.put(NOTIFICATION_DATE, notification_model.getNotification_Date());
        icv.put(NOTIFICATION_STATUS, notification_model.getNotification_Status());


        idb.insert(TableName, null, icv);
        idb.close();

    }

    public Notification_Model getNotificationByID(String gID) {

        Notification_Model gLogModel = new Notification_Model();
        SQLiteDatabase gdb = this.getReadableDatabase();
        String gQuery = "SELECT * FROM " + TableName + " WHERE " + NOTIFICATION_ID + "=" + "'"+gID+"'";
        Cursor gCur = gdb.rawQuery(gQuery, null);
        if (gCur.moveToFirst()) {
            gLogModel.Notification_Id = gCur.getString(1)+"";
            gLogModel.Notification_Date = gCur.getString(2);
            gLogModel.Notification_Status = gCur.getString(3);

        }


        return gLogModel;
    }
//    public ArrayList<RecyclerView_Log_Model> GetAllCallLogs(){
//        ArrayList<RecyclerView_Log_Model> logsList=new ArrayList<>();
//
//
//        String query1="SELECT * FROM " + TableName;
//        SQLiteDatabase db=this.getReadableDatabase();
//        Cursor gCur= db.rawQuery(query1,null);
//        if(gCur.moveToFirst()){
//            do{
//                RecyclerView_Log_Model gfile=new RecyclerView_Log_Model();
//                gfile.id = gCur.getString(0);
//
//                gfile.line_id = gCur.getString(1);
//                gfile.line_number = gCur.getString(2);
//                gfile.number = gCur.getString(3);
//                gfile.date = gCur.getString(4);
//                gfile.ring_count = gCur.getString(5);
//                gfile.duration = gCur.getString(6);
//                gfile.status = gCur.getString(7);
//                gfile.sync_status = gCur.getString(8);
//                gfile.sim_number = gCur.getString(9);
//                gfile.call_type = gCur.getString(10);
//
//                logsList.add(gfile);
//            }while(gCur.moveToNext());
//        }
//
//
//        return logsList;
//    }


    public void updateLogdata(Notification_Model fileModel) {

        SQLiteDatabase udbs = this.getWritableDatabase();
        ContentValues ucv = new ContentValues();
        ucv.put(NOTIFICATION_STATUS, "seen");

        udbs.update(TableName, ucv, ID + " = ?", new String[] {String.valueOf(fileModel.Notification_Id)});

    }


}
