package org.brandon.rika.ffr;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by JJ on 2/19/2015.
 */
public class Move {

    public Integer moveID = 0;
    public Integer position = 0;
    public Integer reps = 0;
    public Integer weight = 0;
    public Integer weightID = 0;
    public Integer workoutID = 0;

    public String description = "";
    public String name = "";

    private SQLiteDatabase database;

    public Move(SQLiteDatabase db, Integer mID, Integer wID, Integer pos) {

        moveID = mID;
        database = db;
        workoutID = wID;
        position = pos;
        Cursor cursor = DatabaseHandler.getMove(db, mID);
        description = cursor.getString(2);
        name = cursor.getString(1);
        weightID = cursor.getInt(4);
        weight = getWeightNum();
        cursor.close();
        reps = DatabaseHandler.getLastRep(db, mID);
    }

    private Integer getWeightNum() {
        return DatabaseHandler.getWeightNum(database, weightID);
    }

    public void setRepsDown() {
        if (reps > 1) {
            reps--;
        }
    }
    public void setRepsUp() {
        reps++;
    }

    public void setWeightDown() {
        if (weightID > 1) {
            weightID--;
            weight = getWeightNum();
        }
    }
    public void setWeightUp() {
        weightID++;
        weight = getWeightNum();
        if (weight == 0) {
            weightID--;
            weight = getWeightNum();
        }
    }
}
