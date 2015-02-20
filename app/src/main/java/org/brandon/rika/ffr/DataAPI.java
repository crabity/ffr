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

    private static DataAPI i_dataapi;

    private SQLiteDatabase db;
    private DatabaseHandler dbh;

    private ArrayList<Integer> bodyparts;
    private ArrayList<Integer> moves;

    static Integer MOVE_COUNT = 5;
    private int current_move_idx;

    public DataAPI(Context c) {
        dbh = DatabaseHandler.getInstance(c);
        db = dbh.getWritableDatabase();

        newWorkout();
    }

    public static DataAPI getInstance(Context c){
        if(i_dataapi==null){
            i_dataapi = new DataAPI(c.getApplicationContext());
        }
        return i_dataapi;
    }

    ArrayList<String> getBodyParts() {
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

    public SQLiteDatabase getDB(){
        return db;
    }

    ArrayList<String> getEquipment() {
        return dbh.getEquipmentList(db, moves);
    }

    void getMoves() {
        Random random = new Random();
        moves = new ArrayList<Integer>();
        for(int i = 0; i < MOVE_COUNT; i++) {
            int move_id = dbh.getRandomMoveID(db, bodyparts.get(random.nextInt(bodyparts.size())));
            moves.add(move_id);
        }
    }

    public int getMoveID(){
        return moves.get(current_move_idx - 1);
    }

    public int getMoveIDX(){
        return current_move_idx;
    }

    ArrayList<String> getMoveList() {
        ArrayList<String> move_names = new ArrayList<>();
        for(int i=0; i < moves.size(); i++){
            move_names.add(dbh.getMoveName(db, moves.get(i)));
        }
        return move_names;
    }

    public void incrMoveIDX(){
        current_move_idx++;
    }

    public boolean isLastMove(){
        return current_move_idx == MOVE_COUNT;
    }

    public void newWorkout(){
        current_move_idx = 1;
    }
}
