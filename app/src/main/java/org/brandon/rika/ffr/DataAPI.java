package org.brandon.rika.ffr;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by Rika on 2/15/2015.
 */
public class DataAPI {

    private SQLiteDatabase db;
    private static DatabaseHandler dbh;

    private ArrayList<Integer> bodyparts;
    private ArrayList<String> equipment;
    private ArrayList<Integer> moves;

    private static Integer MOVE_COUNT = 24;

    public DataAPI(Context c) {
        dbh = DatabaseHandler.getInstance(c);
        db = dbh.getWritableDatabase();
    }

    /*
    Retrieve list of bodyparts to exercise
     */
    ArrayList<String> getBodyParts(Context context) {
        ArrayList<String> list = new ArrayList<String>();
        bodyparts=new ArrayList<Integer>();
        int bpNum1 = dbh.getBodyPart(db);
        list.add(dbh.getBodyPartName(db, bpNum1));
        bodyparts.add(bpNum1);
        int bpNum2 = dbh.getBodyPart(db);
        if (bpNum1 != bpNum2) {
            list.add(dbh.getBodyPartName(db, bpNum2));
            bodyparts.add(bpNum2);
        }
        return list;
    }

    private void getMoves(Context c) {
        Random random = new Random();
        moves = new ArrayList<Integer>();
        for(int i = 0; i < MOVE_COUNT; i++) {
            int move_id = dbh.getMove(db, random.nextInt(bodyparts.size()));
            moves.add(move_id);
        }
    }

    /*
    Retrieve list of equipment needed
     */
    ArrayList<String> getEquipment(Context c) {
        if (moves.isEmpty()) getMoves(c);
        return dbh.getEquipmentList(db, moves);
    }
}
