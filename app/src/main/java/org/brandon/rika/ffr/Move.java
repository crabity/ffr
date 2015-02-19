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

    public Move(SQLiteDatabase db, Context c, Integer moveID, Integer wID, Integer pos) {
        workoutID = wID;
        position = pos;
        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHandler.TABLE_MOVES + " WHERE " + DatabaseHandler.MOVES_ID + " = " + moveID, null);
        moveID = cursor.getInt(0);
        description = cursor.getString(2);
        name = cursor.getString(1);
        weightID = cursor.getInt(4);
        cursor = db.rawQuery("SELECT * FROM " + DatabaseHandler.TABLE_WEIGHTS + " WHERE " + DatabaseHandler.WEIGHTS_ID + "=" + weightID, null);
        weight = cursor.getInt(1);
        cursor.close();
    }
}
