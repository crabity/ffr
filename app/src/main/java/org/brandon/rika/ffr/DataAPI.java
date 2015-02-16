package org.brandon.rika.ffr;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Rika on 2/15/2015.
 */
public class DataAPI {

    private SQLiteDatabase db;
    private static DatabaseHandler dbh;

    private HashMap<String, Integer> bodyparts;
    private HashMap<String, Integer> equipment;
    private HashMap<String, Integer> moves;

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
        bodyparts.put(dbh.getBodyPartName(db, bpNum1), bpNum1);
        int bpNum2 = dbh.getBodyPart(db);
        if (bpNum1 != bpNum2) {
            list.add(dbh.getBodyPartName(db, bpNum2));
            bodyparts.put(dbh.getBodyPartName(db, bpNum2), bpNum2);
        }
        return list;
    }

    private void getMoves(Context c) {

    }

    /*
    Retrieve list of equipment needed
     */
    ArrayList<String> getEquipment(Context context){
        if(moves.isEmpty()) getMoves(context);
        ArrayList<String> list = new ArrayList<String>();
        list.add("Weights");
        list.add("Yoga Mat");
        return list;
    }
}
