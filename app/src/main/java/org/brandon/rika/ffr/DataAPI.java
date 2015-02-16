package org.brandon.rika.ffr;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Rika on 2/15/2015.
 */
public class DataAPI {

    private SQLiteDatabase db;
    private static DatabaseHandler dbh;

    public DataAPI(Context c) {
        dbh = DatabaseHandler.getInstance(c);
        db = dbh.getWritableDatabase();
    }

    /*
    Retrieve list of bodyparts to exercise
     */
    ArrayList<String> getBodyParts(Context context){
        ArrayList<String> list = new ArrayList<String>();
        int bpNum1 = dbh.getBodyPart(db);
        list.add(dbh.getBodyPartName(db, bpNum1));
        int bpNum2 = dbh.getBodyPart(db);
        if (bpNum1 != bpNum2) {
            list.add(dbh.getBodyPartName(db, bpNum2));
        }
        return list;
    }

    /*
    Retrieve list of equipment needed
     */
    ArrayList<String> getEquipment(Context context){
        ArrayList<String> list = new ArrayList<String>();
        list.add("Weights");
        list.add("Yoga Mat");
        return list;
    }
}
